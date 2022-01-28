package com.francobm.bungeestaff.commands;

import com.francobm.bungeestaff.BungeeStaff;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AlertsCommand extends Command {
    private final BungeeStaff plugin;

    public AlertsCommand(BungeeStaff plugin) {
        super("togglealerts", "", "talerts", "togglea", "alertas");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(args.length == 1){
                switch (args[0].toLowerCase()){
                    case "staffchat":
                        plugin.getBungeeStaffManager().toggleChatMessages(player);
                        break;
                    case "reports":
                    case "reportes":
                        plugin.getBungeeStaffManager().toggleReportNotify(player);
                        break;
                    case "requests":
                    case "helpops":
                        plugin.getBungeeStaffManager().toggleRequestNotify(player);
                        break;
                    default:
                        plugin.getBungeeStaffManager().sendHelp(player);
                        break;
                }
            }
        }
    }
}
