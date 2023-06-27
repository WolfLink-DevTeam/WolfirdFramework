package org.wolflink.minecraft.wolfird.framework.container;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.wolflink.common.ioc.Singleton;
import org.wolflink.minecraft.wolfird.framework.WolfirdPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class WolfirdPluginContainer {

    /**
     * 容器管理的子插件对象引用列表
     * 以插件名作为索引(不是全名，便于Bukkit查找)
     */
    private final Map<String, WolfirdPlugin> pluginItems = new HashMap<>();

    public boolean registerPlugin(@Nonnull String name, @Nonnull WolfirdPlugin instance) {
        if (pluginItems.containsKey(name)) return false;
        pluginItems.put(name, instance);
        return true;
    }

    public boolean unregisterPlugin(@Nonnull String name, @Nonnull WolfirdPlugin instance) {
        if (pluginItems.containsKey(name) && pluginItems.containsValue(instance)) {
            pluginItems.remove(name);
            return true;
        } else return false;
    }

    @Nullable
    public WolfirdPlugin getPlugin(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null || !plugin.isEnabled()) return null;
        return pluginItems.get(name);
    }

}
