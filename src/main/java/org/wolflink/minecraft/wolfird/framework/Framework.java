package org.wolflink.minecraft.wolfird.framework;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.wolflink.minecraft.wolfird.framework.jpa.CommonPlayerData;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import java.io.File;
import java.util.UUID;

/**
 * 框架 Bukkit 插件主类
 */
public final class Framework extends SubPlugin {

    @Getter private static Framework INSTANCE;
    private final BaseNotifier logger = Guice.getBean(BaseNotifier.class);
    public Framework() {
        init();
    }
    private long initTime;
    @Override protected void init() {
        logger.info("开始初始化");
        initTime = System.currentTimeMillis();
        INSTANCE = this;
        this.saveDefaultConfig();
    }
    @Override public void onEnable() {
        logger.info("正在加载可用模式插件...");
        loadSubPlugins("mode-plugin");
        logger.info("正在加载可用系统插件...");
        loadSubPlugins("system-plugin");
        logger.info("正在加载可用拓展插件...");
        loadSubPlugins("addon-plugin");
        logger.info("§f初始化完成，用时 §a"+(System.currentTimeMillis()-initTime)/1000.0+" §f秒");

    }
    @Override public void onDisable() {
        logger.info("开始卸载框架");
    }

    private void loadSubPlugins(String subPluginFolderName){
        File subPluginFolder = new File(this.getDataFolder().getPath(),subPluginFolderName);
        if(!subPluginFolder.exists())subPluginFolder.mkdirs();
        Bukkit.getPluginManager().loadPlugins(subPluginFolder);
    }
}
