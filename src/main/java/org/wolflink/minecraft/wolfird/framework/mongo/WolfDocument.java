package org.wolflink.minecraft.wolfird.framework.mongo;

import com.mongodb.lang.Nullable;
import org.bson.Document;

/**
 * 封装文档类
 * 提供了根据路径插入值和根据路径查询值的方法
 * 在与MongoDB互相操作时需要调用 toDocument() 方法转换为 Document 类型
 */
public class WolfDocument {

    private Document thisDoc = new Document();

    public Document toDocument() {
        return thisDoc;
    }

    public WolfDocument(Document document) {
        if (document == null) throw new IllegalArgumentException("Can't create WolfDocument because Document is null");
        thisDoc = document;
    }

    public WolfDocument(String name) {
        thisDoc.put("documentName", name);
    }

    public String getName() {
        return thisDoc.getString("documentName");
    }

    /**
     * 根据路径设置值，例如：
     * server.ssh.port
     * server.ssh.enabled
     */
    @Nullable
    public Object putByPath(final String path, final Object value) {
        String[] pathArgs = path.split("\\.");
        String lastArg = pathArgs[pathArgs.length - 1];
        Document nowDocument = thisDoc;
        for (int i = 0; i < pathArgs.length - 1; i++) {
            String arg = pathArgs[i];
            Document doc = nowDocument.get(arg, Document.class);
            if (doc == null) {
                doc = new Document();
                nowDocument.put(arg, doc);
            }
            nowDocument = doc;
        }
        return nowDocument.put(lastArg, value);
    }

    /**
     * 根据给定路径查询对应值
     * 如果路径不存在则返回null
     */
    @Nullable
    public Object getByPath(final String path) {
        String[] pathArgs = path.split("\\.");
        String lastArg = pathArgs[pathArgs.length - 1];
        Document nowDocument = thisDoc;
        for (int i = 0; i < pathArgs.length - 1; i++) {
            String arg = pathArgs[i];
            Document doc = nowDocument.get(arg, Document.class);
            if (doc == null) {
                return null;
            }
            nowDocument = doc;
        }
        return nowDocument.get(lastArg);
    }
}