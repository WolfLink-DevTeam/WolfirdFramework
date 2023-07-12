package org.wolflink.minecraft.wolfird.framework;


import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.SubScheduler;
import org.wolflink.minecraft.wolfird.framework.container.*;
import org.wolflink.common.ioc.IOC;
import org.wolflink.minecraft.wolfird.framework.notifier.SubPluginNotifier;

import java.util.HashSet;
import java.util.Set;

/**
 * 代表一个 Wolfird 子插件
 */
public abstract class WolfirdPlugin extends JavaPlugin {

    @Getter
    private final SubScheduler subScheduler;
    /**
     * 插件描述信息，详见 plugin.yml
     */
    protected final @Getter PluginDescriptionFile info;

    protected final @Getter SubPluginNotifier notifier;

    protected final @Getter WolfirdPluginContainer container;

    public WolfirdPlugin() {
        this.info = getDescription();
        if(info.getPrefix() == null)this.notifier = new SubPluginNotifier("§f"+this.getClass().getSimpleName());
        else this.notifier = new SubPluginNotifier(info.getPrefix());
        this.container = IOC.getBean(WolfirdPluginContainer.class);
        this.subScheduler = new SubScheduler();
    }
    private final Set<WolfirdCommand> commandSet = new HashSet<>();
    public final void bindCommand(WolfirdCommand command) {
        commandSet.add(command);
        registerCommand(command);
    }
    /**
     * 向框架中注册指令
     */
    private void registerCommand(WolfirdCommand command) {
        IOC.getBean(CommandContainer.class).registerCommand(command);
    }

    /**
     * 向框架中取消注册指令
     */
    private void unregisterCommand(WolfirdCommand command) {
        IOC.getBean(CommandContainer.class).registerCommand(command);
    }
    private void unregisterBindCommands() {
        commandSet.forEach(this::unregisterCommand);
    }

    /**
     * 初始化方法，向 Framework 容器注册该子插件
     */
    @Override
    public final void onEnable() {
        notifier.info("正在向框架注册该系统插件...");
        if (container.registerPlugin(info.getName(), this)) {
            notifier.info("注册完成");
        } else notifier.error("注册失败");
        afterEnabled();
    }
    public abstract void afterEnabled();
    public abstract void beforeDisabled();
    /**
     * 注销方法，向框架容器注销该子插件
     */
    @Override
    public final void onDisable() {
        beforeDisabled();
        notifier.info("正在向框架注销该系统插件...");
        unregisterBindCommands();
        subScheduler.cancelAllTasks();
        if (container.unregisterPlugin(info.getName(), this)) {
            notifier.info("注销完成");
        } else notifier.error("注销失败");
    }
}
