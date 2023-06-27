package org.wolflink.minecraft.wolfird.framework.stage;

import lombok.Getter;
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.IScheduler;
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.SubScheduler;

public abstract class Stage implements IScheduler {
    @Getter
    private final String displayName;
    private final SubScheduler subScheduler;

    public Stage(String displayName) {
        this.displayName = displayName;
        subScheduler = new SubScheduler();
    }

    /**
     * 开始阶段
     */
    public void enter() {
        onEnter();
    }

    /**
     * 离开阶段
     */
    public void leave() {
        subScheduler.cancelAllTasks();
        onLeave();
    }
    protected abstract void onEnter();
    protected abstract void onLeave();
    @Override
    public void runTaskLater(Runnable runnable, long delay) {
        subScheduler.runTaskLater(runnable,delay);
    }
    @Override
    public void runTaskLaterAsync(Runnable runnable, long delay) {
        subScheduler.runTaskLaterAsync(runnable,delay);
    }
    @Override
    public void runTaskTimer(Runnable runnable, long delay, long period) {
        subScheduler.runTaskTimer(runnable,delay,period);
    }
    @Override
    public void runTaskTimerAsync(Runnable runnable, long delay, long period) {
        subScheduler.runTaskTimerAsync(runnable,delay,period);
    }
}
