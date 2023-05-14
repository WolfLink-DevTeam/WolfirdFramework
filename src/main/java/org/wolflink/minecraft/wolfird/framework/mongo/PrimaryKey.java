package org.wolflink.minecraft.wolfird.framework.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键标记，创建EntityRepository时传入的Entity类中
 * 必须且只能出现一个由 @PrimaryKey 标记的主键
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey { }
