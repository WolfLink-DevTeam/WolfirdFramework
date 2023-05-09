package org.wolflink.minecraft.wolfird.framework.subplugin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Calendar;

/**
 * 代表一个 Wolfird 子插件抽象
 * 可能是：Mode(模式插件)、Addon(功能拓展插件)、System(玩法系统插件)
 */
@SuppressWarnings("Do not inherit this class,it's just used for framework.")
public abstract class SubPlugin extends JavaPlugin {

    @Getter
    private String name;
    @Getter
    private String version;
    @Getter
    private String author;
    @Getter
    private Calendar lastUpdate;

    public SubPlugin(String name,String version,String author,Calendar lastUpdate){
        this.name = name;
        this.version = version;
        this.author = author;
        this.lastUpdate = lastUpdate;
    }
    /**
     * 1.18.2 MockBukkit Entrance Point
     */
    protected SubPlugin(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file){
        super(loader,desc,dataFolder,file);
        name = "Framework-Mock";
        version = "TEST";
        author = "TEST";
        lastUpdate = Calendar.getInstance();
    }

    /**
     * 初始化方法，向 Framework 容器注册之类的。
     * 生命周期在 onEnable 之前，在完成类成员属性初始化之后。
     */
    protected abstract void init();
}
