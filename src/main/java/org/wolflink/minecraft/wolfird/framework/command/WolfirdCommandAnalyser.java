package org.wolflink.minecraft.wolfird.framework.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.wolflink.common.ioc.Inject;
import org.wolflink.minecraft.wolfird.framework.bukkit.TNode;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.container.CommandContainer;
import org.wolflink.common.ioc.IOC;
import org.wolflink.common.ioc.Singleton;
import org.wolflink.minecraft.wolfird.framework.notifier.BaseNotifier;
import org.wolflink.minecraft.wolfird.framework.notifier.FrameworkNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class WolfirdCommandAnalyser implements CommandExecutor, TabCompleter {
    BaseNotifier notifier = IOC.getBean(FrameworkNotifier.class);
    @Inject
    private CommandContainer commandContainer;

    public void register(String mainCommand) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(mainCommand);
        if(pluginCommand == null) {
            notifier.warn("尝试注册指令失败，未找到该 Bukkit 指令："+mainCommand);
            return;
        }
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandContainer commandContainer = IOC.getBean(CommandContainer.class);
        String[] allArgs = new String[args.length + 1];
        allArgs[0] = command.getName();
        System.arraycopy(args, 0, allArgs, 1, args.length);
        WolfirdCommand wolfirdCommand = commandContainer.findBestMatchCommand(allArgs);
        if (wolfirdCommand == null) notifier.cmdResult("未找到匹配的指令。", sender);
        else wolfirdCommand.tryExecute(sender, allArgs);
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 0, args.length - 1);
        List<String> result = new ArrayList<>();
        TNode<String> wolfird = commandContainer.getCommandTree().get(command.getName());
        if (wolfird == null) return result;
        TNode<String> node = wolfird.getByPath(args);
        if (node == null) return result;
        return new ArrayList<>(node.getKeys());
    }
}
