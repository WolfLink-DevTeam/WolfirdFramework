package org.wolflink.minecraft.wolfird.framework.bukkit.attribute;

import lombok.Data;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;

/**
 * 属性操作元数据
 */
@Data
public class ModifierData {
    // 属性修改器名称
    private final String modifierName;
    // 属性
    private final Attribute attribute;
    // 操作类型
    private final Operation operation;
    // 数值
    private final double value;
    // Bukkit属性修改器
    private final AttributeModifier attributeModifier;

    public ModifierData(String modifierName, Attribute attribute, Operation operation, double value) {
        this.modifierName = modifierName;
        this.attribute = attribute;
        this.operation = operation;
        this.value = value;
        attributeModifier = new AttributeModifier(modifierName, value, operation);
    }

    public AttributeModifier getModifier() {
        return attributeModifier;
    }
}
