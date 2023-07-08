package org.wolflink.minecraft.wolfird.framework.config;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.wolflink.minecraft.wolfird.framework.Framework;

import javax.annotation.Nullable;
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
    @Nullable
    public <T> T get(String path) {
        try {
            Object result = fileConfiguration.get(path);
            if(result == null) return null;
            return (T) result;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "在进行类型转换时出现异常，相关信息：" + path);
            return null;
        }
    }

    /**
     * 以 Json 格式读取并转为对应类型
     */
    @Nullable
    public <T> T get(String path,Class<T> tClass) {
        String str = get(path);
        if(str == null)return null;
        if(hasJsonAnnotation(tClass)) {
            try {
                return new Gson().fromJson(str,tClass);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                Bukkit.getLogger().log(Level.SEVERE, "在进行JSON解序列化时出现异常，相关信息：" + path);
                return null;
            }
        } else return null;
    }
    private boolean hasJsonAnnotation(Class<?> clazz) {
        try {
            return clazz.getAnnotation(Json.class) != null;
        } catch (Exception e) { return false; }
    }

    @Override
    public void update(String path, Object value) {
        if(hasJsonAnnotation(value.getClass())) {
            fileConfiguration.set(path,new Gson().toJson(value));
        } else fileConfiguration.set(path,value);
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
            fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
            configFile.createNewFile();
            defaultConfig.forEach(this::update);
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Framework.getInstance().getNotifier().error("在尝试保存配置文件 "+configFile.getName()+" 时出现了问题。");
        }
    }
}
