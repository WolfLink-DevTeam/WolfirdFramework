package org.wolflink.minecraft.wolfird.framework.gamestage.stage;

import lombok.Getter;
import org.wolflink.minecraft.wolfird.framework.bukkit.scheduler.SubScheduler;
import org.wolflink.minecraft.wolfird.framework.gamestage.stageholder.StageHolder;

public abstract class Stage {
    @Getter
    private final String displayName;
    @Getter
    private final SubScheduler subScheduler;
    @Getter
    private final StageHolder stageHolder;

    public Stage(String displayName, StageHolder stageHolder) {
        this.displayName = displayName;
        subScheduler = new SubScheduler();
        this.stageHolder = stageHolder;
    }

    /**
     * 抽象层面开始阶段相关实现
     */
    public void enter() {
        onEnter();
    }

    /**
     * 抽象层面离开阶段相关实现
     */
    public void leave() {
        subScheduler.cancelAllTasks();
        onLeave();
    }

    /**
     * 具体阶段进入时的业务实现
     */
    protected abstract void onEnter();
    /**
     * 具体阶段离开时的业务实现
     */
    protected abstract void onLeave();
}
