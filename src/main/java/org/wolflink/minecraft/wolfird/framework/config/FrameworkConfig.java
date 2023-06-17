package org.wolflink.minecraft.wolfird.framework.config;


import org.wolflink.minecraft.wolfird.framework.ioc.Singleton;

@Singleton
public class FrameworkConfig extends MongoConfig {
    public FrameworkConfig() {
        super("wolfird_config");
    }
}
