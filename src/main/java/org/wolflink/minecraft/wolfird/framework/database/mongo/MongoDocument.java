package org.wolflink.minecraft.wolfird.framework.database.mongo;

import com.mongodb.lang.Nullable;
import org.bson.Document;
import org.wolflink.minecraft.wolfird.framework.database.common.BaseDocument;

/**
 * Entity层
 * 在与MongoDB互相操作时需要调用 toDocument() 方法转换为 Document 类型
 */
public class MongoDocument extends BaseDocument {

    private Document thisDoc = new Document();

    public Document toDocument() {
        return thisDoc;
    }

    public MongoDocument(Document document) {
        if (document == null) throw new IllegalArgumentException("Can't create MongoDocument because Document is null");
        thisDoc = document;
    }

    public MongoDocument(String name) {
        thisDoc.put("documentName", name);
    }

    @Override
    public String getDocumentName() {
        return thisDoc.getString("documentName");
    }

    @Override @Nullable
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
    @Override @Nullable
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