package org.wolflink.minecraft.wolfird.framework.subplugin;


import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.MongoDB;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.container.*;
import org.wolflink.minecraft.wolfird.framework.notifier.SubPluginNotifier;

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

    protected final @Getter SubPluginNotifier notifier;

    public SubPlugin(){
        this.info = this.getDescription();
        this.notifier = new SubPluginNotifier(info.getPrefix());
    }

    public <T extends SubPluginContainer<? extends SubPlugin>> T getContainer(){
        if(this instanceof AddonPlugin) return (T) Guice.getBean(AddonContainer.class);
        else if(this instanceof ModePlugin) return (T) Guice.getBean(ModeContainer.class);
        else if(this instanceof SystemPlugin) return (T) Guice.getBean(SystemContainer.class);
        return null;
    }

    // TODO 应该再弄个列表存储某个子插件注册的所有指令，在插件卸载时把那些指令全部注销
    /**
     * 向框架中注册指令
     */
    public void registerCommand(WolfirdCommand command) {
        Guice.getBean(CommandContainer.class).registerCommand(command);
    }
    /**
     * 向框架中取消注册指令
     */
    public void unregisterCommand(WolfirdCommand command) {
        Guice.getBean(CommandContainer.class).registerCommand(command);
    }

    /**
     * 初始化方法，向 Framework 容器注册之类的。
     * 该方法的调用顺序应该在 onEnable 之前。
     */
    protected abstract void init();
    /**
     * 注销方法，向框架容器注销插件
     * 需要在 onDisable 之前调用
     */
    protected abstract void beforeDisable();
}
