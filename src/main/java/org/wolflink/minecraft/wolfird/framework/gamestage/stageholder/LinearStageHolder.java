package org.wolflink.minecraft.wolfird.framework.gamestage.stageholder;

import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage;

/**
 * 线性流程的阶段管理类
 */
public class LinearStageHolder extends StageHolder {
    /**
     * 流程是否循环
     */
    private final boolean recycle;
    /**
     * 不可变列表
     */
    private Stage[] stages;
    private int thisStageIndex = -1;
    /**
     * 按先后顺序绑定阶段
     */
    public LinearStageHolder(boolean recycle) {
        this.recycle = recycle;
    }
    public void bindStages(Stage[] stages) {
        this.stages = stages;
    }
    @Override
    public void next() {
        if(thisStage != null)thisStage.leave();
        thisStage = getNextStage();
        if(thisStage != null) {
            thisStageIndex++;
            thisStage.enter();
        }  else {
            thisStageIndex = -1;
            if(recycle) next();
            else throw new IllegalStateException("所有阶段已结束，没有下一个阶段了");
        }
    }

    @Override
    Stage getNextStage() {
        int nextStageIndex = thisStageIndex+1;
        if(nextStageIndex < stages.length) {
            return stages[nextStageIndex];
        } else {
            return null;
        }
    }
}
