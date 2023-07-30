package org.wolflink.minecraft.wolfird.framework.bukkit.scheduler;

/**
 * 具有任务调度功能的接口
 */
public interface IScheduler {

    void cancelTask(int taskId);
    int runTask(Runnable runnable);
    int runTaskAsync(Runnable runnable);
    int runTaskLater(Runnable runnable, long delay);
    int runTaskLaterAsync(Runnable runnable, long delay);
    int runTaskTimer(Runnable runnable, long delay, long period);
    int runTaskTimerAsync(Runnable runnable, long delay, long period);
}
