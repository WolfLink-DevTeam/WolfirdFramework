package org.wolflink.minecraft.wolfird.framework.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.wolflink.minecraft.wolfird.framework.Framework;

import java.util.logging.Level;

public class YamlConfig extends BaseConfig {

    FileConfiguration fileConfiguration;
    @Override
    public <T> T get(ConfigProjection configProjection) {
        try {
            Object result = fileConfiguration.get(configProjection.getPath());
            if(result == null) {
                update(configProjection, configProjection.getDefaultValue());
                return (T) configProjection.getDefaultValue();
            }
            return (T) result;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "在进行类型转换时出现异常，相关信息：" + configProjection.getPath());
            return null;
        }
    }

    @Override
    public void update(ConfigProjection configProjection, Object value) {
        fileConfiguration.set(configProjection.getPath(),value);
    }

    @Override
    public void load() {
        fileConfiguration = Framework.getInstance().getConfig();
    }

    @Override
    public void save() {
        Framework.getInstance().saveConfig();
    }
}
