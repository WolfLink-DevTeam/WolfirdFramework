package org.wolflink.minecraft.wolfird.framework.subplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Calendar;

/**
 * 小型玩法拓展子插件
 * 例如：温泉、镐子探测、镐子破盾...
 */
public abstract class AddonPlugin extends SubPlugin {
    public AddonPlugin(String name, String version, String author, Calendar lastUpdate) {
        super(name, version, author, lastUpdate);
    }

    protected AddonPlugin(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
    }

    @Override
    protected void init(){

    }
}
