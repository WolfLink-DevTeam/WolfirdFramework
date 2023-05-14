package org.wolflink.minecraft.wolfird.framework.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.wolflink.minecraft.wolfird.framework.Guice;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.container.CommandContainer;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;

@Singleton
public class WolfirdCommandExecutor implements CommandExecutor {
    @Inject
    BaseNotifier notifier;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandContainer commandContainer = Guice.getBean(CommandContainer.class);
        String[] allArgs = new String[args.length+1];
        allArgs[0] = command.getName();
        System.arraycopy(args, 0, allArgs, 1, args.length);
        WolfirdCommand wolfirdCommand = commandContainer.findBestMatchCommand(allArgs);
        if(wolfirdCommand == null) notifier.cmdResult("未找到匹配的指令。",sender);
        else wolfirdCommand.tryExecute(sender);
        return true;
    }
}
