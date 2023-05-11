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
        AddonContainer container = getContainer();
        container.pluginItems.put(info.getFullName(),this);
    }

}
