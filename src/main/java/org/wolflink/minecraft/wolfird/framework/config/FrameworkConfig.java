package org.wolflink.minecraft.wolfird.framework.config;

import com.google.inject.Singleton;
import org.wolflink.minecraft.wolfird.framework.mongo.DocumentRepository;

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
     * 存放运行时配置文档
     */
    private final Map<ConfigProjection,Object> runtimeConfigs = new HashMap<>();

    public FrameworkConfig(){
        documentRepo = new DocumentRepository("wolfird_config");
    }
    /**
     * 加载运行时配置
     */
    public void load(){
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
    }

    /**
     * 保存运行时配置
     */
    public void save(){
        for (Map.Entry<ConfigProjection,Object> entry : runtimeConfigs.entrySet()) {
            documentRepo.updateValue(
                    entry.getKey().getDocumentName(),
                    entry.getKey().getPath(),
                    entry.getValue()
            );
        }
    }
}
