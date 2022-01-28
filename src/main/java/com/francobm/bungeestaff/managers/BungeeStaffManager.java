package com.francobm.bungeestaff.managers;

import com.francobm.bungeestaff.BungeeStaff;
import com.francobm.bungeestaff.cache.PlayerData;
import com.francobm.bungeestaff.cache.ReportCache;
import com.francobm.bungeestaff.cache.RequestCache;
import com.francobm.bungeestaff.utils.Utils;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.ArrayList;
import java.util.List;

public class BungeeStaffManager {
    private final BungeeStaff plugin;

    public BungeeStaffManager(BungeeStaff plugin){
        this.plugin = plugin;
    }

    public void reportPlayer(ProxiedPlayer player, String targetName, String reason){
        if(!player.hasPermission("bstaff.report")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        if(player.getName().equalsIgnoreCase(targetName)){
            player.sendMessage(Utils.Text(plugin.getMessage("report.same-player", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        ProxiedPlayer target = plugin.getProxy().getPlayer(targetName);
        if(target == null){
            player.sendMessage(Utils.Text(plugin.getMessage("report.offline-target", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        if(target.hasPermission("bstaff.report.bypass")){
            player.sendMessage(Utils.Text(plugin.getMessage("report.bypass", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        List<String> reports = new ArrayList<>();
        for(String report : plugin.getConfig().getConfiguration().getStringList("allow-reports")){
            reports.add(report.toLowerCase());
        }
        if(!reports.contains(reason.toLowerCase())){
            player.sendMessage(Utils.Text(plugin.getMessage("report.other-reason", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        ReportCache reportCache = plugin.addReport(new ReportCache(player.getName(), targetName, reason, target.getServer().getInfo().getName()));


        for(PlayerData playerData : plugin.getPlayers()){
            if(playerData == null) continue;
            if(!playerData.isReportNotify()) continue;
            sendReportNotify(playerData.getPlayer(), reportCache);
        }
    }

    public void answerRequest(ProxiedPlayer player, String targetName, String answer){
        if(!player.hasPermission("bstaff.request.answer")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        if(player.getName().equalsIgnoreCase(targetName)){
            player.sendMessage(Utils.Text(plugin.getMessage("request.answer.same-player", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        RequestCache requestCache = plugin.getRequest(targetName);
        if(requestCache == null) {
            player.sendMessage(Utils.Text(plugin.getMessage("request.answer.no-exist", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        if(!requestCache.getAnswer().isEmpty()){
            player.sendMessage(Utils.Text(plugin.getMessage("request.answer.already", true).replace("%prefix%", plugin.namePlugin).replace("%author%", requestCache.getAnswerAuthor())));
            return;
        }
        requestCache.setAnswerAuthor(player.getName());
        requestCache.setAnswer(answer);
        ProxiedPlayer target = plugin.getProxy().getPlayer(targetName);
        for(PlayerData playerData : plugin.getPlayers()){
            if(playerData == null) continue;
            if(!playerData.isRequestNotify()) continue;
            playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("request.answer.others-success", true).replace("%prefix%", plugin.namePlugin).replace("%author%", player.getName()).replace("%player%", targetName)));
        }
        if(target == null) return;
        for(String format : plugin.getConfig().getConfiguration().getStringList("messages.request.answer.format")){
            target.sendMessage(Utils.Text(format.replace("%prefix%", plugin.namePlugin).replace("%author%", player.getName()).replace("%answer%", answer)));
        }
    }

    public void request(ProxiedPlayer player, String reason){
        if(!player.hasPermission("bstaff.request")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }

        RequestCache requestCache = plugin.addRequest(new RequestCache(player.getName(), reason, player.getServer().getInfo().getName()));


        for(PlayerData playerData : plugin.getPlayers()){
            if(playerData == null) continue;
            if(!playerData.isRequestNotify()) continue;
            sendRequestNotify(playerData.getPlayer(), requestCache);
        }
    }

    public void disconnectServer(ProxiedPlayer player, String serverName){
        if(!player.hasPermission("bstaff.use")) {
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null){
            //plugin.getLogger().info("Disconnect - PlayerData Null");
            return;
        }
        plugin.removePlayer(player.getName());
        //plugin.getLogger().info("Disconnect - Success");
        for(PlayerData pData : plugin.getPlayers()){
            if(pData == null) continue;
            if(!pData.getPlayer().isConnected()) continue;
            if(pData.getPlayer().getName().equalsIgnoreCase(player.getName())) continue;
            pData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("staff.server.quit", true).replace("%player%", player.getName()).replace("%prefix%", plugin.namePlugin)));
        }
    }

    public void connectServer(ProxiedPlayer player, String serverName){
        if(!player.hasPermission("bstaff.use")) {
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null){
            playerData = new PlayerData(player, serverName);
            plugin.addPlayer(playerData);
            //plugin.getLogger().info("Connect Server - new Player");
        }
        if(playerData.isReportNotify()){
            seeReports(player);
        }
        if(playerData.isRequestNotify()){
            seeRequests(player);
        }
        playerData.setServer(serverName);
        //plugin.getLogger().info("Connect - Success");
        for(PlayerData pData : plugin.getPlayers()){
            if(pData == null) continue;
            if(!pData.getPlayer().isConnected()) continue;
            if(pData.getPlayer().getName().equalsIgnoreCase(player.getName())) continue;
            pData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("staff.server.join", true).replace("%player%", player.getName()).replace("%prefix%", plugin.namePlugin)));
        }
    }

    public void chatStaff(ChatEvent event){
        if(event.isCommand()) return;
        if(event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            if (!player.hasPermission("bstaff.use")) {
                return;
            }
            PlayerData playerData = plugin.getPlayer(player.getName());
            if (playerData == null) {
                //plugin.getLogger().info("ChatStaff - PlayerData Null");
                return;
            }
            if(!playerData.isChat()) return;
            //plugin.getLogger().info("ChatStaff - Success");
            String format = plugin.getMessage("staff.chat.format", true).replace("%server%", player.getServer().getInfo().getName()).replace("%player%", player.getName()).replace("%message%", event.getMessage()).replace("%prefix%", plugin.namePlugin);
            event.setCancelled(true);
            for(PlayerData pData : plugin.getPlayers()){
                if(!pData.isChatMessages()) continue;
                pData.getPlayer().sendMessage(Utils.Text(format.replace("%prefix%", plugin.namePlugin)));
            }
        }
    }

    public void chatStaff(ProxiedPlayer player, String message){
        if (!player.hasPermission("bstaff.use")) {
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if (playerData == null) {
            plugin.addPlayer(new PlayerData(player, player.getServer().getInfo().getName()));
            //plugin.getLogger().info("ChatStaff No Event - PlayerData Null");
            return;
        }
        String format = plugin.getMessage("staff.chat.format", true).replace("%server%", player.getServer().getInfo().getName()).replace("%player%", player.getName()).replace("%message%", message).replace("%prefix%", plugin.namePlugin);
        for(PlayerData pData : plugin.getPlayers()){
            if(!pData.isChatMessages()) continue;
            pData.getPlayer().sendMessage(Utils.Text(format.replace("%prefix%", plugin.namePlugin)));
        }
    }

    public void seeReports(ProxiedPlayer player){
        for(ReportCache reportCache : plugin.getReports()){
            if(reportCache == null) continue;
            sendReportNotify(player, reportCache);
        }
    }

    public void seeRequests(ProxiedPlayer player){
        for(RequestCache requestCache : plugin.getRequests()){
            if(requestCache == null) continue;
            sendRequestNotify(player, requestCache);
        }
    }

    public void sendReportNotify(ProxiedPlayer player, ReportCache reportCache){
        List<Content> content = new ArrayList<>();
        for(String hover : plugin.getConfig().getConfiguration().getStringList("messages.report.notify.hover")){
            content.add(new Text(Utils.ChatColor(hover)));
        }
        ComponentBuilder componentBuilder = new ComponentBuilder("");
        for(String format : plugin.getConfig().getConfiguration().getStringList("messages.report.notify.clickable")){
            format = format.replace("%player%", reportCache.getPlayer()).replace("%reported%", reportCache.getReported()).replace("%reason%", reportCache.getReason()).replace("%prefix%", plugin.namePlugin);
            if(format.contains(reportCache.getReported())){
                componentBuilder.append(Utils.Text(format)).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/staffserver " + reportCache.getServer())).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, content));
                continue;
            }
            componentBuilder.append(Utils.Text(format)).event((HoverEvent) null);
        }
        BaseComponent[] baseComponents = componentBuilder.create();
        player.sendMessage(baseComponents);
    }

    public void sendRequestNotify(ProxiedPlayer player, RequestCache requestCache){
        List<Content> content = new ArrayList<>();
        for(String hover : plugin.getConfig().getConfiguration().getStringList("messages.request.notify.hover")){
            content.add(new Text(Utils.ChatColor(hover)));
        }
        ComponentBuilder componentBuilder = new ComponentBuilder("");
        for(String format : plugin.getConfig().getConfiguration().getStringList("messages.request.notify.clickable")){
            format = format.replace("%player%", requestCache.getPlayer()).replace("%reason%", requestCache.getReason()).replace("%prefix%", plugin.namePlugin);
            if(format.contains(requestCache.getPlayer())){
                componentBuilder.append(Utils.Text(format)).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/staffserver " + requestCache.getServer())).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, content));
                continue;
            }
            componentBuilder.append(Utils.Text(format)).event((HoverEvent) null);
        }
        BaseComponent[] baseComponents = componentBuilder.create();
        player.sendMessage(baseComponents);
    }

    public void switchServer(ProxiedPlayer player, String serverName){
        if(!player.hasPermission("bstaff.use")){
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if (playerData == null) {
            //plugin.getLogger().info("Switch - PlayerData Null");
            return;
        }

        playerData.setServer(serverName);

        ComponentBuilder componentBuilder = new ComponentBuilder("");
        List<Content> content = new ArrayList<>();
        for(String hover : plugin.getConfig().getConfiguration().getStringList("messages.staff.server.switch.hover")){
            content.add(new Text(Utils.ChatColor(hover)));
        }
        for(String format : plugin.getConfig().getConfiguration().getStringList("messages.staff.server.switch.clickable")){
            format = format.replace("%player%", player.getName()).replace("%server%", serverName).replace("%prefix%", plugin.namePlugin);
            if(format.contains(serverName)){
                componentBuilder.append(Utils.Text(format)).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/staffserver " + serverName)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, content));
                continue;
            }
            componentBuilder.append(Utils.Text(format)).event((HoverEvent) null);
        }
        BaseComponent[] baseComponents = componentBuilder.create();

        for(PlayerData pData : plugin.getPlayers()){
            if(pData == null) continue;
            if(!pData.getPlayer().isConnected()) continue;
            if(pData.getPlayer().getName().equalsIgnoreCase(player.getName())) continue;
            pData.getPlayer().sendMessage(baseComponents);
        }

    }

    public void toggleChat(ProxiedPlayer player){
        if(!player.hasPermission("bstaff.use")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null) {
            //plugin.getLogger().info("ToggleChat - PlayerData Null");
            return;
        }
        if(!playerData.isChatMessages()){
            playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("staff.chat.error-messages", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        playerData.toggleChat();
        if(playerData.isChat()){
            playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("staff.chat.activated", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("staff.chat.deactivated", true).replace("%prefix%", plugin.namePlugin)));
    }

    public void toggleChatMessages(ProxiedPlayer player){
        if(!player.hasPermission("bstaff.staffchat.notify")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null) {
            return;
        }
        playerData.toggleChatMessages();
        if(playerData.isChatMessages()){
            playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("staff.chat.messages.activated", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        if(playerData.isChat()){
            playerData.toggleChat();
        }
        playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("staff.chat.messages.deactivated", true).replace("%prefix%", plugin.namePlugin)));
    }

    public void toggleReportNotify(ProxiedPlayer player){
        if(!player.hasPermission("bstaff.report.notify")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null) {
            return;
        }
        playerData.toggleReportNotify();
        if(playerData.isReportNotify()){
            playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("report.notify.activated", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("report.notify.deactivated", true).replace("%prefix%", plugin.namePlugin)));
    }

    public void toggleRequestNotify(ProxiedPlayer player){
        if(!player.hasPermission("bstaff.request.notify")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        PlayerData playerData = plugin.getPlayer(player.getName());
        if(playerData == null) {
            return;
        }
        playerData.toggleRequestNotify();
        if(playerData.isRequestNotify()){
            playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("report.notify.activated", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        playerData.getPlayer().sendMessage(Utils.Text(plugin.getMessage("report.notify.deactivated", true).replace("%prefix%", plugin.namePlugin)));
    }

    public void reload(ProxiedPlayer player){
        if(!player.hasPermission("bstaff.reload")){
            player.sendMessage(Utils.Text(plugin.getMessage("no-permission", true).replace("%prefix%", plugin.namePlugin)));
            return;
        }
        //plugin.getLogger().info(Utils.ChatColor("BungeeStaff >> Plugin reloading.."));
        plugin.getConfig().reload();
        plugin.namePlugin = plugin.getMessage("prefix", true);
        player.sendMessage(Utils.Text(plugin.getMessage("reload", true).replace("%prefix%", plugin.namePlugin)));
        //plugin.getLogger().info("BungeeStaff " + plugin.getDescription().getVersion() + " has successfully reloaded!");
    }

    public void reload(){
        //plugin.getLogger().info(Utils.ChatColor("BungeeStaff >> Plugin reloading.."));
        plugin.getConfig().reload();
        plugin.namePlugin = plugin.getMessage("prefix", true);
        //plugin.getLogger().info("BungeeStaff " + plugin.getDescription().getVersion() + " has successfully reloaded!");
    }

    public void sendHelp(ProxiedPlayer player){
        for(String string : plugin.getConfig().getConfiguration().getStringList("messages.help")){
            player.sendMessage(Utils.Text(string));
        }
    }
    
    public void sendPingList(ProxiedPlayer player, String serverName){
        ServerInfo server = plugin.getProxy().getServerInfo(serverName);
        server.ping((result, error) -> {
            if (error != null) {
                player.sendMessage(Utils.Text("No tiene una descripcion!"));
                // Unable to get MotD from server, print this to the console for example
                return;
            }

            String motd = result.getDescriptionComponent().toString();
            player.sendMessage(Utils.Text(motd));
            // Do something
        });
    }
}
