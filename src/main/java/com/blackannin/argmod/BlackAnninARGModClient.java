package com.blackannin.argmod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * 客户端辅助类（可选）
 * 仅当客户端安装此模组时生效，目前仅提供配置界面支持
 */
@Mod(value = BlackAnninARGMod.MOD_ID, dist = Dist.CLIENT)
public class BlackAnninARGModClient {
    public BlackAnninARGModClient(ModContainer container) {
        // 允许在客户端 Mods 菜单中打开配置界面
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
