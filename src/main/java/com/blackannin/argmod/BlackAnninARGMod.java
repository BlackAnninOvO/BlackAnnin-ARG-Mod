package com.blackannin.argmod;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

/**
 * BlackAnnin ARG 服务器核心模组
 * 仅限服务端运行，提供基础的服务器限制和 ARG 事件触发逻辑
 */
@Mod(BlackAnninARGMod.MOD_ID)
public class BlackAnninARGMod {
    public static final String MOD_ID = "blackannin_arg_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BlackAnninARGMod(IEventBus modEventBus, ModContainer modContainer) {
        // 注册通用设置
        modEventBus.addListener(this::commonSetup);

        // 注册服务器事件处理器
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(ServerEventHandler.class);

        // 注册模组配置
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("BlackAnnin ARG 服务器模组已启动。");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ARG 服务器正在启动，已加载安全策略。");
    }
}
