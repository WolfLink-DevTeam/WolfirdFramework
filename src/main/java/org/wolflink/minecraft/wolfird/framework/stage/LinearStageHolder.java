package org.wolflink.minecraft.wolfird.framework.stage;

import java.util.Arrays;
import java.util.List;

/**
 * 线性流程的阶段管理类
 */
public class LinearStageHolder {
    /**
     * 流程是否循环
     */
    private final boolean recycle;
    /**
     * 不可变列表
     */
    private final List<Stage> linearStages;
    private int nowStageIndex = -1;
    private Stage nowStage = null;
    /**
     * 按先后顺序绑定阶段
     */
    public LinearStageHolder(boolean recycle,Stage... stages) {
        this.recycle = recycle;
        if(stages == null || stages.length == 0)throw new IllegalArgumentException("在创建LinearStageHolder时必须传入至少1个阶段");
        linearStages = Arrays.asList(stages);
    }

    /**
     * 结束当前阶段，进行下一个阶段
     */
    public void next() {
        if(nowStage != null)nowStage.leave();
        nowStageIndex++;
        if(nowStageIndex < linearStages.size()) {
            nowStage = linearStages.get(nowStageIndex);
            nowStage.enter();
        }
        // 没有下一个阶段了，循环
        else if(recycle) {
            nowStage = null;
            nowStageIndex = -1;
            next();
        }
        else {
            throw new IllegalStateException("所有阶段已结束，没有下一个阶段了");
        }
    }
}
