package org.wolflink.minecraft.wolfird.framework;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import java.io.File;

/**
 * 框架 Bukkit 插件主类
 */
public final class Framework extends SubPlugin {

    @Getter private static Framework INSTANCE;
    public Framework() {
        init();
    }

    @Override protected void init() {
        INSTANCE = this;
        this.saveDefaultConfig();
    }
    @Override public void onEnable() {
        loadSubPlugins("mode-plugin");
        loadSubPlugins("system-plugin");
        loadSubPlugins("addon-plugin");
    }
    @Override public void onDisable() {}

    private void loadSubPlugins(String subPluginFolderName){
        File subPluginFolder = new File(this.getDataFolder().getPath(),subPluginFolderName);
        if(!subPluginFolder.exists())subPluginFolder.mkdirs();
        //TODO
        System.out.println("测试路径"+subPluginFolder.getPath());
        Bukkit.getPluginManager().loadPlugins(subPluginFolder);
    }
}
