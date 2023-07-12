package org.wolflink.minecraft.wolfird.framework.gamestage.stageholder;

import lombok.Getter;
import lombok.Setter;
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage;

/**
 * 抽象的阶段持有者对象
 * (其子实现可以是线性结构，也可以是树状支线结构，因此抽象程度较高，封装内容少)
 */
public abstract class StageHolder {

    @Getter
    @Setter
    protected Stage thisStage = null;

    /**
     * 进行下一个阶段
     */
    public abstract void next();
    /**
     * 获取下一个阶段(可空)
     * @return 下一阶段
     */
    abstract Stage getNextStage();
}
