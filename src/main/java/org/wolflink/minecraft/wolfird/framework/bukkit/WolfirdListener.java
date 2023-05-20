package org.wolflink.minecraft.wolfird.framework.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.wolflink.minecraft.wolfird.framework.Framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

// TODO 还应该提供对 SchedulerTask 相关的封装，否则在监听器禁用时这些任务仍在执行，不符合逻辑
public abstract class WolfirdListener implements Listener {

    private boolean enabled = false;
    /**
     * 存储该监听器涉及到的所有调度器任务，以便于禁用监听器时注销所有任务
     */
    private final Set<Integer> taskIdSet = new HashSet<>();

    public void runTaskLater(Runnable runnable, long delay) {
        taskIdSet.add(Bukkit.getScheduler().runTaskLater(Framework.getInstance(), runnable, delay).getTaskId());
    }

    public void runTaskLaterAsync(Runnable runnable, long delay) {
        taskIdSet.add(Bukkit.getScheduler().runTaskLaterAsynchronously(Framework.getInstance(), runnable, delay).getTaskId());
    }

    public void runTaskTimer(Runnable runnable, long delay, long period) {
        taskIdSet.add(Bukkit.getScheduler().runTaskTimer(Framework.getInstance(), runnable, delay, period).getTaskId());
    }

    public void runTaskTimerAsync(Runnable runnable, long delay, long period) {
        taskIdSet.add(Bukkit.getScheduler().runTaskTimerAsynchronously(Framework.getInstance(), runnable, delay, period).getTaskId());
    }

    public void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        // 从禁用到启用
        if (enabled) {
            Bukkit.getPluginManager().registerEvents(this, Framework.getInstance());
        }
        // 从启用到禁用
        else {
            // 取消所有还未完成的任务
            taskIdSet.forEach(id -> Bukkit.getScheduler().cancelTask(id));
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
        }
    }
}
