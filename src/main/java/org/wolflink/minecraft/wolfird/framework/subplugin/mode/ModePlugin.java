package org.wolflink.minecraft.wolfird.framework.subplugin.mode;

import org.wolflink.minecraft.wolfird.framework.container.ModeContainer;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

/**
 * 游戏模式拓展插件
 * 例如：生存模式、猎人游戏、时间陷阱、饥饿游戏、极限生存冠军...
 */
public abstract class ModePlugin extends SubPlugin {

    public ModePlugin() {
        init();
    }

    @Override
    protected void init() {
        notifier.info("正在向框架注册该模式插件...");
        ModeContainer container = getContainer();
        if (container.registerSubPlugin(info.getName(), this)) {
            notifier.info("注册完成");
        } else notifier.error("注册失败");
    }

    @Override
    protected void beforeDisable() {
        notifier.info("正在向框架注销该模式插件...");
        ModeContainer container = getContainer();
        if (container.unregisterSubPlugin(info.getName(), this)) {
            notifier.info("注销完成");
        } else notifier.error("注销失败");
    }
}
