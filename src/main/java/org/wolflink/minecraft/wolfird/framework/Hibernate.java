package org.wolflink.minecraft.wolfird.framework;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class Hibernate {
    private static Hibernate INSTANCE;
    private static Hibernate getInstance() {
        if(INSTANCE == null)INSTANCE = new Hibernate();
        return INSTANCE;
    }
    private final EntityManagerFactory entityManagerFactory;
    public Hibernate() {
        Map<String,String> properties = new HashMap<>();
        entityManagerFactory = new HibernatePersistenceProvider().createEntityManagerFactory("jpaFactory",properties);
    }
    /**
     * 通过 Consumer 操作数据库
     */
    public static void operate(Consumer<EntityManager> consumer) {
        try(EntityManager em = getInstance().entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            consumer.accept(em);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
