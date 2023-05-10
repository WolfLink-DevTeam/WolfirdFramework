package org.wolflink.minecraft.wolfird.framework.subplugin;


import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.wolflink.minecraft.wolfird.framework.ioc.AddonContainer;
import org.wolflink.minecraft.wolfird.framework.ioc.ModeContainer;
import org.wolflink.minecraft.wolfird.framework.ioc.SubPluginContainer;
import org.wolflink.minecraft.wolfird.framework.ioc.SystemContainer;

import java.io.File;

/**
 * 代表一个 Wolfird 子插件抽象
 * 可能是：Mode(模式插件)、Addon(功能拓展插件)、System(玩法系统插件)
 */
@SuppressWarnings("Do not inherit this class,it's just used for framework.")
public abstract class SubPlugin extends JavaPlugin {

    /**
     * 插件描述信息，详见 plugin.yml
     */
    protected final @Getter PluginDescriptionFile info;

    public SubPlugin(){
        this.info = this.getDescription();
    }
    /**
     * 1.18.2 MockBukkit Entrance Point
     */
    protected SubPlugin(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file){
        super(loader,desc,dataFolder,file);
        this.info = this.getDescription();
    }
    public <T extends SubPluginContainer<? extends SubPlugin>> T getContainer(){
        if(this instanceof AddonPlugin) return (T) Spring.INSTANCE.getBean("addonContainer", AddonContainer.class);
        else if(this instanceof ModePlugin) return (T) Spring.INSTANCE.getBean("modeContainer", ModeContainer.class);
        else if(this instanceof SystemPlugin) return (T) Spring.INSTANCE.getBean("systemContainer", SystemContainer.class);
        return null;
    }

    /**
     * 初始化方法，向 Framework 容器注册之类的。
     * 该方法的调用顺序应该在 onEnable 之前，在完成类成员属性初始化之后。
     */
    protected abstract void init();
}
