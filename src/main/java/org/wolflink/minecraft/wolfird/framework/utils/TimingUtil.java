package org.wolflink.minecraft.wolfird.framework.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimingUtil {
    private static final Map<String, Long> timingCache = new ConcurrentHashMap<>();

    /**
     * 开始一个计时任务
     */
    public static void start(final String taskName) {
        timingCache.put(Thread.currentThread().getId()+"|"+taskName, System.currentTimeMillis());
    }

    /**
     * 结束一个计时任务并返回时间(毫秒)
     */
    public static Long finish(final String taskName) {
        String mapKey = Thread.currentThread().getId()+"|"+taskName;
        if (!timingCache.containsKey(mapKey)) return 0L;
        Long startTime = timingCache.get(mapKey);
        timingCache.remove(mapKey);
        return System.currentTimeMillis() - startTime;
    }
}
