package org.wolflink.minecraft.wolfird.framework.subplugin;


import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.container.AddonContainer;
import org.wolflink.minecraft.wolfird.framework.container.ModeContainer;
import org.wolflink.minecraft.wolfird.framework.container.SubPluginContainer;
import org.wolflink.minecraft.wolfird.framework.container.SystemContainer;

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

    public <T extends SubPluginContainer<? extends SubPlugin>> T getContainer(){
        if(this instanceof AddonPlugin) return (T) Guice.getBean(AddonContainer.class);
        else if(this instanceof ModePlugin) return (T) Guice.getBean(ModeContainer.class);
        else if(this instanceof SystemPlugin) return (T) Guice.getBean(SystemContainer.class);
        return null;
    }

    /**
     * 初始化方法，向 Framework 容器注册之类的。
     * 该方法的调用顺序应该在 onEnable 之前。
     */
    protected abstract void init();
}
