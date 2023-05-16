package org.wolflink.minecraft.wolfird.framework.ioc;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class IOC {
    private static final Map<Class<?>,Object> singletonMap = new HashMap<>();
    private static final Stack<Class<?>> busyClasses = new Stack<>();

    /**
     * 获取给定类的实例
     * @param clazz     Bean Class(用@Singleton标记单例)
     * @param arguments 构造方法参数，如果希望获取的是单例并且IOC容器中已经存在该实例，则可以不传参数列表
     */
    @Nonnull
    public static synchronized <T> T getBean(Class<? extends T> clazz,Object... arguments) {
        // 单例
        if(singletonMap.containsKey(clazz))return (T) singletonMap.get(clazz);
        if(busyClasses.contains(clazz)) {
            throw new RuntimeException("不允许回环依赖注入："+busyClasses.stream().map(Class::getName).collect(Collectors.joining()));
        }
        busyClasses.push(clazz);
        T result = null;
        try {
            Constructor<? extends T> constructor;
            try {
                constructor = clazz.getConstructor(Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new));
                result = constructor.newInstance(arguments);
            } catch (NoSuchMethodException e) {
                result = clazz.getDeclaredConstructor().newInstance();
            }
            for (Field field : clazz.getDeclaredFields()) {
                if(field.getAnnotation(Inject.class) != null) {
                    field.setAccessible(true);
                    field.set(result,getBean(field.getType()));
                }
            }
            if(clazz.getAnnotation(Singleton.class) != null) singletonMap.put(clazz,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        busyClasses.pop();
        if(result == null)throw new NullPointerException(clazz.getName()+" 实例化结果为 null");
        return result;
    }
}
