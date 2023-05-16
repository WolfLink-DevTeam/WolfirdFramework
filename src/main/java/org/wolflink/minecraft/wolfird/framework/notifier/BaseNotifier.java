package org.wolflink.minecraft.wolfird.framework.notifier;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.wolflink.minecraft.wolfird.framework.config.ConfigProjection;
import org.wolflink.minecraft.wolfird.framework.config.FrameworkConfig;
import org.wolflink.minecraft.wolfird.framework.ioc.IOC;

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
public abstract class BaseNotifier {
    private final Logger logger = Bukkit.getLogger();
    private @Setter boolean debugMode = false;
    private final boolean colorfulConsole;
    private static final String consoleColorFilter = "§[0-9a-fk-or]";
    private String consoleTemplate;
    private final String chatTemplate;
    private final String notifyTemplate;
    private @Setter @Getter String prefix;
    public BaseNotifier(String prefix) {
        this(prefix,
                IOC.getBean(FrameworkConfig.class).get(ConfigProjection.NOTIFIER_CONSOLE_TEMPLATE),
                IOC.getBean(FrameworkConfig.class).get(ConfigProjection.NOTIFIER_CHAT_TEMPLATE),
                IOC.getBean(FrameworkConfig.class).get(ConfigProjection.NOTIFIER_NOTIFY_TEMPLATE),
                IOC.getBean(FrameworkConfig.class).get(ConfigProjection.NOTIFIER_COLORFUL_CONSOLE)
        );
    }
    public BaseNotifier(String prefix,String consoleTemplate,String chatTemplate,String notifyTemplate,boolean colorfulConsole) {
        this.prefix = prefix;
        this.consoleTemplate = consoleTemplate;
        this.chatTemplate = chatTemplate;
        this.notifyTemplate = notifyTemplate;
        this.colorfulConsole = colorfulConsole;
        if(!colorfulConsole) this.consoleTemplate = consoleTemplate.replaceAll(consoleColorFilter,"");
    }
    public void debug(String msg) {
        if(! debugMode) return;
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§e调试")
                .replace("{msg}","§e"+msg);
        if(!colorfulConsole)text = text.replaceAll(consoleColorFilter,"");
        logger.log(Level.WARNING,text);
    }
    public void info(String msg) {
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§b信息")
                .replace("{msg}",msg);
        if(!colorfulConsole)text = text.replaceAll(consoleColorFilter,"");
        logger.log(Level.INFO,text);
    }

    public void warn(String msg) {
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§e警告")
                .replace("{msg}","§e"+msg);
        if(!colorfulConsole)text = text.replaceAll(consoleColorFilter,"");
        logger.log(Level.WARNING,text);
    }

    public void error(String msg) {
        String text = consoleTemplate
                .replace("{prefix}",prefix)
                .replace("{level}","§c错误")
                .replace("{msg}","§c"+msg);
        if(!colorfulConsole)text = text.replaceAll(consoleColorFilter,"");
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
        if(!colorfulConsole)text = text.replaceAll(consoleColorFilter,"");
        logger.log(Level.INFO,text);
    }
    public void chat(String msg, Player p) {
        String text = chatTemplate
                .replace("{prefix}",prefix)
                .replace("{msg}",msg);
        p.sendMessage(text);
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
        if(!colorfulConsole)text = text.replaceAll(consoleColorFilter,"");
        logger.log(Level.INFO,text);
    }
    public void notify(String msg,Player p) {
        String text = notifyTemplate
                .replace("{prefix}",prefix)
                .replace("{msg}",msg);
        p.sendMessage(text);
    }
    public void cmdResult(String msg,CommandSender sender) {
        String text = notifyTemplate
                .replace("{prefix}",prefix)
                .replace("{msg}",msg);
        if(sender instanceof ConsoleCommandSender && !colorfulConsole)text = text.replaceAll(consoleColorFilter,"");
        sender.sendMessage(text);
    }

    /**
     * 测试方法
     * 以各种格式输出信息，以便调整格式
     */
    public void easyTest() {
        chat("这是聊天信息");
        notify("这是醒目信息");
        debug("这是调试信息1");
        debugMode = true;
        debug("这是调试信息2");
        info("这是普通信息");
        warn("这是警告信息");
        error("这是错误信息");
    }

}
