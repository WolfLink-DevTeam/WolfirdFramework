package org.wolflink.minecraft.wolfird.framework.notifier;

import com.google.inject.Singleton;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 通用消息播报者
 *
 * 播报消息格式：
 * 控制台 -> [前缀|等级] 信息
 * 聊天框(带颜色) -> [ 前缀 ] ››› 信息
 * 例如：
 * [Wolfird|信息] abab
 */
@Singleton
public class BaseNotifier {
    private final Logger logger = Bukkit.getLogger();
    private boolean debugMode = false;
    private final String consoleTemplate;
    private final String chatTemplate;
    private final String notifyTemplate;
    private final String prefix;
    public BaseNotifier() {
        this("§9Wolfird",
                "§8[{prefix}§7|{level}§8] §r{msg}",
                "§8[ {prefix} §8] §f›§7›§8› §r{msg}",
                "\n§8[ {prefix} §8] §f›§7›§8› \n\n§r{msg}\n\n"
        );
    }
    public BaseNotifier(String prefix,String consoleTemplate,String chatTemplate,String notifyTemplate) {
        this.prefix = prefix;
        this.consoleTemplate = consoleTemplate;
        this.chatTemplate = chatTemplate;
        this.notifyTemplate = notifyTemplate;
    }

    /**
     * 切换 DEBUG 模式
     * @return 改变后 DEBUG 模式是否启用
     */
    public boolean toggleDebug() {
        debugMode = !debugMode;
        return debugMode;
    }

    public void debug(String msg) {
        if(! debugMode) return;
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§e调试")
                .replace("{msg}","§e"+msg);
        logger.log(Level.WARNING,text);
    }
    public void info(String msg) {
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§b信息")
                .replace("{msg}",msg);
        logger.log(Level.INFO,text);
    }

    public void warn(String msg) {
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§e警告")
                .replace("{msg}","§e"+msg);
        logger.log(Level.WARNING,text);
    }

    public void error(String msg) {
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§c错误")
                .replace("{msg}","§c"+msg);
        logger.log(Level.SEVERE,text);
    }

    /**
     * 自定义消息，不带任何格式
     */
    public void custom(String fullText) {
        logger.log(Level.INFO,fullText);
    }

    /**
     * 聊天栏消息，但后台也可见
     */
    public void chat(String msg) {
        String text = chatTemplate
                .replace("{prefix}",prefix)
                .replace("{msg}",msg);
        Bukkit.broadcastMessage(text);
        logger.log(Level.INFO,text);
    }

    /**
     * 重要消息通知，会占用较多显示区域，
     * 同时发送给聊天栏和后台
     */
    public void notify(String msg) {
        String text = notifyTemplate
                .replace("{prefix}",prefix)
                .replace("{msg}",msg);
        Bukkit.broadcastMessage(text);
        logger.log(Level.INFO,text);
    }

    /**
     * 测试方法
     * 以各种格式输出信息，以便调整格式
     */
    public void easyTest() {
        chat("这是聊天信息");
        notify("这是醒目信息");
        debug("这是调试信息1");
        toggleDebug();
        debug("这是调试信息2");
        info("这是普通信息");
        warn("这是警告信息");
        error("这是错误信息");
    }

}
