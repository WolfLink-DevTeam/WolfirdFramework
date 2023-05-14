package org.wolflink.minecraft.wolfird.framework.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.lang.NonNull;
import lombok.Getter;
import org.bson.Document;
import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.MongoDB;

/**
 * 结构不规则的文档仓库
 */
public class DocumentRepository {
    private @Getter final String table;
    private @Getter final MongoCollection<Document> collection;

    public DocumentRepository(String table) {
        this.table = table;
        collection = Guice.getBean(MongoDB.class).getDatabase().getCollection(table);
        collection.createIndex(new Document("documentName",1), new IndexOptions().unique(true));
    }

    /**
     * 根据文档名获取文档
     * 如果文档不存在则会立刻创建一个
     *
     * @param documentName  文档名
     * @return  WolfDocument对象
     */
    @NonNull
    public WolfDocument findByDocumentName(String documentName){
        Document document = collection.find(new Document("documentName",documentName)).first();
        if(document == null) {
            WolfDocument wolfDocument = new WolfDocument(documentName);
            collection.insertOne(wolfDocument.toDocument());
            return wolfDocument;
        }
        else return new WolfDocument(document);
    }

    /**
     * 更新指定文档指定路径的值
     * 如果文档不存在则会创建文档
     * 如果路径不存在则会创建路径
     *
     * @param documentName  文档名
     * @param path          路径
     * @param value         值
     */
    public void updateValue(String documentName,String path,Object value){
        WolfDocument wolfDocument = findByDocumentName(documentName);
        wolfDocument.putByPath(path,value);
        collection.updateOne(new Document("documentName",documentName),new Document("$set",wolfDocument.toDocument()));
    }

    /**
     * 获取指定文档指定路径的值
     * 如果文档不存在则会创建文档
     * 如果路径不存在则会创建路径
     *
     * @param documentName  文档名
     * @param path          路径
     * @return              值
     */
    @NonNull
    public Object getValue(String documentName,String path,Object defaultValue) {
        WolfDocument wolfDocument = findByDocumentName(documentName);
        Object result = wolfDocument.getByPath(path);
        if(result == null) {
            wolfDocument.putByPath(path,defaultValue);
            result = defaultValue;
            collection.updateOne(new Document("documentName",documentName),new Document("$set",wolfDocument.toDocument()));
        }
        return result;
    }
}
