package com.francobm.bungeestaff.commands;

import com.francobm.bungeestaff.BungeeStaff;
import com.francobm.bungeestaff.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCommand extends Command {
    private final BungeeStaff plugin;

    public ServerCommand(BungeeStaff plugin) {
        super("staffserver", "", "sserver");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            if(args.length >= 1) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                ServerInfo serverInfo = plugin.getProxy().getServerInfo(args[0]);
                if(serverInfo == null) return;
                if(player.getServer().getInfo().getName().equalsIgnoreCase(serverInfo.getName())) return;
                player.sendMessage(Utils.Text(plugin.getMessage("staff.server.connect", true).replace("%server%", serverInfo.getName()).replace("%prefix%", plugin.namePlugin)));
                player.connect(serverInfo);
            }
        }
    }
}
