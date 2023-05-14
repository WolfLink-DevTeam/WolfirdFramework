package org.wolflink.minecraft.wolfird.framework.container;

import com.google.inject.Singleton;
import lombok.Getter;
import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.command.CmdHelp;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class CommandContainer {
    /**
     * 存放注册的所有指令
     * 匹配的时候直接异步遍历匹配然后执行(指令数应该不超过100条，性能影响很小)
     */
    private final @Getter Set<WolfirdCommand> commands = new HashSet<>();
    public void registerCommand(WolfirdCommand wolfirdCommand) {
        commands.add(wolfirdCommand);
    }
    public CommandContainer() {
        commands.add(Guice.getBean(CmdHelp.class));
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
