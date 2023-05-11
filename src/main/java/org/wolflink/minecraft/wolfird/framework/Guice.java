package org.wolflink.minecraft.wolfird.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import org.wolflink.minecraft.wolfird.framework.container.AddonContainer;
import org.wolflink.minecraft.wolfird.framework.container.ModeContainer;
import org.wolflink.minecraft.wolfird.framework.container.SystemContainer;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;

public final class Guice {

    private static Guice INSTANCE;
    private static Guice getInstance() {
        if(INSTANCE == null)INSTANCE = new Guice();
        return INSTANCE;
    }
    private Guice() { }
    private final Injector injector = com.google.inject.Guice.createInjector(new GuiceModule());;

    public static <T> T getBean(Class<T> clazz){
        return getInstance().injector.getInstance(clazz);
    }

}

final class GuiceModule extends AbstractModule {
    @Override public void configure() {
        bind(AddonContainer.class).in(Scopes.SINGLETON);
        bind(ModeContainer.class).in(Scopes.SINGLETON);
        bind(SystemContainer.class).in(Scopes.SINGLETON);
        bind(BaseNotifier.class).in(Scopes.SINGLETON);
    }
}