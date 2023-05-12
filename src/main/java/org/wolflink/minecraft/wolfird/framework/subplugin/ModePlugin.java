package org.wolflink.minecraft.wolfird.framework.subplugin;

import org.wolflink.minecraft.wolfird.framework.container.AddonContainer;
import org.wolflink.minecraft.wolfird.framework.container.ModeContainer;

/**
 * 游戏模式拓展插件
 * 例如：生存模式、猎人游戏、时间陷阱、饥饿游戏、极限生存冠军...
 */
public abstract class ModePlugin extends SubPlugin {

    public ModePlugin() {
        init();
    }
    @Override
    protected void init(){
        notifier.info("正在向框架注册该拓展插件...");
        ModeContainer container = getContainer();
        if(container.registerSubPlugin(info.getName(),this)) {
            notifier.info("注册完成");
        } else notifier.error("注册失败");
    }
}
