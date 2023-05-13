package org.wolflink.minecraft.wolfird.framework.config;

import lombok.Getter;

public enum PathProjection {
    MONGO_URL("mongo.connectionUrl"),
    MONGO_DB_NAME("mongo.databaseName"),
    NOTIFIER_PREFIX("notifier.prefix"),
    NOTIFIER_CONSOLE_TEMPLATE("notifier.template.console"),
    NOTIFIER_CHAT_TEMPLATE("notifier.template.chat"),
    NOTIFIER_NOTIFY_TEMPLATE("notifier.template.notify"),

    ;
    @Getter
    private final String documentName;
    @Getter
    private final String path;

    PathProjection(String path) {
        this(DocumentProjection.FRAMEWORK,path);
    }
    PathProjection(DocumentProjection documentProjection,String path) {
        this(documentProjection.name().toLowerCase(),path);
    }
    PathProjection(String documentName, String path) {
        this.documentName = documentName;
        this.path = path;
    }
}
