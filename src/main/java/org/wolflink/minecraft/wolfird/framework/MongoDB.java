package org.wolflink.minecraft.wolfird.framework;

import com.google.inject.Singleton;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;

import java.util.function.Consumer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Singleton
public final class MongoDB {
    @Setter
    @Getter
    private boolean error = true;
    MongoClient client;
    @Getter
    MongoDatabase database;
    // TODO 数据库的连接信息应该从其他地方修改，现在是存放在数据库中的，但是不配置好连接信息又连不上数据库
    public MongoDB() {
        this(
                Framework.getINSTANCE().getConfig().getString("mongo_connection_url"),
                Framework.getINSTANCE().getConfig().getString("mongo_database_name")
        );
    }
    private MongoDB(String connectionUrl,String databaseName) {

        new Thread(()->{
            try {
                Thread.sleep(1000 * 10);
                if(!error)return;
                Guice.getBean(BaseNotifier.class).error("无法创建数据库连接，请前往 config/WolfirdFramework/config.yml 修改相关配置");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

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
