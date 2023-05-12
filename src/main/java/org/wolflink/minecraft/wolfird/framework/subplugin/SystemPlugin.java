package org.wolflink.minecraft.wolfird.framework.subplugin;

import org.wolflink.minecraft.wolfird.framework.container.AddonContainer;
import org.wolflink.minecraft.wolfird.framework.container.ModeContainer;
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
        notifier.info("正在向框架注册该系统插件...");
        SystemContainer container = getContainer();
        if(container.registerSubPlugin(info.getName(),this)) {
            notifier.info("注册完成");
        } else notifier.error("注册失败");
    }
    @Override
    protected void beforeDisable() {
        notifier.info("正在向框架注销该系统插件...");
        SystemContainer container = getContainer();
        if(container.unregisterSubPlugin(info.getName(),this)) {
            notifier.info("注销完成");
        } else notifier.error("注销失败");
    }
}
