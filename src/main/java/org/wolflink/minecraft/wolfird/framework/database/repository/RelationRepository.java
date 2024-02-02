package org.wolflink.minecraft.wolfird.framework.database.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 联系型仓库
 */
public abstract class RelationRepository<K1,V1,K2,V2> {
    private final Map<V1,K2> v1Map = new ConcurrentHashMap<>();
    private final Map<V2,K1> v2Map = new ConcurrentHashMap<>();
    public abstract K1 getPrimaryKey1(V1 value);
    public abstract V1 getValue1(K1 key);
    public abstract V2 getValue2(K2 key);
    public abstract K2 getPrimaryKey2(V2 value);
    public void associateByValue(V1 v1,V2 v2) {
        v1Map.put(v1,getPrimaryKey2(v2));
        v2Map.put(v2,getPrimaryKey1(v1));
    }
    public void associateByKey(K1 k1,K2 k2) {
        v1Map.put(getValue1(k1),k2);
        v2Map.put(getValue2(k2),k1);
    }
    public void dissociateByValue(V1 v1,V2 v2) {
        v1Map.remove(v1);
        v2Map.remove(v2);
    }
    public void dissociateByKey(K1 k1,K2 k2) {
        v1Map.remove(getValue1(k1));
        v2Map.remove(getValue2(k2));
    }
    public V2 find2(V1 v1) {
        K2 k2 = v1Map.get(v1);
        return getValue2(k2);
    }
    public V1 find1(V2 v2) {
        K1 k1 = v2Map.get(v2);
        return getValue1(k1);
    }
}
