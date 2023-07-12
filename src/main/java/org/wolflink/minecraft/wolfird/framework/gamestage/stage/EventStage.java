package org.wolflink.minecraft.wolfird.framework.gamestage.stage;


import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdListener;
import org.wolflink.minecraft.wolfird.framework.gamestage.stageholder.StageHolder;

/**
 * Stage 的基础上封装了一个 WolfirdListener
 */
public abstract class EventStage extends Stage {
    private final WolfirdListener listener;
    public EventStage(String displayName, StageHolder stageHolder, WolfirdListener listener) {
        super(displayName,stageHolder);
        this.listener = listener;
    }
    @Override
    public void enter() {
        listener.setEnabled(true);
        super.enter();
    }
    @Override
    public void leave() {
        listener.setEnabled(false);
        super.leave();
    }
}
