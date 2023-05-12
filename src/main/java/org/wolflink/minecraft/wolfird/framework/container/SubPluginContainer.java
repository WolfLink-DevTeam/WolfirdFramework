package org.wolflink.minecraft.wolfird.framework.container;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class SubPluginContainer<T extends SubPlugin> {

    /**
     * 容器管理的子插件对象引用列表
     * 以插件名作为索引(不是全名，便于Bukkit查找)
     */
    private final Map<String,T> pluginItems = new HashMap<>();

    public boolean registerSubPlugin(@Nonnull String name,@Nonnull T instance) {
        if(pluginItems.containsKey(name))return false;
        pluginItems.put(name,instance);
        return true;
    }
    public boolean unregisterSubPlugin(@Nonnull String name,@Nonnull T instance) {
        if(pluginItems.containsKey(name) && pluginItems.containsValue(instance)) {
            pluginItems.remove(name);
            return true;
        } else return false;
    }

    @Nullable
    public T getSubPlugin(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if(plugin == null || ! plugin.isEnabled())return null;
        return pluginItems.get(name);
    }

}
