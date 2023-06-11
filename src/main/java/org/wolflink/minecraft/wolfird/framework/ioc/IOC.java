package org.wolflink.minecraft.wolfird.framework.ioc;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


public class IOC {

    // 存储单例对象的映射表
    private static final ConcurrentHashMap<Class<?>, Object> singletonMap = new ConcurrentHashMap<>();
    // 存储当前正在创建的类
    private static final AtomicReference<Class<?>> busyClass = new AtomicReference<>();

    // 常量定义
    private static final String LOOPBACK_ERROR = "不允许回环依赖注入: ";
    private static final String NULL_RESULT = " 实例化结果为 null";

    // 添加一个私有构造器，防止实例化工具类
    private IOC() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取给定类的实例
     *
     * @param clazz     Bean Class(用@Singleton标记单例)
     * @param arguments 构造方法参数，如果希望获取的是单例并且IOC容器中已经存在该实例，则可以不传参数列表
     * @return 给定类的实例
     */
    @Nonnull
    public static <T> T getBean(Class<? extends T> clazz, Object... arguments) {
        // 双重检查锁定
        if (singletonMap.containsKey(clazz)) {
            return clazz.cast(singletonMap.get(clazz));
        }
        synchronized (IOC.class) {
            if (singletonMap.containsKey(clazz)) {
                return clazz.cast(singletonMap.get(clazz));
            }
            T result = createBean(clazz, arguments);
            if (result == null) {
                throw new NullPointerException(clazz.getName() + NULL_RESULT);
            }
            return result;
        }
    }

    // 创建Bean实例，并注入依赖
    private static <T> T createBean(Class<? extends T> clazz, Object... arguments) {
        T result = null;
        try {
            checkCircularDependency(clazz);
            busyClass.set(clazz);
            result = createInstance(clazz, arguments);
            setField(clazz, result);
            saveSingleton(clazz, result);
            busyClass.set(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 检查是否存在回环依赖
    private static void checkCircularDependency(Class<?> clazz) {
        if (busyClass.get() == clazz) {
            throw new IllegalArgumentException(LOOPBACK_ERROR + busyClass.get().getName());
        }
    }

    // 根据构造方法参数创建实例
    private static <T> T createInstance(Class<? extends T> clazz, Object... arguments) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<? extends T> constructor;
        try {
            constructor = clazz.getConstructor(Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new));
            return constructor.newInstance(arguments);
        } catch (NoSuchMethodException e) {
            return clazz.getDeclaredConstructor().newInstance();
        }
    }

    // 注入依赖字段
    private static <T> void setField(Class<? extends T> clazz, T result) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Inject.class) != null) {
                if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                }
                field.set(result, getBean(field.getType()));
            }
        }
    }

    // 如果是单例类，则保存到映射表中
    private static <T> void saveSingleton(Class<? extends T> clazz, T result) {
        if (clazz.getAnnotation(Singleton.class) != null) {
            singletonMap.put(clazz, result);
        }
    }
}
