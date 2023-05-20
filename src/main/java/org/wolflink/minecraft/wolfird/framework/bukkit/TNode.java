package org.wolflink.minecraft.wolfird.framework.bukkit;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 泛型节点类
 * 用于建立树状数据结构
 */
public class TNode<T> {
    @Getter
    private final T value;
    private final Set<TNode<T>> references = new HashSet<>();
    public TNode(T value) {
        this.value = value;
    }
    @Nullable
    public TNode<T> get(T value){
        for (TNode<T> node : references) {
            if(node.value.equals(value))return node;
        }
        return null;
    }
    public void ref(TNode<T> node){
        references.add(node);
    }
}
