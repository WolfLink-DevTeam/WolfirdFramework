import org.wolflink.minecraft.wolfird.framework.ioc.Inject;
import org.wolflink.minecraft.wolfird.framework.ioc.Singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class IOC {
    private static final Map<Class<?>,Object> singletonMap = new HashMap<>();
    private static final Stack<Class<?>> busyClasses = new Stack<>();

    public static synchronized <T> T getBean(Class<? extends T> clazz,Object... arguments) {
        System.out.println("getBean "+clazz.getName());
        // 单例
        if(singletonMap.containsKey(clazz))return (T) singletonMap.get(clazz);
        if(busyClasses.contains(clazz)) {
            System.out.println("检测到回环");
            return null;
        }
        System.out.println("推入"+clazz.getName());
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
        System.out.println("推出"+clazz.getName());
        busyClasses.pop();
        return result;
    }
}
