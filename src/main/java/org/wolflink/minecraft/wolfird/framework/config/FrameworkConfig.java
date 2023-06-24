package org.wolflink.minecraft.wolfird.framework.config;


import org.wolflink.common.ioc.Singleton;
import org.wolflink.minecraft.wolfird.framework.Framework;

@Singleton
public class FrameworkConfig extends YamlConfig {
    public FrameworkConfig() {
        super(Framework.getInstance().getDescription().getName());
    }
}
