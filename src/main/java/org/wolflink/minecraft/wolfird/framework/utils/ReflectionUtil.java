package org.wolflink.minecraft.wolfird.framework.utils;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil {
    /**
     * 扫描指定包下同时满足以下两个条件的类：
     * 1.继承自 [superClass]
     * 2.类上拥有注解 [annotation]
     *
     * @param packagePath   被扫描的包路径
     * @param superClass    父类
     * @param annotation    注解
     * @return              满足条件的所有类
     */
    public static <T> Set<Class<T>> getClasses(String packagePath,Class<T> superClass, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<T>> classes = new HashSet<>();
        // 注解匹配
        for(Class<?> clazz : reflections.getTypesAnnotatedWith(annotation)) {
            // 类匹配
            if(clazz.getSuperclass().equals(superClass)) {
                classes.add((Class<T>) clazz);
            }
        }
        return classes;
    }
}
