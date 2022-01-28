package com.francobm.bungeestaff.commands;

import com.francobm.bungeestaff.BungeeStaff;
import com.francobm.bungeestaff.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {
    private final BungeeStaff plugin;

    public ReportCommand(BungeeStaff plugin) {
        super("report", "", "reportar");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(args.length == 1){
                switch (args[0].toLowerCase()){
                    case "see":
                    case "ver":
                    case "list":
                    case "lista":
                        plugin.getBungeeStaffManager().seeReports(player);
                        break;
                }
            }
            if(args.length == 2) {
                plugin.getBungeeStaffManager().reportPlayer(player, args[0], args[1]);
                return;
            }
            player.sendMessage(Utils.Text(plugin.getMessage("report.usage", true).replace("%prefix%", plugin.namePlugin)));
        }
    }
}
