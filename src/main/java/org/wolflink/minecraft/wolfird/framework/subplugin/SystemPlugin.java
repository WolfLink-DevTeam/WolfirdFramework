package org.wolflink.minecraft.wolfird.framework.subplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Calendar;

/**
 * 大型玩法系统拓展插件
 * 例如：天赋树、商店系统、数据统计...
 */
public abstract class SystemPlugin extends SubPlugin {
    public SystemPlugin(String name, String version, String author, Calendar lastUpdate) {
        super(name, version, author, lastUpdate);
    }

    protected SystemPlugin(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
    }
}
