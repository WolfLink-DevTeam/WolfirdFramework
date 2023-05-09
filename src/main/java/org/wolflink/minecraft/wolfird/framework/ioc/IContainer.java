package org.wolflink.minecraft.wolfird.framework.ioc;

import org.wolflink.minecraft.wolfird.framework.subplugin.SubPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class IContainer<T extends SubPlugin> {

    /**
     * 容器管理的子插件对象引用列表
     */
    private final List<T> pluginItems = new ArrayList<>();

    /**
     * 子插件的主类路径
     */
    private final List<String> pluginPackages = new ArrayList<>();

}
