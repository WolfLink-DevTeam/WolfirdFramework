package org.wolflink.minecraft.wolfird.framework.subplugin.addon;

import org.wolflink.minecraft.wolfird.framework.container.AddonContainer;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

/**
 * 小型玩法拓展子插件
 * 例如：温泉、镐子探测、镐子破盾...
 */
public abstract class AddonPlugin extends SubPlugin {

    public AddonPlugin() {
        init();
    }

    @Override
    protected void init() {
        notifier.info("正在向框架注册该拓展插件...");
        AddonContainer container = getContainer();
        if (container.registerSubPlugin(info.getName(), this)) {
            notifier.info("注册完成");
        } else notifier.error("注册失败");
    }

    @Override
    protected void beforeDisable() {
        notifier.info("正在向框架注销该拓展插件...");
        AddonContainer container = getContainer();
        if (container.unregisterSubPlugin(info.getName(), this)) {
            notifier.info("注销完成");
        } else notifier.error("注销失败");
    }

}
