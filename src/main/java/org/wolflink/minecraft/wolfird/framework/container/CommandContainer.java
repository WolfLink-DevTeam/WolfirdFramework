package org.wolflink.minecraft.wolfird.framework.container;

import lombok.Getter;
import org.wolflink.minecraft.wolfird.framework.bukkit.TNode;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.command.CmdHelp;
import org.wolflink.minecraft.wolfird.framework.ioc.IOC;
import org.wolflink.minecraft.wolfird.framework.ioc.Singleton;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class CommandContainer {
    /**
     * 存放注册的所有指令
     * 匹配的时候直接异步遍历匹配然后执行(指令数应该不超过100条，性能影响很小)
     */
    private final @Getter Set<WolfirdCommand> commands = new HashSet<>();
    /**
     * 指令根节点，下一个节点是 wolfird
     */
    private final @Getter TNode<String> commandTree = new TNode<>("");
    /**
     * 单独注册指令，一般是提供给子插件调用
     */
    public void registerCommand(WolfirdCommand command) {
        commandTree.pathRef(command.getCommandArgs());
        commands.add(command);
    }
    /**
     * 单独注销指令，一般是提供给子插件调用
     */
    public void unregisterCommand(WolfirdCommand command) {
        commandTree.pathDel(command.getCommandArgs());
        commands.remove(command);
    }
    /**
     * 初始化框架的所有指令
     */
    public void registerCommands() {
        registerCommand(IOC.getBean(CmdHelp.class));
    }
    /**
     * 寻找最佳匹配指令
     */
    public WolfirdCommand findBestMatchCommand(String[] inputArgs) {
        int inputLen = inputArgs.length;
        for (WolfirdCommand command : commands) {
            // 长度不匹配，跳过
            if(command.getCommandArgs().length != inputLen) continue;
            boolean match = true;
            // 长度匹配的情况再做具体判定
            for (int i = 0 ; i < inputLen ; i++) {
                String cmdArg = command.getCommandArgs()[i];
                if(cmdArg.contains("{"))continue;
                String inputArg = inputArgs[i];
                if(! cmdArg.equalsIgnoreCase(inputArg)) {
                    match = false;
                    break;
                }
            }
            if(match)return command;
        }
        return null;
    }

}
