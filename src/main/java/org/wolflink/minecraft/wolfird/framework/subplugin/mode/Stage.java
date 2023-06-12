package org.wolflink.minecraft.wolfird.framework.subplugin.mode;

public abstract class Stage {
    private final long taskPeriod;
    private final String stageName;
    private final String stageDisplayName;

    public Stage(long taskPeriod,String stageName,String stageDisplayName) {
        this.taskPeriod = taskPeriod;
        this.stageName = stageName;
        this.stageDisplayName = stageDisplayName;
    }
    void start() {
        onEnter();
    }
    void end() {

    }

    /**
     * 在进入阶段时调用
     */
    abstract void onEnter();

    /**
     * 在离开阶段时调用
     */
    abstract void onLeave();

    /**
     * 在阶段过程中循环调用
     */
    abstract void timerTask();
}
