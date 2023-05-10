package org.wolflink.minecraft.wolfird.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import lombok.Getter;
import org.wolflink.minecraft.wolfird.framework.ioc.AddonContainer;
import org.wolflink.minecraft.wolfird.framework.ioc.ModeContainer;
import org.wolflink.minecraft.wolfird.framework.ioc.SystemContainer;

import java.util.ArrayList;
import java.util.List;

public final class Guice {

    private static Guice INSTANCE;
    public static Guice getInstance() {
        if(INSTANCE == null)INSTANCE = new Guice();
        return INSTANCE;
    }
    private Guice() {
        configModules.add(new GuiceModule());
    }
    private final List<AbstractModule> configModules = new ArrayList<>();

    @Getter private Injector injector;

    void addConfigModule(AbstractModule module) {
        configModules.add(module);
    }

    /**
     * 在所有子插件加载完毕后调用
     */
    void init() {
        injector = com.google.inject.Guice.createInjector(configModules);
    }

}

final class GuiceModule extends AbstractModule {
    @Override public void configure() {
        bind(AddonContainer.class).in(Scopes.SINGLETON);
        bind(ModeContainer.class).in(Scopes.SINGLETON);
        bind(SystemContainer.class).in(Scopes.SINGLETON);
    }
}