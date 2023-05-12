package org.wolflink.minecraft.wolfird.framework.subplugin;

import org.wolflink.minecraft.wolfird.framework.container.AddonContainer;

/**
 * 小型玩法拓展子插件
 * 例如：温泉、镐子探测、镐子破盾...
 */
public abstract class AddonPlugin extends SubPlugin {

    public AddonPlugin() {
        init();
    }

    @Override
    protected void init(){
        notifier.info("正在向框架注册该拓展插件...");
        AddonContainer container = getContainer();
        if(container.registerSubPlugin(info.getName(),this)) {
            notifier.info("注册完成");
        } else notifier.error("注册失败");
    }

}
