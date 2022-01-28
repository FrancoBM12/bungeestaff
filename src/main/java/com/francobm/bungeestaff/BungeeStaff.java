package com.francobm.bungeestaff;

import com.francobm.bungeestaff.cache.PlayerData;
import com.francobm.bungeestaff.cache.ReportCache;
import com.francobm.bungeestaff.cache.RequestCache;
import com.francobm.bungeestaff.commands.*;
import com.francobm.bungeestaff.files.FileCreator;
import com.francobm.bungeestaff.listeners.PlayerListener;
import com.francobm.bungeestaff.managers.BungeeStaffManager;
import com.francobm.bungeestaff.utils.Utils;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class BungeeStaff extends Plugin {

    private BungeeStaffManager bungeeStaffManager;
    public String namePlugin;
    private FileCreator config;
    private List<ReportCache> reports;
    private List<PlayerData> players;
    private List<RequestCache> requests;
    @Override
    public void onEnable() {
        // Plugin startup logic
        bungeeStaffManager = new BungeeStaffManager(this);
        config = new FileCreator(this, "config");
        namePlugin = config.getConfiguration().getString("messages.prefix");
        registerCommands();
        registerListeners();
        players = new ArrayList<>();
        reports = new ArrayList<>();
        requests = new ArrayList<>();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PlayerData getPlayer(String name){
        for(PlayerData playerData : players){
            if(playerData.getPlayer().getName().equalsIgnoreCase(name)){
                return playerData;
            }
        }
        return null;
    }

    public RequestCache addRequest(RequestCache requestCache){
        this.requests.add(requestCache);
        return requestCache;
    }

    public RequestCache getRequest(String name){
        for(RequestCache requestCache : requests){
            if(requestCache.getPlayer().equalsIgnoreCase(name)){
                return requestCache;
            }
        }
        return null;
    }

    public void addPlayer(PlayerData playerData){
        this.players.add(playerData);
    }

    public void removePlayer(String name){
        players.removeIf(playerData -> playerData.getPlayer().getName().equalsIgnoreCase(name));
    }

    public ReportCache getReport(String name){
        for(ReportCache reportCache : reports){
            if(reportCache.getReported().equalsIgnoreCase(name)){
                return reportCache;
            }
        }
        return null;
    }

    public ReportCache addReport(ReportCache reportCache){
        this.reports.add(reportCache);
        return reportCache;
    }

    public String getMessage(String path, boolean live){
        if(live){
            if(getConfig().getConfiguration().getString("messages."+path) == null){
                return Utils.ChatColor(namePlugin + "&cError path Message Not Found");
            }else{
                return Utils.ChatColor(getConfig().getConfiguration().getString("messages."+path));
            }
        }else{
            if(getConfig().getConfiguration().getString(path) == null) {
                return Utils.ChatColor(namePlugin + "&cError path Not Found");
            }else{
                return Utils.ChatColor(getConfig().getConfiguration().getString(path));
            }
        }
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

    public void registerListeners() {
        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
    }
    public void registerCommands(){
        getProxy().getPluginManager().registerCommand(this, new ReportCommand(this));
        getProxy().getPluginManager().registerCommand(this, new ChatCommand(this));
        getProxy().getPluginManager().registerCommand(this, new ServerCommand(this));
        getProxy().getPluginManager().registerCommand(this, new AlertsCommand(this));
        getProxy().getPluginManager().registerCommand(this, new BStaffCommand(this));
        getProxy().getPluginManager().registerCommand(this, new RequestCommand(this));
    }

    public BungeeStaffManager getBungeeStaffManager() {
        return bungeeStaffManager;
    }

    public FileCreator getConfig() {
        return config;
    }

    public List<ReportCache> getReports() {
        return reports;
    }

    public List<RequestCache> getRequests() {
        return requests;
    }
}
