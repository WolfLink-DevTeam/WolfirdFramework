package org.wolflink.minecraft.wolfird.framework.config;

import java.util.EnumMap;

/**
 * Service层通用配置文件类
 */
public abstract class BaseConfig {
    /**
     * 存放运行时配置文档
     */
    protected final EnumMap<ConfigProjection, Object> runtimeConfigs = new EnumMap<>(ConfigProjection.class);
    /**
     * 获取运行时配置
     */
    public abstract <T> T get(ConfigProjection configProjection);
    /**
     * 修改运行时配置
     */
    public abstract void update(ConfigProjection configProjection, Object value);

    /**
     * 从数据库中加载配置文件到运行时数据区
     */
    public abstract void load();

    /**
     * 将运行时数据区的数据保存到数据库中
     */
    public abstract void save();
}
