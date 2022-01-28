package com.francobm.bungeestaff.commands;

import com.francobm.bungeestaff.BungeeStaff;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BStaffCommand extends Command {
    private final BungeeStaff plugin;
    public BStaffCommand(BungeeStaff plugin) {
        super("bungeestaff", "", "bstaff", "bungees");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxyServer){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    plugin.getBungeeStaffManager().reload();
                    return;
                }
            }
        }
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(args.length >= 1){
                switch (args[0].toLowerCase()){
                    case "help":
                        plugin.getBungeeStaffManager().sendHelp(player);
                        break;
                    case "reload":
                        plugin.getBungeeStaffManager().reload(player);
                        break;
                    case "mping":
                        if(args.length < 2) return;
                        plugin.getBungeeStaffManager().sendPingList(player, args[1]);
                        break;
                }
                return;
            }
            plugin.getBungeeStaffManager().sendHelp(player);
        }
    }
}
