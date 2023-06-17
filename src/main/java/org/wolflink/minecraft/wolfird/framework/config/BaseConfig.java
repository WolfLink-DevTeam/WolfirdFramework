package org.wolflink.minecraft.wolfird.framework.config;

import java.util.EnumMap;

public abstract class BaseConfig {
    /**
     * 存放运行时配置文档
     */
    private final EnumMap<ConfigProjection, Object> runtimeConfigs = new EnumMap<>(ConfigProjection.class);

}
