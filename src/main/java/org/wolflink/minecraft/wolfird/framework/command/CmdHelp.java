package org.wolflink.minecraft.wolfird.framework.command;


import org.bukkit.command.CommandSender;
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdCommand;
import org.wolflink.minecraft.wolfird.framework.container.CommandContainer;
import org.wolflink.common.ioc.Inject;
import org.wolflink.minecraft.wolfird.framework.notifier.FrameworkNotifier;

public class CmdHelp extends WolfirdCommand {
    @Inject
    private FrameworkNotifier notifier;
    @Inject
    private CommandContainer commandContainer;

    private final String mainCommand;
    public CmdHelp(String mainCommand) {
        super(false, true, true, mainCommand+" help", "查看 "+mainCommand+" 插件帮助");
        this.mainCommand = mainCommand;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        sender.sendMessage(" ");
        sender.sendMessage("§8[ §a" + mainCommand + " §8] §f指令帮助");
        sender.sendMessage(" ");
        for (WolfirdCommand command : commandContainer.getCommands()) {
            if(command.getCommandTemplate().startsWith(mainCommand)) {
                sender.sendMessage("§f  " + command.getCommandTemplate() + " §8- §7" + command.getHelpMessage());
            }
        }
        sender.sendMessage(" ");
    }
}
