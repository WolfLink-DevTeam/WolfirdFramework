package org.wolflink.minecraft.wolfird.framework.database.common;

import com.mongodb.lang.Nullable;
import lombok.Getter;

/**
 * 封装文档类
 * 提供了根据路径插入值和根据路径查询值的方法
 * @deprecated 似乎是多余的设计，并没有需要实现该类的情况
 */
@Deprecated
public abstract class BaseDocument {

    public abstract String getDocumentName();

    /**
     * 根据给定路径设置值
     * @param path 路径例如 server.ssh.port 和 server.ssh.enabled
     * @param value 需要被插入的值
     * @return 被替换前的值(可空)
     */
    @Nullable
    public abstract Object putByPath(final String path, final Object value);

    /**
     * 根据给定路径查询对应值
     * @param path 路径例如 server.ssh.port 和 server.ssh.enabled
     * @return 按路径查询得到的值(可空)
     */
    @Nullable
    public abstract Object getByPath(final String path);
}
