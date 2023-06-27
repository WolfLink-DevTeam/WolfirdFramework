package org.wolflink.minecraft.wolfird.framework.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.wolflink.minecraft.wolfird.framework.Framework;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

/**
 * 基于 Yaml 的配置文件，子插件声明的配置文件都会被整理到外层
 */
public class YamlConfig extends BaseConfig {

    private static final File frameworkCfgFolder = new File(Framework.getInstance().getDataFolder(),"config");
    private final File configFile;
    FileConfiguration fileConfiguration;

    public YamlConfig(String configName, Map<String,Object> defaultConfig) {
        super(configName,defaultConfig);
        if(!frameworkCfgFolder.exists())frameworkCfgFolder.mkdirs();
        configFile = new File(frameworkCfgFolder,configName+".yml");
        if(!configFile.exists())initDefault();
    }

    @Override
    public <T> T get(String path, Object value) {
        try {
            Object result = fileConfiguration.get(path);
            if(result == null) {
                update(path, value);
                return (T) value;
            }
            return (T) result;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "在进行类型转换时出现异常，相关信息：" + path);
            return null;
        }
    }

    @Override
    public void update(String path, Object value) {
        fileConfiguration.set(path,value);
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

    @Override
    public void initDefault() {
        try {
            load();
            configFile.createNewFile();
            defaultConfig.forEach(this::update);
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Framework.getInstance().getNotifier().error("在尝试保存配置文件 "+configFile.getName()+" 时出现了问题。");
        }
    }
}
