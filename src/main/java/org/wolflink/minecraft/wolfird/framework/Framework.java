package org.wolflink.minecraft.wolfird.framework;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.wolflink.minecraft.wolfird.framework.config.ConfigProjection;
import org.wolflink.minecraft.wolfird.framework.ioc.IOC;
import org.wolflink.minecraft.wolfird.framework.command.WolfirdCommandExecutor;
import org.wolflink.minecraft.wolfird.framework.command.WolfirdTabCompleter;
import org.wolflink.minecraft.wolfird.framework.config.FrameworkConfig;
import org.wolflink.minecraft.wolfird.framework.container.CommandContainer;
import org.wolflink.minecraft.wolfird.framework.notifier.FrameworkNotifier;
import org.wolflink.minecraft.wolfird.framework.utils.TimingUtil;

import java.io.File;
import java.util.Objects;

/**
 * 框架 Bukkit 插件主类
 */
public final class Framework extends JavaPlugin {

    @Getter
    private static Framework instance;
    /**
     * 插件描述信息，详见 plugin.yml
     */
    private final @Getter PluginDescriptionFile info;
    private final @Getter FrameworkNotifier notifier;

    public Framework() {
        instance = this;
        info = getInfo();
        // 首先加载配置文件
        IOC.getBean(FrameworkConfig.class).load();
        notifier = IOC.getBean(FrameworkNotifier.class);
    }

    private void showBanner() {
        //TODO：里面的版本信息得用字符串变量替换！
        notifier.custom("""



                ██╗    ██╗ ██████╗ ██╗     ███████╗██╗██████╗ ██████╗
                ██║    ██║██╔═══██╗██║     ██╔════╝██║██╔══██╗██╔══██╗  [ Author ] WolfLink-DevTeam
                ██║ █╗ ██║██║   ██║██║     █████╗  ██║██████╔╝██║  ██║
                ██║███╗██║██║   ██║██║     ██╔══╝  ██║██╔══██╗██║  ██║  [ Version ] 1.1.0 - SNAPSHOT
                ╚███╔███╔╝╚██████╔╝███████╗██║     ██║██║  ██║██████╔╝
                 ╚══╝╚══╝  ╚═════╝ ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═════╝

                """);
    }

    @Override
    public void onEnable() {
        showBanner();
        notifier.info("开始初始化");
        TimingUtil.start("framework_init");
        this.saveDefaultConfig();
        if((Boolean) ConfigProjection.MONGO_ENABLED.getDefaultValue()) IOC.getBean(MongoDB.class).setError(false);
        notifier.info("正在加载可用子插件...");
        loadSubPlugins();
        notifier.info("§f初始化完成，用时 §a" + TimingUtil.finish("framework_init") / 1000.0 + " §f秒");
        Objects.requireNonNull(Bukkit.getPluginCommand("wolfird")).setExecutor(IOC.getBean(WolfirdCommandExecutor.class));
        Objects.requireNonNull(Bukkit.getPluginCommand("wolfird")).setTabCompleter(IOC.getBean(WolfirdTabCompleter.class));
        IOC.getBean(CommandContainer.class).registerCommands();
    }

    @Override
    public void onDisable() {
        // 保存配置文件
        notifier.info("开始卸载框架");
        if((Boolean) ConfigProjection.MONGO_ENABLED.getDefaultValue()) {
            if (IOC.getBean(MongoDB.class).isError()) return;
            IOC.getBean(FrameworkConfig.class).save();
            IOC.getBean(MongoDB.class).close();
        }
        notifier.info("框架已被完全卸载");
    }

    private void loadSubPlugins() {
        File subPluginFolder = new File(this.getDataFolder().getPath(), "sub-plugin");
        if (!subPluginFolder.exists()) subPluginFolder.mkdirs();
        Bukkit.getPluginManager().loadPlugins(subPluginFolder);
    }
}