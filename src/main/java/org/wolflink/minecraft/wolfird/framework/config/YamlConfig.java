package org.wolflink.minecraft.wolfird.framework.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.wolflink.minecraft.wolfird.framework.Framework;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * 基于 Yaml 的配置文件，子插件声明的配置文件都会被整理到外层
 */
public class YamlConfig extends BaseConfig {

    private static final File frameworkCfgFolder = new File(Framework.getInstance().getDataFolder(),"config");
    private final File configFile;
    FileConfiguration fileConfiguration;

    public YamlConfig(String configName) {
        super(configName);
        if(!frameworkCfgFolder.exists())frameworkCfgFolder.mkdirs();
        configFile = new File(frameworkCfgFolder,configName+".yml");
        try {
            if(!configFile.exists())configFile.createNewFile();
        } catch (IOException e) {
            Framework.getInstance().getNotifier().error("在尝试保存配置文件 "+configFile.getName()+" 时出现了问题。");
        }

    }

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
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void save() {
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            Framework.getInstance().getNotifier()
                    .error("在尝试保存配置文件 "+configName+" 时出现了异常，请查看上方详细错误信息。");
        }

    }
}
