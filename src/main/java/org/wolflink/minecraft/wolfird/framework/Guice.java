package org.wolflink.minecraft.wolfird.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.name.Names;

public final class Guice {

    private static Guice INSTANCE;
    private static Guice getInstance() {
        if(INSTANCE == null)INSTANCE = new Guice();
        return INSTANCE;
    }
    private Guice() { }
    private final Injector injector = com.google.inject.Guice.createInjector(new GuiceModule());;

    /**
     * 主要获取 Bean 的方法
     * 通过 Bean 类获取
     */
    public static <T> T getBean(Class<T> clazz) {
        return getInstance().injector.getInstance(clazz);
    }
}

final class GuiceModule extends AbstractModule {
    @Override public void configure() {
    }
}