package com.blackannin.argmod;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器端事件处理器
 * 实现了白名单限制、死亡踢出以及特定坐标警告功能
 */
@EventBusSubscriber(modid = BlackAnninARGMod.MOD_ID)
public class ServerEventHandler {
    // 待踢出玩家的计时器 (UUID -> 踢出时间戳)
    private static final Map<UUID, Long> KICK_TIMERS = new ConcurrentHashMap<>();
    
    // 玩家上次触发坐标警告的时间 (UUID -> 上次触发时间戳)
    private static final Map<UUID, Long> LAST_WARNING_TIME = new ConcurrentHashMap<>();
    
    /**
     * 玩家登录验证逻辑 (白名单)
     */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            String playerName = player.getGameProfile().getName();
            // 从配置读取白名单
            if (!Config.ALLOWED_PLAYERS.get().contains(playerName)) {
                player.connection.disconnect(Component.literal(Config.WHITELIST_KICK_MESSAGE.get()));
            }
        }
    }

    /**
     * 玩家复活逻辑 (死亡踢出计时开始)
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 从配置读取延迟时间 (秒转毫秒)
            long delayMs = Config.DEATH_KICK_DELAY.get() * 1000L;
            KICK_TIMERS.put(player.getUUID(), System.currentTimeMillis() + delayMs);
        }
    }

    /**
     * 玩家刻更新逻辑 (处理踢出计时和坐标警告)
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        UUID uuid = player.getUUID();
        long now = System.currentTimeMillis();

        // 1. 处理复活后的踢出逻辑
        if (KICK_TIMERS.containsKey(uuid)) {
            if (now >= KICK_TIMERS.get(uuid)) {
                KICK_TIMERS.remove(uuid);
                // 从配置读取提示信息
                player.connection.disconnect(Component.literal(Config.DEATH_KICK_MESSAGE.get()));
                return;
            }
        }

        // 2. 处理特定坐标警告逻辑 (仅限主世界)
        if (player.level().dimension() == Level.OVERWORLD) {
            // 从配置读取目标坐标和半径
            double targetX = Config.WARNING_TARGET_X.get();
            double targetY = Config.WARNING_TARGET_Y.get();
            double targetZ = Config.WARNING_TARGET_Z.get();
            double radius = Config.WARNING_RADIUS.get();
            
            double distSq = player.distanceToSqr(targetX, targetY, targetZ);
            if (distSq <= (radius * radius)) {
                long lastWarning = LAST_WARNING_TIME.getOrDefault(uuid, 0L);
                // 从配置读取冷却时间 (分钟转毫秒)
                long cooldownMs = Config.WARNING_COOLDOWN.get() * 60L * 1000L;
                
                if (now - lastWarning >= cooldownMs) {
                    sendProminentWarning(player);
                    LAST_WARNING_TIME.put(uuid, now);
                }
            }
        }
    }

    /**
     * 发送醒目的全屏标题警告
     */
    private static void sendProminentWarning(ServerPlayer player) {
        // 从配置读取警告文字
        Component warningText = Component.literal(Config.WARNING_MESSAGE.get())
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
        
        // 发送标题动画配置：淡入 10 刻 (0.5s), 停留 80 刻 (4s), 淡出 10 刻 (0.5s)
        player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 80, 10));
        // 发送主标题文字
        player.connection.send(new ClientboundSetTitleTextPacket(warningText));
    }

    /**
     * 玩家登出清理
     */
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID uuid = event.getEntity().getUUID();
        KICK_TIMERS.remove(uuid);
        // 注意：警告冷却时间 LAST_WARNING_TIME 通常需要跨登录保存，这里不清理以维持 20 分钟限制
    }
}
