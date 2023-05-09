package org.wolflink.minecraft.wolfird.framework.subplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Calendar;

/**
 * 游戏模式拓展插件
 * 例如：生存模式、猎人游戏、时间陷阱、饥饿游戏、极限生存冠军...
 */
public abstract class ModePlugin extends SubPlugin {
    public ModePlugin(String name, String version, String author, Calendar lastUpdate) {
        super(name, version, author, lastUpdate);
    }

    protected ModePlugin(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
    }
}
