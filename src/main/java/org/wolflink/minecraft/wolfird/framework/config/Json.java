package org.wolflink.minecraft.wolfird.framework.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用来标记数据类可被 Gson 序列化 / 解序列化
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Json { }
