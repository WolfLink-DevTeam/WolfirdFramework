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
     * 指令模板，可指令变量自动解析，例如：
     * wolfird enable {moduleName}
     * 权限 wolfird.framework.enable.moduleName
     * wolfird {moduleName} help {helpArgs}
     * 权限 wolfird.moduleName.help.helpArgs
     * 变量名不会保留，调用时通过 args[0]、args[1] 从左到右按顺序使用变量
     */
    private @Getter final String commandTemplate;
    /**
     * 将 commandTemplate 以空格分隔为字符串数组
     */
    private @Getter final String[] commandArgs;
    /**
     * 存储变量位置的数组，如[1,3]
     */
    private @Getter final int[] placeholderIndexes;
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

        // 将指令模板中的变量索引值存储到 placeholderIndexes 中进行映射，便于调用
        int pointer = 0;
        int[] indexes = new int[commandArgs.length];
        for (int i = 0;i < commandArgs.length;i++) {
            String arg = commandArgs[i];
            if(arg.contains("{"))indexes[pointer++] = i;
        }
        placeholderIndexes = new int[indexes.length];
        System.arraycopy(indexes, 0, placeholderIndexes, 0, indexes.length);

        permission = String.join(".",commandArgs);
        permission = permission.replace("{","");
        permission = permission.replace("}","");
    }

    /**
     * 尝试执行指令，会检查执行者身份、权限
     * @param sender        指令调用方
     * @param commandParts  指令拆分列表
     */
    public void tryExecute(CommandSender sender,String[] commandParts) {
        BaseNotifier notifier = IOC.getBean(FrameworkNotifier.class);
        String[] args = new String[placeholderIndexes.length];
        for (int i = 0;i < args.length;i++) {
            args[i] = commandParts[placeholderIndexes[i]];
        }
        if(sender instanceof ConsoleCommandSender) {
            if(allowConsoleSender) {
                execute(sender,args);
            }
            else {
                notifier.cmdResult("该指令不允许以控制台身份执行。",sender);
            }
            return;
        }
        if(!allowPlayerSender) {
            notifier.cmdResult("该指令不允许以玩家身份执行。",sender);
            return;
        }
        if(requirePerm) {
            if(sender.hasPermission(permission)) {
                execute(sender,args);
            } else {
                notifier.cmdResult("权限不足，需要以下权限："+permission,sender);
            }
        } else execute(sender,args);
    }

    /**
     * 强制执行指令，无视检查
     * @param sender    指令调用方
     * @param args      根据指令模板解析的有效参数列表
     */
    protected abstract void execute(CommandSender sender,String[] args);
}
