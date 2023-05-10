package org.wolflink.minecraft.wolfird.framework;

import com.google.inject.AbstractModule;

import java.util.ArrayList;
import java.util.List;

public final class Guice {
    public List<AbstractModule> configModules = new ArrayList<>();
}
class GuiceModule extends AbstractModule {
    @Override public void configure() {

    }
}