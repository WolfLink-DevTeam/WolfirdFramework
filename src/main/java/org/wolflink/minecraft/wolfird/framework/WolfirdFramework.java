package org.wolflink.minecraft.wolfird.framework;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import java.io.File;
import java.util.Calendar;

public final class WolfirdFramework extends SubPlugin {

    public WolfirdFramework() {
        super("Framework", "1.0.0", "MikkoAyaka", Calendar.getInstance());
        super.getLastUpdate().set(2023, Calendar.MAY,9,20,34);
    }

    protected WolfirdFramework(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
