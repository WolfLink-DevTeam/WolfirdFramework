package org.wolflink.minecraft.wolfird.framework;

import com.google.inject.Singleton;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.function.Consumer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Singleton
public final class MongoDB {
    MongoClient client;
    @Getter
    MongoDatabase database;
    public MongoDB() {
        this("mongodb://localhost:27017/","wolfird_db");
    }
    private MongoDB(String connectionUrl,String databaseName) {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionUrl))
                .codecRegistry(codecRegistry)
                .build();
        client = MongoClients.create(clientSettings);
        database = client.getDatabase(databaseName);
    }

    /**
     * 只应该在插件卸载时调用，以免数据库发生问题
     */
    public void close() {
        client.close();
        client = null;
    }

    /**
     * POJO 数据库集合操作
     * @param collectionName    集合名称
     * @param clazz             POJO类
     * @param consumer          POJO消费函数
     */
    public <T> void operate(String collectionName,Class<T> clazz, Consumer<MongoCollection<T>> consumer) {
        MongoCollection<T> tMongoCollection = database.getCollection(collectionName,clazz);
        consumer.accept(tMongoCollection);
    }

    /**
     * Document 数据库集合操作
     * @param collectionName    集合名称
     * @param consumer          Document消费函数
     */
    public void operate(String collectionName, Consumer<MongoCollection<Document>> consumer) {
        MongoCollection<Document> mongoCollection = database.getCollection(collectionName);
        consumer.accept(mongoCollection);
    }
}
