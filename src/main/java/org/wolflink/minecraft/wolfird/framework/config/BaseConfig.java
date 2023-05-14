package org.wolflink.minecraft.wolfird.framework.config;

import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.mongo.DocumentRepository;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;
import org.wolflink.minecraft.wolfird.framework.utils.TimingUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 通用配置文件类
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
     * 修改运行时配置
     */
    public void update(ConfigProjection configProjection,Object value) {
        runtimeConfigs.put(configProjection,value);
    }
    /**
     * 加载运行时配置
     */
    public void load(){
        Guice.getBean(BaseNotifier.class).info("正在从 MongoDB 中加载配置文件...");
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
        Guice.getBean(BaseNotifier.class).info("§f配置文件加载完成，用时 §a"+TimingUtil.finish(uuid.toString())/1000.0+"§f 秒");
    }
    /**
     * 保存运行时配置
     */
    public void save(){
        Guice.getBean(BaseNotifier.class).info("正在向 MongoDB 中保存配置文件...");
        UUID uuid = UUID.randomUUID();
        TimingUtil.start(uuid.toString());
        for (Map.Entry<ConfigProjection,Object> entry : runtimeConfigs.entrySet()) {
            documentRepo.updateValue(
                    entry.getKey().getDocumentName(),
                    entry.getKey().getPath(),
                    entry.getValue()
            );
        }
        Guice.getBean(BaseNotifier.class).info("§f配置文件保存完成，用时 §a"+TimingUtil.finish(uuid.toString())/1000.0+"§f 秒");
    }
}
