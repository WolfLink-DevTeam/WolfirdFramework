package org.wolflink.minecraft.wolfird.framework.subplugin;

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
        ModeContainer container = getContainer();
        container.pluginItems.put(info.getFullName(),this);
    }
}
