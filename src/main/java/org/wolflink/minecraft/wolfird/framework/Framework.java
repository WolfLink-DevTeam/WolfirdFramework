package org.wolflink.minecraft.wolfird.framework;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.wolflink.common.ioc.IOC;
import org.wolflink.minecraft.wolfird.framework.config.ConfigProjection;
import org.wolflink.minecraft.wolfird.framework.command.WolfirdCommandExecutor;
import org.wolflink.minecraft.wolfird.framework.command.WolfirdTabCompleter;
import org.wolflink.minecraft.wolfird.framework.config.FrameworkConfig;
import org.wolflink.minecraft.wolfird.framework.container.CommandContainer;
import org.wolflink.minecraft.wolfird.framework.notifier.FrameworkNotifier;
import org.wolflink.minecraft.wolfird.framework.utils.TimingUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 框架 Bukkit 插件主类
 */
public final class Framework extends JavaPlugin {

    @Getter
    private static Framework instance;
    private final @Getter FrameworkNotifier notifier;
    private final File bannerFile;

    public Framework() {
        instance = this;
        // 首先加载配置文件
        IOC.getBean(FrameworkConfig.class).load();
        notifier = IOC.getBean(FrameworkNotifier.class);
        bannerFile = new File(getDataFolder(),"banner.txt");
    }
    @Override
    public void onEnable() {
        showBanner();
        notifier.info("开始初始化");
        TimingUtil.start("framework_init");
//        this.saveDefaultConfig();
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
    private void createBannerFile() {
        try {
            bannerFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(bannerFile);
            fos.write("""


                ██╗    ██╗ ██████╗ ██╗     ███████╗██╗██████╗ ██████╗
                ██║    ██║██╔═══██╗██║     ██╔════╝██║██╔══██╗██╔══██╗  [ Author ] %author%
                ██║ █╗ ██║██║   ██║██║     █████╗  ██║██████╔╝██║  ██║
                ██║███╗██║██║   ██║██║     ██╔══╝  ██║██╔══██╗██║  ██║  [ Version ] %version%
                ╚███╔███╔╝╚██████╔╝███████╗██║     ██║██║  ██║██████╔╝
                 ╚══╝╚══╝  ╚═════╝ ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═════╝

                """.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            notifier.error("在创建 banner.txt 文件时遇到了问题，请查看上方详细错误信息。");
        }
    }
    private void showBanner() {
        if(!bannerFile.exists()) {
            createBannerFile();
        }
        List<String> bannerText = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(bannerFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            for (String line = br.readLine();line != null;line = br.readLine()) {
                if(line.contains("%")) {
                    line = line.replaceAll("%author%","WolfLink - DevTeam");
                    line = line.replaceAll("%version%", getDescription().getVersion());
                }
                bannerText.add(line);
            }
            br.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            notifier.error("在读取 banner.txt 文件时遇到了问题，请查看上方详细错误信息。");
        }
        bannerText.forEach(notifier::custom);
    }
}