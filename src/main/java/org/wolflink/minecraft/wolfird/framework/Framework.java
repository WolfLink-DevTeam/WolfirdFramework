package org.wolflink.minecraft.wolfird.framework;

import lombok.Getter;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.wolflink.minecraft.wolfird.framework.config.FrameworkConfig;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import java.io.File;

/**
 * 框架 Bukkit 插件主类
 */
public final class Framework extends SubPlugin {

    @Getter private static Framework INSTANCE;
    private final BaseNotifier logger = Guice.getBean(BaseNotifier.class);
    public Framework() {
        showBanner();
        init();
    }
    private long initTime;

    private void showBanner() {
        logger.custom("""



                ██╗    ██╗ ██████╗ ██╗     ███████╗██╗██████╗ ██████╗
                ██║    ██║██╔═══██╗██║     ██╔════╝██║██╔══██╗██╔══██╗  [ Author ] WolfLink-DevTeam
                ██║ █╗ ██║██║   ██║██║     █████╗  ██║██████╔╝██║  ██║
                ██║███╗██║██║   ██║██║     ██╔══╝  ██║██╔══██╗██║  ██║  [ Version ] 1.0.0 - SNAPSHOT
                ╚███╔███╔╝╚██████╔╝███████╗██║     ██║██║  ██║██████╔╝
                 ╚══╝╚══╝  ╚═════╝ ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═════╝

                """);
    }

    @Override protected void init() {
        logger.info("开始初始化");
        initTime = System.currentTimeMillis();
        INSTANCE = this;
        // 加载配置文件
        Guice.getBean(FrameworkConfig.class).load();
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
    @Override
    protected void beforeDisable() {
        // 保存配置文件
        Guice.getBean(FrameworkConfig.class).save();
    }
    @Override public void onDisable() {
        logger.info("开始卸载框架");
        Guice.getBean(MongoDB.class).close();
    }

    private void loadSubPlugins(String subPluginFolderName){
        File subPluginFolder = new File(this.getDataFolder().getPath(),subPluginFolderName);
        if(!subPluginFolder.exists())subPluginFolder.mkdirs();
        Bukkit.getPluginManager().loadPlugins(subPluginFolder);
    }
}
