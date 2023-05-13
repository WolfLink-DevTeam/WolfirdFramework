package org.wolflink.minecraft.wolfird.framework.mongo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MongoTable {
    String name();
}
