package org.wolflink.minecraft.wolfird.framework.subplugin;

import org.wolflink.minecraft.wolfird.framework.container.SystemContainer;

/**
 * 大型玩法系统拓展插件
 * 例如：天赋树、商店系统、数据统计...
 */
public abstract class SystemPlugin extends SubPlugin {
    public SystemPlugin() {
        init();
    }
    @Override
    protected void init(){
        SystemContainer container = getContainer();
        container.pluginItems.put(info.getFullName(),this);
    }
}
