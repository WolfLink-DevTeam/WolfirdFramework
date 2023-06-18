package org.wolflink.minecraft.wolfird.framework.config;


import org.wolflink.minecraft.wolfird.framework.Framework;
import org.wolflink.minecraft.wolfird.framework.ioc.Singleton;

@Singleton
public class FrameworkConfig extends YamlConfig {
    public FrameworkConfig() {
        super(Framework.getInstance().getDescription().getName());
    }
}
