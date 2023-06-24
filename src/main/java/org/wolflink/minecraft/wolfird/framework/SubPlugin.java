package org.wolflink.minecraft.wolfird.framework;


import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.container.*;
import org.wolflink.common.ioc.IOC;
import org.wolflink.minecraft.wolfird.framework.notifier.SubPluginNotifier;

/**
 * 代表一个 Wolfird 子插件
 */
public abstract class SubPlugin extends JavaPlugin {
    /**
     * 插件描述信息，详见 plugin.yml
     */
    protected final @Getter PluginDescriptionFile info;

    protected final @Getter SubPluginNotifier notifier;

    protected final @Getter SubPluginContainer container;

    public SubPlugin() {
        this.info = getDescription();
        this.notifier = new SubPluginNotifier(info.getPrefix());
        this.container = IOC.getBean(SubPluginContainer.class);
    }

    // TODO 应该再弄个列表存储某个子插件注册的所有指令，在插件卸载时把那些指令全部注销

    /**
     * 向框架中注册指令
     */
    public void registerCommand(WolfirdCommand command) {
        IOC.getBean(CommandContainer.class).registerCommand(command);
    }

    /**
     * 向框架中取消注册指令
     */
    public void unregisterCommand(WolfirdCommand command) {
        IOC.getBean(CommandContainer.class).registerCommand(command);
    }

    /**
     * 初始化方法，向 Framework 容器注册该子插件
     */
    @Override
    public void onEnable() {
        notifier.info("正在向框架注册该系统插件...");
        if (container.registerSubPlugin(info.getName(), this)) {
            notifier.info("注册完成");
        } else notifier.error("注册失败");
    }

    /**
     * 注销方法，向框架容器注销该子插件
     */
    @Override
    public void onDisable() {
        notifier.info("正在向框架注销该系统插件...");
        if (container.unregisterSubPlugin(info.getName(), this)) {
            notifier.info("注销完成");
        } else notifier.error("注销失败");
    }
}
