package org.wolflink.minecraft.wolfird.framework.command;


import org.bukkit.command.CommandSender;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.container.CommandContainer;
import org.wolflink.minecraft.wolfird.framework.ioc.Inject;
import org.wolflink.minecraft.wolfird.framework.ioc.Singleton;
import org.wolflink.minecraft.wolfird.framework.notifier.FrameworkNotifier;

@Singleton
public class CmdHelp extends WolfirdCommand {
    @Inject
    private FrameworkNotifier notifier;
    @Inject
    private CommandContainer commandContainer;
    public CmdHelp() {
        super(false, true, true, "wolfird help","查看Wolfird框架插件帮助");
    }

    @Override
    protected void execute(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage("§8[ "+notifier.getPrefix()+" §8]");
        sender.sendMessage(" ");
        for (WolfirdCommand command : commandContainer.getCommands()) {
            sender.sendMessage("§f"+command.getCommandTemplate()+" §8- §7"+command.getHelpMessage());
        }
        sender.sendMessage(" ");
    }
}
