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
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @Override
    public int runTask(Runnable runnable) {
        int taskId = Bukkit.getScheduler().runTask(Framework.getInstance(),runnable).getTaskId();
        taskIdSet.add(taskId);
        return taskId;
    }

    @Override
    public int runTaskAsync(Runnable runnable) {
        int taskId = Bukkit.getScheduler().runTaskAsynchronously(Framework.getInstance(),runnable).getTaskId();
        taskIdSet.add(taskId);
        return taskId;
    }

    @Override
    public int runTaskLater(Runnable runnable, long delay) {
        int taskId = Bukkit.getScheduler().runTaskLater(Framework.getInstance(), runnable, delay).getTaskId();
        taskIdSet.add(taskId);
        return taskId;
    }

    @Override
    public int runTaskLaterAsync(Runnable runnable, long delay) {
        int taskId = Bukkit.getScheduler().runTaskLaterAsynchronously(Framework.getInstance(), runnable, delay).getTaskId();
        taskIdSet.add(taskId);
        return taskId;
    }

    @Override
    public int runTaskTimer(Runnable runnable, long delay, long period) {
        int taskId = Bukkit.getScheduler().runTaskTimer(Framework.getInstance(), runnable, delay, period).getTaskId();
        taskIdSet.add(taskId);
        return taskId;
    }

    @Override
    public int runTaskTimerAsync(Runnable runnable, long delay, long period) {
        int taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(Framework.getInstance(), runnable, delay, period).getTaskId();
        taskIdSet.add(taskId);
        return taskId;
    }

    public void cancelAllTasks() {
        // 取消所有还未完成的任务
        taskIdSet.forEach(id -> Bukkit.getScheduler().cancelTask(id));
        taskIdSet.clear();
    }
}
