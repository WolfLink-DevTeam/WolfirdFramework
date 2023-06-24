package org.wolflink.minecraft.wolfird.framework.notifier;


import org.wolflink.common.ioc.Singleton;

@Singleton
public class FrameworkNotifier extends BaseNotifier {
    public FrameworkNotifier() {
        super("§9Wolfird");
    }
}
