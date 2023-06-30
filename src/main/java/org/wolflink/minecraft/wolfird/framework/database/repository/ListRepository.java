package org.wolflink.minecraft.wolfird.framework.database.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 集合的仓库类，主键可重复
 * 提供基础的增删改查和主键过滤
 *
 * @param <V> 值类型
 */
public abstract class ListRepository<K,V> extends KVRepository<K,V> {
    private final List<V> list = new ArrayList<>();

    /**
     * 根据索引更新单个值
     * @param index 索引
     * @param value 新值
     */
    public void update(int index,V value) {
        list.set(index,value);
    }

    /**
     * 根据主键批量更新
     * @param key       主键
     * @param vConsumer 值消费函数
     */
    public void update(K key, Consumer<V> vConsumer) {
        list.forEach(v -> {
            if(getPrimaryKey(v).equals(key)) {
                vConsumer.accept(v);
            }
        });
    }

    /**
     * 根据主键批量查询
     * @param key   主键
     * @return      匹配主键的值列表
     */
    public List<V> find(K key) {
        if (key == null) return null;
        return list.stream().filter(v -> getPrimaryKey(v).equals(key)).collect(Collectors.toList());
    }

    /**
     * 根据索引查询单个值
     * @param index 索引
     * @return      值
     */
    public V find(int index) {
        if(index >= list.size() || index < 0)return null;
        return list.get(index);
    }

    /**
     * 根据索引删除单个值
     * @param index 索引
     */
    public void delete(int index) {
        list.remove(index);
    }

    @Override
    public Collection<V> findAll() {
        return list;
    }

    @Override
    public void clear() {
        list.clear();
    }
    @Override
    public void insert(V value) {
        list.add(value);
    }

    @Override
    public void deleteByKey(K key) {
        find(key).forEach(this::deleteByValue);
    }

    @Override
    public void deleteByValue(V value) {
        list.remove(value);
    }
}
