package org.wolflink.minecraft.wolfird.framework.database.repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map结构的仓库类，主键不重复
 * 提供基础的增删改查和主键映射
 *
 * @param <K> 主键类型
 * @param <V> 值类型
 */
public abstract class MapRepository<K, V> extends KVRepository<K,V> {
    private final Map<K, V> map = new ConcurrentHashMap<>();

    @Override
    public void insert(V value) {
        map.put(getPrimaryKey(value), value);
    }

    public V find(K key) {
        if (key == null) return null;
        return map.get(key);
    }

    @Override
    public void deleteByKey(K key) {
        map.remove(key);
    }
    @Override
    public void deleteByValue(V value) {
        Set<K> removedKeys = new HashSet<>();
        map.forEach((k,v) -> {
            if(v.equals(value))removedKeys.add(k);
        });
        removedKeys.forEach(this::deleteByKey);
    }

    @Override
    public Collection<V> findAll() {
        return map.values();
    }

    @Override
    public void clear() {
        map.clear();
    }
}
