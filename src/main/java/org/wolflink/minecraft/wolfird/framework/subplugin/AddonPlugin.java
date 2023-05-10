package org.wolflink.minecraft.wolfird.framework.subplugin;

import lombok.NoArgsConstructor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.wolflink.minecraft.wolfird.framework.ioc.AddonContainer;

import java.io.File;

/**
 * 小型玩法拓展子插件
 * 例如：温泉、镐子探测、镐子破盾...
 */
@NoArgsConstructor
public abstract class AddonPlugin extends SubPlugin {

    protected AddonPlugin(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
    }

    @Override
    protected void init(){
        AddonContainer container = getContainer();
        container.pluginItems.put(info.getFullName(),this);
    }

}
