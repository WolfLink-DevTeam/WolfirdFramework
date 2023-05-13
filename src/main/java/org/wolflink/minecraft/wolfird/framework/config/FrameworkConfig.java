package org.wolflink.minecraft.wolfird.framework.config;

import com.google.inject.Singleton;
import org.wolflink.minecraft.wolfird.framework.mongo.DocumentRepository;
import org.wolflink.minecraft.wolfird.framework.mongo.WolfDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置文档 基于 MongoDB
 * 所有子插件都可以使用
 */
@Singleton
public class FrameworkConfig {
    /**
     * 文档仓库
     */
    private final DocumentRepository documentRepo;
    /**
     * 存放默认配置文档
     */
    private final Map<PathProjection,Object> defaultConfigs = new HashMap<>();
    /**
     * 存放运行时配置文档
     */
    private final Map<PathProjection,Object> runtimeConfigs = new HashMap<>();

    /**
     * 绑定默认配置
     */
    private void r(PathProjection path, Object value) {
        defaultConfigs.put(path,value);
    }
    public FrameworkConfig(){
        documentRepo = new DocumentRepository("wolfird_config");

        initDefaultMap();
        load();
    }
    private void initDefaultMap() {
        r(PathProjection.MONGO_URL,"mongodb://localhost:27017/");
        r(PathProjection.MONGO_DB_NAME,"wolfird_db");
        r(PathProjection.NOTIFIER_CONSOLE_TEMPLATE,"§8[{prefix}§7|{level}§8] §r{msg}");
        r(PathProjection.NOTIFIER_CHAT_TEMPLATE,"§8[ {prefix} §8] §f›§7›§8› §r{msg}");
        r(PathProjection.NOTIFIER_NOTIFY_TEMPLATE,"\n§8[ {prefix} §8] §f›§7›§8› \n\n§r{msg}\n\n");
    }

    /**
     * 加载运行时配置
     */
    public void load(){
        for (Map.Entry<PathProjection,Object> entry : defaultConfigs.entrySet()) {
            runtimeConfigs.put(
                    entry.getKey(),
                    documentRepo.getValue(
                            entry.getKey().getDocumentName(),
                            entry.getKey().getPath(),
                            entry.getValue()
                    )
            );
        }
    }

    /**
     * 保存运行时配置
     */
    public void save(){
        for (Map.Entry<PathProjection,Object> entry : runtimeConfigs.entrySet()) {
            documentRepo.updateValue(
                    entry.getKey().getDocumentName(),
                    entry.getKey().getPath(),
                    entry.getValue()
            );
        }
    }
}
