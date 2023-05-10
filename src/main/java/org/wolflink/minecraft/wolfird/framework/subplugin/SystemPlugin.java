package org.wolflink.minecraft.wolfird.framework.subplugin;

import lombok.NoArgsConstructor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.wolflink.minecraft.wolfird.framework.ioc.SystemContainer;

import java.io.File;

/**
 * 大型玩法系统拓展插件
 * 例如：天赋树、商店系统、数据统计...
 */
@NoArgsConstructor
public abstract class SystemPlugin extends SubPlugin {
    protected SystemPlugin(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
    }
    @Override
    protected void init(){
        SystemContainer container = getContainer();
        container.pluginItems.put(info.getFullName(),this);
    }
}
