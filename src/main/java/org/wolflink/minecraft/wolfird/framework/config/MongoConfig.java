package org.wolflink.minecraft.wolfird.framework.config;

import org.bukkit.Bukkit;
import org.wolflink.minecraft.wolfird.framework.database.mongo.MongoDocumentRepository;
import org.wolflink.minecraft.wolfird.framework.utils.TimingUtil;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * 基于 MongoDB 的配置文件(并不通用)
 * 不要使用 Notifier，因为配置数据的加载顺序在Notifier之前，实例化Notifier又依赖配置数据
 */
public class MongoConfig extends BaseConfig {
    /**
     * 文档仓库
     */
    private final MongoDocumentRepository documentRepo;

    public MongoConfig(String table,Map<String,Object> defaultConfig) {
        super(table,defaultConfig);
        documentRepo = new MongoDocumentRepository(table);
    }

    /**
     * 获取运行时配置
     */
    @Override
    public <T> T get(String path) {
        try {
            return (T) runtimeConfigs.get(path);
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "在进行类型转换时出现异常，相关路径：" + path);
            return null;
        }
    }

    /**
     * 修改运行时配置
     */
    @Override
    public void update(String path,Object value) {
        runtimeConfigs.put(path,value);
    }

    /**
     * 加载运行时配置
     */
    @Override
    public void load() {
        Bukkit.getLogger().info("正在从 MongoDB 中加载配置文件...");
        UUID uuid = UUID.randomUUID();
        TimingUtil.start(uuid.toString());
        for (Map.Entry<String,Object> entry : defaultConfig.entrySet()) {
            runtimeConfigs.put(
                    entry.getKey(),
                    documentRepo.getValue(
                            configName,
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }
        Bukkit.getLogger().info("配置文件加载完成，用时 " + TimingUtil.finish(uuid.toString()) / 1000.0 + " 秒");
    }

    /**
     * 保存运行时配置
     */
    @Override
    public void save() {
        Bukkit.getLogger().info("正在向 MongoDB 中保存配置文件...");
        UUID uuid = UUID.randomUUID();
        TimingUtil.start(uuid.toString());
        for (Map.Entry<String, Object> entry : runtimeConfigs.entrySet()) {
            documentRepo.updateValue(
                    configName,
                    entry.getKey(),
                    entry.getValue()
            );
        }
        Bukkit.getLogger().info("配置文件保存完成，用时 " + TimingUtil.finish(uuid.toString()) / 1000.0 + " 秒");
    }

    @Override
    public void initDefault() {

    }
}
