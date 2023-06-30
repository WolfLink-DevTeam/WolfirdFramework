package org.wolflink.minecraft.wolfird.framework.config;

import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Service层通用配置文件类
 */
public abstract class BaseConfig {

    /**
     * 配置文件名，如 framework_cfg
     */
    protected final String configName;
    protected final Map<String,Object> defaultConfig;

    public BaseConfig(String configName, Map<String,Object> defaultConfig) {
        this.configName = configName;
        this.defaultConfig = defaultConfig;
    }

    /**
     * 存放运行时配置文档
     */
    protected final Map<String, Object> runtimeConfigs = new HashMap<>();
    /**
     * 获取运行时配置
     */
    @Nullable
    public abstract <T> T get(String path);
    public <T> T get(String path, Object value) {
        try {
            T result = get(path);
            if(result == null) {
                update(path, value);
                return (T) value;
            } else return result;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "在进行类型转换时出现异常，相关信息：" + path);
            return null;
        }
    }
    /**
     * 修改运行时配置
     */
    public abstract void update(String path, Object value);

    /**
     * 从数据库中加载配置文件到运行时数据区
     */
    public abstract void load();

    /**
     * 将运行时数据区的数据保存到数据库中
     */
    public abstract void save();

    /**
     * 初始化默认的配置文件
     * 会将默认配置同步到运行时数据区中
     * 同时保存配置文件到硬盘
     */
    public abstract void initDefault();
}
