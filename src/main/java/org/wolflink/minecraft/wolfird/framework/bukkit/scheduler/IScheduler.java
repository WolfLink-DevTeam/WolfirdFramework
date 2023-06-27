package org.wolflink.minecraft.wolfird.framework.bukkit.scheduler;

/**
 * 具有任务调度功能的接口
 */
public interface IScheduler {
    void runTaskLater(Runnable runnable, long delay);
    void runTaskLaterAsync(Runnable runnable, long delay);
    void runTaskTimer(Runnable runnable, long delay, long period);
    void runTaskTimerAsync(Runnable runnable, long delay, long period);
}
