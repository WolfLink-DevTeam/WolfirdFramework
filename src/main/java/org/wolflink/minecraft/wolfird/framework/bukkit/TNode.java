package org.wolflink.minecraft.wolfird.framework.bukkit;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 泛型节点类
 * 用于建立树状数据结构
 */
public class TNode<T> {
    @Getter
    private final T value;
    private final Map<T, TNode<T>> references = new HashMap<>();

    public TNode(T value) {
        this.value = value;
    }

    public Collection<T> getKeys() {
        return references.keySet();
    }

    public Collection<TNode<T>> getNodes() {
        return references.values();
    }

    @Nullable
    public TNode<T> get(T value) {
        return references.get(value);
    }

    @Nullable
    public TNode<T> getByPath(T[] values) {
        TNode<T> result = this;
        for (T value : values) {
            result = result.get(value);
            if (result == null) return null;
        }
        return result;
    }

    public void ref(TNode<T> node) {
        references.put(node.value, node);
    }

    public void del(T value) {
        references.remove(value);
    }

    public void del(TNode<T> node) {
        references.remove(node.value);
    }

    public void pathRef(T[] pathValues) {
        pathRef(Arrays.stream(pathValues).map(TNode::new).collect(Collectors.toList()));
    }

    /**
     * 按路径层层绑定，在当前节点后按顺序追加这些节点
     */
    public void pathRef(Collection<TNode<T>> pathNodes) {
        TNode<T> nowNode = this;
        for (TNode<T> node : pathNodes) {
            if (nowNode == null) throw new IllegalArgumentException("进行路径绑定时发现了 value 异常的节点");
            if (nowNode.get(node.value) == null) nowNode.ref(node);
            nowNode = nowNode.get(node.value);
        }
    }

    /**
     * 按路径层层解除绑定，在当前节点后按顺序追加这些节点
     */
    public void pathDel(T[] pathValues) {
        if (pathValues.length == 0) return;
        TNode<T> pathNode = get(pathValues[0]);
        if (pathNode == null) return;
        if (pathValues.length > 1) {
            pathNode.pathDel(Arrays.copyOfRange(pathValues, 1, pathValues.length));
            if (pathNode.references.size() == 0) del(pathNode);
        } else del(pathValues[0]);
    }
}

