package org.wolflink.minecraft.wolfird.framework.ioc;

import javax.annotation.Nonnull;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class IOC {

    // 存储单例对象的映射表
    private static final ConcurrentHashMap<Class<?>, Object> singletonMap = new ConcurrentHashMap<>();
    // 存储所有线程的busyClass堆栈的映射表
    private static final ConcurrentHashMap<Long, ArrayDeque<Class<?>>> busyClassMap = new ConcurrentHashMap<>();

    // 常量定义
    private static final String LOOPBACK_ERROR = "不允许回环依赖注入: ";
    private static final String NULL_RESULT = " 实例化结果为 null";
    private static final String BEAN_PROVIDER_SHADOWED = """
            在注册 BeanProvider 时出现冲突，数据已被以下信息覆盖：
            Bean 类型：%type%
            Bean 提供者方法：%provider%
            Bean 配置类：%config%
            """;
    private static final String BEAN_NO_CONSTRUCTOR = """
            未能找到无参构造方法，相关类：
            PackageName：%package_name%
            Name：%class_name%
            """;
    private static final String BEAN_CONSTRUCTOR_SECURITY = """
            无权访问无参构造方法，相关类：
            PackageName：%package_name%
            Name：%class_name%
            """;
    private static final Map<Class<?>, Supplier<Object>> beanProviders = new ConcurrentHashMap<>();
    // 添加私有constructor防止实例化工具类
    private IOC() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 传入 Bean 配置类，通过反射扫描获取其中的 BeanProvider 注解获取提供者方法
     * 将方法封装到 Supplier
      */
    public static void registerBeanConfiguration(Object beanConfig) {
        Class<?> beanConfigClass = beanConfig.getClass();
        Arrays.stream(beanConfigClass.getDeclaredMethods()).forEach(method -> {
            if(Arrays.stream(method.getDeclaredAnnotations()).anyMatch(it -> it.annotationType().equals(BeanProvider.class))) {
                Class<?> returnType = method.getReturnType();
                if(beanProviders.containsKey(returnType)) {
                    System.out.println(BEAN_PROVIDER_SHADOWED
                            .replace("%type%",returnType.getName())
                            .replace("%provider%",method.getName())
                            .replace("%config%",beanConfigClass.getName())
                    );
                }
                beanProviders.put(returnType,()->{
                    try {
                        return method.invoke(beanConfig);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            }
        });
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
        if (singletonMap.containsKey(clazz)) {
            return clazz.cast(singletonMap.get(clazz));
        }
        T result = createBean(clazz, arguments);
        if (result == null) {
            throw new NullPointerException(clazz.getName() + NULL_RESULT);
        }
        return result;
    }

    /**
     * IOC Bean 层面实例创建方法
     * 判断单例 Bean 并存储
     * 完成依赖注入
     */

    private static <T> T createBean(Class<? extends T> clazz, Object... arguments) {
        T result = null;
        ArrayDeque<Class<?>> busyClasses = getBusyClasses();
        try {
            checkCircularDependency(clazz);
            busyClasses.push(clazz);
            result = createInstance(clazz, arguments);
            setField(clazz, result);
            saveSingleton(clazz, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        busyClasses.pop();
        return result;
    }

    /**
     * 获取当前线程的busyClass堆栈，实际上是一个ArrayDeque
     */
    @Nonnull
    private static ArrayDeque<Class<?>> getBusyClasses() {
        return busyClassMap.computeIfAbsent(Thread.currentThread().getId(), k -> new ArrayDeque<>());
    }

    /**
     * 检查是否存在回环依赖
     */
    private static void checkCircularDependency(Class<?> clazz) {
        if (getBusyClasses().contains(clazz)) {
            throw new IllegalArgumentException(LOOPBACK_ERROR + getBusyClasses().stream().map(Class::getName).collect(Collectors.joining(" -> ")));
        }
    }

    /**
     * 反射层实例创建方法
     * 扫描 BeanConfiguration 寻找是否有匹配的 Bean Provider
     * 如果存在则优先考虑从 BeanConfiguration 中获取 Bean
     * 如果不存在则根据构造方法参数创建实例
     */
    @Nonnull
    private static <T> T createInstance(@Nonnull Class<? extends T> clazz, Object... arguments) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (arguments.length == 0) {
            // 遍历 BeanConfigurations 寻找适配的 Bean Provider
            if(beanProviders.containsKey(clazz)) return (T) beanProviders.get(clazz).get();
            else {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (NoSuchMethodException e) {
                    System.out.println(BEAN_NO_CONSTRUCTOR
                            .replace("%package_name%",clazz.getPackageName())
                            .replace("%class_name%",clazz.getName()));
                } catch (SecurityException e) {
                    System.out.println(BEAN_CONSTRUCTOR_SECURITY
                            .replace("%package_name%",clazz.getPackageName())
                            .replace("%class_name%",clazz.getName()));
                }
            }
        }
        Constructor<? extends T> constructor;
        constructor = clazz.getConstructor(Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new));
        return constructor.newInstance(arguments);
    }

    /**
     * 注入依赖字段
     */
    private static <T> void setField(@Nonnull Class<? extends T> clazz, T result) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Inject.class) != null) {
                if (!Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                }
                field.set(result, getBean(field.getType()));
            }
        }
    }

    /**
     * 如果是单例类，则保存到映射表中
     */
    private static <T> void saveSingleton(@Nonnull Class<? extends T> clazz, T result) {
        if (clazz.getAnnotation(Singleton.class) != null) {
            singletonMap.put(clazz, result);
        }
    }
}
