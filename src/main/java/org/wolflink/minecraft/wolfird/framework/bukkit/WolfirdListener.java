package org.wolflink.minecraft.wolfird.framework.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.wolflink.minecraft.wolfird.framework.Framework;
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.SubScheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public abstract class WolfirdListener implements Listener {

    @Getter
    private boolean enabled = false;
    @Getter
    private final SubScheduler subScheduler = new SubScheduler();

    public void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        // 从禁用到启用
        if (enabled) {
            Bukkit.getPluginManager().registerEvents(this, Framework.getInstance());
            onEnable();
        }
        // 从启用到禁用
        else {
            // 取消所有还未完成的任务
            subScheduler.cancelAllTasks();
            Set<Class<Event>> eventClasses = new HashSet<>();

            for (Method method : getClass().getDeclaredMethods()) {
                if (method.getAnnotation(EventHandler.class) == null) continue;
                for (Class<?> argClass : method.getParameterTypes()) {
                    // 判断参数类的类型是否继承自 Event 类
                    if (Event.class.isAssignableFrom(argClass)) {
                        eventClasses.add((Class<Event>) argClass);
                    }
                }
            }
            for (Class<Event> eventClass : eventClasses) {
                try {
                    Method getHandlerList = eventClass.getMethod("getHandlerList", null);
                    HandlerList handlerList = (HandlerList) getHandlerList.invoke(null, null);
                    handlerList.unregister(this);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            onDisable();
        }
    }
    public void onEnable(){ }
    public void onDisable(){ }
}
