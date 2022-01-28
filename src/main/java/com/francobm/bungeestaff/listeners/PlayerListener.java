package com.francobm.bungeestaff.listeners;

import com.francobm.bungeestaff.BungeeStaff;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
    private final BungeeStaff plugin;

    public PlayerListener(BungeeStaff plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnected(PostLoginEvent event){
        plugin.getBungeeStaffManager().connectServer(event.getPlayer(), "Login");
    }

    @EventHandler
    public void onChat(ChatEvent event){
        plugin.getBungeeStaffManager().chatStaff(event);
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event){
        plugin.getBungeeStaffManager().disconnectServer(event.getPlayer(), "Disconnect");
    }

    @EventHandler
    public void onServerSwitch(ServerConnectedEvent event){
        plugin.getBungeeStaffManager().switchServer(event.getPlayer(), event.getServer().getInfo().getName());
    }
}
