package org.wolflink.minecraft.wolfird.framework.config;

import com.google.inject.Singleton;

/**
 * 配置文档 基于 MongoDB
 * 所有子插件都可以使用
 */
@Singleton
public class FrameworkConfig extends BaseConfig{
    public FrameworkConfig(){
         super("wolfird_config");
    }
}
