package org.wolflink.minecraft.wolfird.framework.database.repository;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class KVRepository<K,V> {
    /**
     * 将值映射为主键
     * @param value 值
     * @return      主键
     */
    public abstract K getPrimaryKey(V value);
    public abstract Collection<V> findAll();
    public abstract void clear();
    /**
     * 向仓库中插入值
     * 如果主键冲突可能会覆盖或者保留，看仓库具体实现
     * @param value 值
     */
    public abstract void insert(V value);
    /**
     * 根据主键删除相关的值
     * @param key   主键
     */
    public abstract void deleteByKey(K key);

    /**
     * 从集合中删除给定值
     * @param value 值
     */
    public abstract void deleteByValue(V value);

    /**
     * 根据条件过滤值
     * @param filter    条件过滤器
     * @return          符合条件的所有值的列表(可能为空列表)
     */
    public Collection<V> findBy(Function<V,Boolean> filter) {
        return findAll().stream().filter(filter::apply).collect(Collectors.toList());
    }
    public int size() {
        return findAll().size();
    }
}
