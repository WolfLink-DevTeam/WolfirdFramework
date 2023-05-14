package org.wolflink.minecraft.wolfird.framework.config;

import org.wolflink.minecraft.wolfird.framework.utils.StringUtil;

/**
 * 文档名称投影
 */
public enum DocumentProjection {
    FRAMEWORK,SOME_OTHER
    ;

    /**
     * 转小驼峰
     */
    @Override
    public String toString() {
        return StringUtil.underline2SmallHump(name());
    }
}
