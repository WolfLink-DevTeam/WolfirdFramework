package org.wolflink.minecraft.wolfird.framework.config;

import org.bukkit.Bukkit;
import org.wolflink.minecraft.wolfird.framework.mongo.DocumentRepository;
import org.wolflink.minecraft.wolfird.framework.utils.TimingUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * 通用配置文件类
 * 不要使用 Notifier，因为配置数据的加载顺序在Notifier之前，实例化Notifier又依赖配置数据
 */
public abstract class BaseConfig {
    /**
     * 文档仓库
     */
    private final DocumentRepository documentRepo;
    /**
     * 存放运行时配置文档
     */
    private final Map<ConfigProjection,Object> runtimeConfigs = new HashMap<>();
    public BaseConfig(String table){
        documentRepo = new DocumentRepository(table);
    }
    /**
     * 获取运行时配置
     */
    public <T> T get(ConfigProjection configProjection) {
        try {
            T result = (T) runtimeConfigs.get(configProjection);
            return result;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE,"在进行类型转换时出现异常，相关信息："+configProjection.getPath());
            return null;
        }
    }
    /**
     * 修改运行时配置
     */
    public void update(ConfigProjection configProjection,Object value) {
        runtimeConfigs.put(configProjection,value);
    }
    /**
     * 加载运行时配置
     */
    public void load(){
        Bukkit.getLogger().info("正在从 MongoDB 中加载配置文件...");
        UUID uuid = UUID.randomUUID();
        TimingUtil.start(uuid.toString());
        for (ConfigProjection configNode : ConfigProjection.values()) {
            runtimeConfigs.put(
                    configNode,
                    documentRepo.getValue(
                            configNode.getDocumentName(),
                            configNode.getPath(),
                            configNode.getDefaultValue()
                    )
            );
        }
        Bukkit.getLogger().info("配置文件加载完成，用时 "+TimingUtil.finish(uuid.toString())/1000.0+" 秒");
    }
    /**
     * 保存运行时配置
     */
    public void save(){
        Bukkit.getLogger().info("正在向 MongoDB 中保存配置文件...");
        UUID uuid = UUID.randomUUID();
        TimingUtil.start(uuid.toString());
        for (Map.Entry<ConfigProjection,Object> entry : runtimeConfigs.entrySet()) {
            documentRepo.updateValue(
                    entry.getKey().getDocumentName(),
                    entry.getKey().getPath(),
                    entry.getValue()
            );
        }
        Bukkit.getLogger().info("配置文件保存完成，用时 "+TimingUtil.finish(uuid.toString())/1000.0+" 秒");
    }
}