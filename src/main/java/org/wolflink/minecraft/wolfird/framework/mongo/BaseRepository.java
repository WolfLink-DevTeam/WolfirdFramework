package org.wolflink.minecraft.wolfird.framework.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.lang.Nullable;
import lombok.Getter;
import org.bson.Document;
import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.MongoDB;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 实体仓库，提供基本的增删改查方法
 * 实体类中至少有一个元素被 @PrimaryKey 标记
 */
public class BaseRepository<E> {
    private @Getter String table = null;
    private @Getter final MongoCollection<E> collection;
    private @Getter final Class<E> entityClass;
    // 主键名(主键也只允许String类型)
    private @Getter String primaryKey = null;
    public BaseRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
        if(entityClass.getAnnotation(MongoTable.class) != null){
            this.table = entityClass.getAnnotation(MongoTable.class).name();
        }
        for (Field field : entityClass.getDeclaredFields()){
            if(field.getAnnotation(PrimaryKey.class) != null){
                primaryKey = field.getName();
                break;
            }
        }
        if(primaryKey == null)throw new IllegalArgumentException("实体类中没有用 @PrimaryKey 注解标记主键");
        collection = Guice.getBean(MongoDB.class).getDatabase().getCollection(table,entityClass);
        collection.createIndex(new Document(primaryKey,1), new IndexOptions().unique(true));
    }
    @Nullable
    public E findByPrimaryKey(Object value) {
        return collection.find(new Document(primaryKey,value)).first();
    }
    public void insert(List<? extends E> entities) {
        collection.insertMany(entities);
    }
    public void insert(E entity) {
        collection.insertOne(entity);
    }
    public void updateByPrimaryKey(Object value,E entity) {
        deleteByPrimaryKey(value);
        insert(entity);
    }
    public void deleteByPrimaryKey(Object value) { collection.deleteOne(new Document(primaryKey,value)); }
    public void clear() { collection.drop(); }
}
