package org.wolflink.minecraft.wolfird.framework.utils;

import java.util.HashMap;
import java.util.Map;

public class TimingUtil {
    private static final Map<String, Long> timingCache = new HashMap<>();

    /**
     * 开始一个计时任务
     */
    public static void start(final String taskName) {
        timingCache.put(taskName, System.currentTimeMillis());
    }

    /**
     * 结束一个计时任务并返回时间(毫秒)
     */
    public static Long finish(final String taskName) {
        if (!timingCache.containsKey(taskName)) return 0L;
        Long startTime = timingCache.get(taskName);
        timingCache.remove(taskName);
        return System.currentTimeMillis() - startTime;
    }
}
