package org.wolflink.minecraft.wolfird.framework.config;


import org.wolflink.common.ioc.Singleton;
import org.wolflink.minecraft.wolfird.framework.Framework;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class FrameworkConfig extends YamlConfig {
    private static final Map<String,Object> cacheConfig = new HashMap<>();
    static {
        for (ConfigProjection configProjection : ConfigProjection.values()) {
            cacheConfig.put(configProjection.getPath(),configProjection.getDefaultValue());
        }
    }
    public FrameworkConfig() {
        super(Framework.getInstance().getDescription().getName(),cacheConfig);
    }
    public <T> T get(ConfigProjection configProjection) {
        return get(configProjection.getPath(),configProjection.getDefaultValue());
    }

    public void update(ConfigProjection configProjection, Object value) {
        update(configProjection.getPath(),value);
    }
}
