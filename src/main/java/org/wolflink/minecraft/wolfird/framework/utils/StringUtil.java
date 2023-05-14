package org.wolflink.minecraft.wolfird.framework.utils;

public class StringUtil {

    /**
     * 下划线转小驼峰
     */
    public static String underline2SmallHump(final String underlineString) {
        boolean uppercase = false;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0;i < underlineString.length();i++) {
            char c = underlineString.charAt(i);
            if(c == '_') {
                uppercase = true;
                continue;
            }
            if(uppercase) {
                uppercase = false;
                buffer.append(Character.toUpperCase(c));
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
