package org.wolflink.minecraft.wolfird.framework.ioc;

import org.bukkit.Bukkit;
import org.wolflink.minecraft.wolfird.framework.Framework;
import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SubPluginContainer<T extends SubPlugin> {

    /**
     * 容器管理的子插件对象引用列表
     * 以插件全名(插件名+插件版本)作为索引
     */
    public final Map<String,T> pluginItems = new HashMap<>();

    /**
     * 启用子插件
     * @return 是否成功启用(否，已经被加载或发生错误)
     */
    public boolean enablePlugin(){
        return true;
    }

    /**
     * 禁用子插件
     * @return 是否成功禁用(否，已经被禁用或发生错误)
     */
    public boolean disablePlugin(){
        return true;
    }

}
