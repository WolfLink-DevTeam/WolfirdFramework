package org.wolflink.minecraft.wolfird.framework.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.wolflink.minecraft.wolfird.framework.Framework;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于 Bukkit Scheduler 的子调度器
 * 支持一键注销由该调度器发起的所有任务
 */
public class SubScheduler implements IScheduler {
    /**
     * 存储该调度器发起的所有任务，以便于统一管理并注销任务
     */
    private final Set<Integer> taskIdSet = new HashSet<>();

    @Override
    public void runTaskLater(Runnable runnable, long delay) {
        taskIdSet.add(Bukkit.getScheduler().runTaskLater(Framework.getInstance(), runnable, delay).getTaskId());
    }

    @Override
    public void runTaskLaterAsync(Runnable runnable, long delay) {
        taskIdSet.add(Bukkit.getScheduler().runTaskLaterAsynchronously(Framework.getInstance(), runnable, delay).getTaskId());
    }

    @Override
    public void runTaskTimer(Runnable runnable, long delay, long period) {
        taskIdSet.add(Bukkit.getScheduler().runTaskTimer(Framework.getInstance(), runnable, delay, period).getTaskId());
    }

    @Override
    public void runTaskTimerAsync(Runnable runnable, long delay, long period) {
        taskIdSet.add(Bukkit.getScheduler().runTaskTimerAsynchronously(Framework.getInstance(), runnable, delay, period).getTaskId());
    }

    public void cancelAllTasks() {
        // 取消所有还未完成的任务
        taskIdSet.forEach(id -> Bukkit.getScheduler().cancelTask(id));
        taskIdSet.clear();
    }
}
