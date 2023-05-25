package org.wolflink.minecraft.wolfird.framework.bukkit.attribute;


import org.bukkit.Bukkit;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.wolflink.minecraft.wolfird.framework.Framework;
import org.wolflink.minecraft.wolfird.framework.ioc.IOC;
import org.wolflink.minecraft.wolfird.framework.notifier.FrameworkNotifier;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家属性管理器
 */
public class WolfirdAttribute {
    private final Player player;
    public WolfirdAttribute(Player player) {
        this.player = player;
    }
    /**
     * 应用属性修改器
     */
    void applyModifier(ModifierData data){
        AttributeInstance attributeInstance = player.getAttribute(data.getAttribute());
        if(attributeInstance == null) {
            IOC.getBean(FrameworkNotifier.class).warn(data.getAttribute().name()+" 属性实例获取失败，请联系开发者。");
            return;
        }
        attributeInstance.addModifier(data.getModifier());
    }

    /**
     * 移除属性修改器
     */
    void removeModifier(ModifierData data){
        AttributeInstance attributeInstance = player.getAttribute(data.getAttribute());
        if(attributeInstance == null) {
            IOC.getBean(FrameworkNotifier.class).warn(data.getAttribute().name()+" 属性实例获取失败，请联系开发者。");
            return;
        }
        attributeInstance.removeModifier(data.getModifier());
    }

    private final Map<UUID,Integer> tempTaskMap = new ConcurrentHashMap<>();
    /**
     * 临时应用属性修改器
     * 如果该属性修改器已经存在，则会刷新冷却而不会叠加
     */
    void applyTempModifier(ModifierData data,Long durationTicks) {
        UUID modifierUUID = data.getModifier().getUniqueId();
        if(tempTaskMap.containsKey(modifierUUID)) {
            // 取消先前的定时任务
            Bukkit.getScheduler().cancelTask(tempTaskMap.get(modifierUUID));
        }
        else {
            // 应用 新的属性修改器
            applyModifier(data);
        }
        int taskId = Bukkit.getScheduler().runTaskLater(Framework.getInstance(),()->{
            removeModifier(data);
            tempTaskMap.remove(modifierUUID);
        },durationTicks).getTaskId();
        tempTaskMap.put(modifierUUID,taskId);
    }
}