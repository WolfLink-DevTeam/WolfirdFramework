package org.wolflink.minecraft.wolfird.framework.config;

import lombok.Getter;

/**
 * 默认配置投影
 * 存储配置路径和默认配置值
 */
public enum ConfigProjection {
    MONGO_URL("mongo.connectionUrl","mongodb://localhost:27017/"),
    MONGO_DB_NAME("mongo.databaseName","wolfird_db"),
    NOTIFIER_COLORFUL_CONSOLE("notifier.colorful_console",true),
    NOTIFIER_CONSOLE_TEMPLATE("notifier.template.console","§8[{prefix}§7|{level}§8] §r{msg}"),
    NOTIFIER_CHAT_TEMPLATE("notifier.template.chat","§8[ {prefix} §8] §f›§7›§8› §r{msg}"),
    NOTIFIER_NOTIFY_TEMPLATE("notifier.template.notify","\n§8[ {prefix} §8] §f›§7›§8› \n\n§r{msg}\n\n"),

    ;
    @Getter
    private final String documentName;
    @Getter
    private final String path;
    @Getter
    private final Object defaultValue;

    ConfigProjection(String path,Object defaultValue) {
        this(DocumentProjection.FRAMEWORK,path,defaultValue);
    }
    ConfigProjection(DocumentProjection documentProjection, String path,Object defaultValue) {
        this(documentProjection.toString(),path,defaultValue);
    }
    ConfigProjection(String documentName, String path,Object defaultValue) {
        this.documentName = documentName;
        this.path = path;
        this.defaultValue = defaultValue;
    }
}
