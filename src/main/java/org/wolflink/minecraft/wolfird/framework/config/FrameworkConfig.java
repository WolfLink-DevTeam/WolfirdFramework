package org.wolflink.minecraft.wolfird.framework.config;

import com.google.inject.Singleton;
import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.mongo.DocumentRepository;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;
import org.wolflink.minecraft.wolfird.framework.utils.TimingUtil;

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
        Guice.getBean(BaseNotifier.class).info("正在从 MongoDB 中加载配置文件...");
        TimingUtil.start("config_load");
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
        Guice.getBean(BaseNotifier.class).info("§f配置文件加载完成，用时 §a"+TimingUtil.finish("config_load")/1000.0+"§f 秒");
    }

    /**
     * 保存运行时配置
     */
    public void save(){
        Guice.getBean(BaseNotifier.class).info("正在向 MongoDB 中保存配置文件...");
        TimingUtil.start("config_save");
        for (Map.Entry<ConfigProjection,Object> entry : runtimeConfigs.entrySet()) {
            documentRepo.updateValue(
                    entry.getKey().getDocumentName(),
                    entry.getKey().getPath(),
                    entry.getValue()
            );
        }
        Guice.getBean(BaseNotifier.class).info("§f配置文件保存完成，用时 §a"+TimingUtil.finish("config_save")/1000.0+"§f 秒");
    }
}
