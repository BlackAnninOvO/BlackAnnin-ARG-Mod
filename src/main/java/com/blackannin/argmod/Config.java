package com.blackannin.argmod;

import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 模组配置系统
 * 配置文件生成在 config/blackannin_arg_mod-common.toml
 */
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 白名单设置
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ALLOWED_PLAYERS = BUILDER
            .comment("允许进入服务器的玩家 ID 列表 (仅限服务端)")
            .defineList("allowedPlayers", List.of("HexiY_ovo", "BlackAnnin"), o -> o instanceof String);

    public static final ModConfigSpec.ConfigValue<String> WHITELIST_KICK_MESSAGE = BUILDER
            .comment("未在白名单中的玩家尝试进入时的提示信息")
            .define("whitelistKickMessage", "抱歉，您未被允许加入此服务器");

    // 死亡踢出设置
    public static final ModConfigSpec.IntValue DEATH_KICK_DELAY = BUILDER
            .comment("玩家复活后多少秒被踢出服务器 (秒)")
            .defineInRange("deathKickDelaySeconds", 5, 1, 3600);

    public static final ModConfigSpec.ConfigValue<String> DEATH_KICK_MESSAGE = BUILDER
            .comment("玩家复活被踢出时的提示信息")
            .define("deathKickMessage", "你死掉了");

    // 坐标警告设置
    public static final ModConfigSpec.IntValue WARNING_RADIUS = BUILDER
            .comment("特定坐标触发警告的半径范围 (格)")
            .defineInRange("warningRadius", 20, 1, 100);

    public static final ModConfigSpec.IntValue WARNING_COOLDOWN = BUILDER
            .comment("同一玩家触发警告的冷却时间 (分钟)")
            .defineInRange("warningCooldownMinutes", 20, 1, 1440);

    public static final ModConfigSpec.ConfigValue<String> WARNING_MESSAGE = BUILDER
            .comment("触发警告时显示的文字内容")
            .define("warningMessage", "今晚的活动就要开始了...");

    public static final ModConfigSpec.IntValue WARNING_TARGET_X = BUILDER.defineInRange("targetX", -450, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue WARNING_TARGET_Y = BUILDER.defineInRange("targetY", 135, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue WARNING_TARGET_Z = BUILDER.defineInRange("targetZ", -650, Integer.MIN_VALUE, Integer.MAX_VALUE);

    // 登录传送设置
    public static final ModConfigSpec.IntValue SPAWN_X = BUILDER
            .comment("玩家进入服务器时传送的目标 X 坐标")
            .defineInRange("spawnX", 240, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue SPAWN_Y = BUILDER
            .comment("玩家进入服务器时传送的目标 Y 坐标")
            .defineInRange("spawnY", 65, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue SPAWN_Z = BUILDER
            .comment("玩家进入服务器时传送的目标 Z 坐标")
            .defineInRange("spawnZ", -205, Integer.MIN_VALUE, Integer.MAX_VALUE);

    // 游玩时长限制设置
    public static final ModConfigSpec.IntValue PLAYTIME_LIMIT = BUILDER
            .comment("玩家进入服务器后允许游玩的最大时长 (分钟)")
            .defineInRange("playtimeLimitMinutes", 30, 1, 1440);

    public static final ModConfigSpec.ConfigValue<String> PLAYTIME_KICK_MESSAGE = BUILDER
            .comment("超过游玩时长限制被踢出时的提示信息")
            .define("playtimeKickMessage", "你...怎么还不来找我...");

    // 状态恢复设置
    public static final ModConfigSpec.BooleanValue HEAL_ON_JOIN = BUILDER
            .comment("玩家进入服务器时是否自动回满血量和饱食度")
            .define("healOnJoin", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
