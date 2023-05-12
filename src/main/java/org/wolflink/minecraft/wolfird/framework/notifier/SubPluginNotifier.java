package org.wolflink.minecraft.wolfird.framework.notifier;

public class SubPluginNotifier extends BaseNotifier{
    /**
     * 前缀需要指定颜色，例如 §9Wolfird
     */
    public SubPluginNotifier(String prefix) {
        super(prefix,
                "§8[{prefix}§7|{level}§8] §r{msg}",
                "§8[ {prefix} §8] §f›§7›§8› §r{msg}",
                "\n§8[ {prefix} §8] §f›§7›§8› \n\n§r{msg}\n\n"
        );
    }
}
