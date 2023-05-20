package org.wolflink.minecraft.wolfird.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.wolflink.minecraft.wolfird.framework.bukkit.TNode;
import org.wolflink.minecraft.wolfird.framework.container.CommandContainer;
import org.wolflink.minecraft.wolfird.framework.ioc.Inject;
import org.wolflink.minecraft.wolfird.framework.ioc.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Singleton
public class WolfirdTabCompleter implements TabCompleter {
    @Inject
    private CommandContainer commandContainer;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 0, args.length - 1);
        List<String> result = new ArrayList<>();
        TNode<String> wolfird = commandContainer.getCommandTree().get("wolfird");
        if (wolfird == null) return result;
        TNode<String> node = wolfird.getByPath(args);
        if (node == null) return result;
        return node.getKeys().stream().toList();
    }
}
