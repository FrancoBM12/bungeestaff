package com.francobm.bungeestaff.commands;

import com.francobm.bungeestaff.BungeeStaff;
import com.francobm.bungeestaff.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RequestCommand extends Command {
    private final BungeeStaff plugin;

    public RequestCommand(BungeeStaff plugin) {
        super("request", "", "ayuda", "solicitar", "helpop");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(args.length >= 1){
                switch (args[0].toLowerCase()){
                    case "see":
                    case "ver":
                    case "list":
                    case "lista":
                        if(args.length == 1) {
                            plugin.getBungeeStaffManager().seeRequests(player);
                        }
                        return;
                    case "answer":
                    case "responder":
                    case "r":
                        // /request answer message
                        if(args.length == 1){
                            player.sendMessage(Utils.Text(plugin.getMessage("request.answer.usage", true).replace("%prefix%", plugin.namePlugin)));
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i = 1; i < args.length; i++){
                            stringBuilder.append(args[i]).append(" ");
                        }
                        plugin.getBungeeStaffManager().answerRequest(player, args[1], stringBuilder.toString());
                        return;
                }
            }
            if(args.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for(String arg : args){
                    stringBuilder.append(arg).append(" ");
                }
                plugin.getBungeeStaffManager().request(player, stringBuilder.toString());
                return;
            }
            player.sendMessage(Utils.Text(plugin.getMessage("request.usage", true).replace("%prefix%", plugin.namePlugin)));
        }
    }
}
