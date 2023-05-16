package org.wolflink.minecraft.wolfird.framework.bukkit;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.wolflink.minecraft.wolfird.framework.ioc.IOC;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;
import org.wolflink.minecraft.wolfird.framework.notifier.FrameworkNotifier;

public abstract class WolfirdCommand {
    /**
     * 是否需要权限 如果需要则会自动注册权限
     */
    private final boolean requirePerm;
    /**
     * 执行该指令需要的权限(前提是requirePerm设置为true)
     */
    private @Getter String permission;
    /**
     * 允许控制台身份执行
     */
    private final boolean allowConsoleSender;
    /**
     * 允许玩家身份执行
     */
    private final boolean allowPlayerSender;
    /**
     * 指令模板，可带变量，自动解析到当前类，例如：
     * wolfird enable {moduleName}
     * 权限 wolfird.framework.enable.moduleName
     * wolfird {moduleName} help {helpArgs}
     * 权限 wolfird.moduleName.help.helpArgs
     */
    private @Getter final String commandTemplate;
    /**
     * 将 commandTemplate 以空格分隔为字符串数组
     */
    private @Getter final String[] commandArgs;
    /**
     * 指令的帮助信息
     */
    private @Getter final String helpMessage;
    public WolfirdCommand(
            boolean requirePerm,
            boolean allowConsoleSender,
            boolean allowPlayerSender,
            String commandTemplate,
            String helpMessage
    ) {
        this.requirePerm = requirePerm;
        this.allowConsoleSender = allowConsoleSender;
        this.allowPlayerSender = allowPlayerSender;
        this.commandTemplate = commandTemplate;
        this.helpMessage = helpMessage;
        commandArgs = commandTemplate.split(" ");
        permission = String.join(".",commandArgs);
        permission = permission.replace("{","");
        permission = permission.replace("}","");
    }

    /**
     * 尝试执行指令，会检查执行者身份、权限
     */
    public void tryExecute(CommandSender sender) {
        BaseNotifier notifier = IOC.getBean(FrameworkNotifier.class);
        if(sender instanceof ConsoleCommandSender) {
            if(allowConsoleSender) execute(sender);
            else {
                notifier.cmdResult("该指令不允许以控制台身份执行。",sender);
                return;
            }
        }
        if(!allowPlayerSender) {
            notifier.cmdResult("该指令不允许以玩家身份执行。",sender);
            return;
        }
        if(requirePerm) {
            if(sender.hasPermission(permission)) {
                execute(sender);
            } else {
                notifier.cmdResult("权限不足，需要以下权限："+permission,sender);
            }
        } else execute(sender);
    }

    /**
     * 强制执行指令，无视检查
     */
    protected abstract void execute(CommandSender sender);
}