package org.wolflink.minecraft.wolfird.framework;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import java.io.File;
import java.util.Calendar;

public final class WolfirdFramework extends SubPlugin {

    /**
     * 在 init 方法初始化其中所有实例引用
     */
    public static class InstanceHolder{

        public static WolfirdFramework PLUGIN;
        public static ApplicationContext SPRING;

    }

    public WolfirdFramework() {
        super("Framework", "1.0.0", "MikkoAyaka", Calendar.getInstance());
        super.getLastUpdate().set(2023, Calendar.MAY,9,20,34);
        init();
    }

    protected WolfirdFramework(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
        init();
    }

    @Override
    protected void init() {
        InstanceHolder.PLUGIN = this;
        InstanceHolder.SPRING = SpringApplication.run(WolfirdFramework.class);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
