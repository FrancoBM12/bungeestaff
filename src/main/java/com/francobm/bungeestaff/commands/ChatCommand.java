package com.francobm.bungeestaff.commands;

import com.francobm.bungeestaff.BungeeStaff;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatCommand extends Command {
    private final BungeeStaff plugin;

    public ChatCommand(BungeeStaff plugin) {
        super("staffchat", "", "staffc", "cstaff", "sc");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            // /sc asd asd asd
            if(args.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for(String arg : args){
                    stringBuilder.append(arg);
                    if(!args[args.length-1].equalsIgnoreCase(arg)){
                        stringBuilder.append(" ");
                    }
                }
                plugin.getBungeeStaffManager().chatStaff(player, stringBuilder.toString());
                return;
            }
            plugin.getBungeeStaffManager().toggleChat(player);
        }
    }

}
