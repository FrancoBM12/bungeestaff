package com.francobm.bungeestaff.cache;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerData {
    private final ProxiedPlayer player;
    private boolean chat;
    private boolean chatMessages;
    private boolean reportNotify;
    private boolean requestNotify;
    private String server;

    public PlayerData(ProxiedPlayer player, String server){
        this.player = player;
        this.server = server;
        this.chat = false;
        this.chatMessages = true;
        this.reportNotify = true;
        this.requestNotify = true;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }

    public void setReportNotify(boolean reportNotify) {
        this.reportNotify = reportNotify;
    }

    public boolean isChat() {
        return chat;
    }

    public void toggleChat(){
        chat = !chat;
    }

    public void setRequestNotify(boolean requestNotify) {
        this.requestNotify = requestNotify;
    }

    public boolean isRequestNotify() {
        return requestNotify;
    }

    public void toggleRequestNotify(){
        requestNotify = !requestNotify;
    }

    public boolean isChatMessages() {
        return chatMessages;
    }

    public void toggleChatMessages(){
        chatMessages = !chatMessages;
    }

    public void setChatMessages(boolean chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void toggleReportNotify(){
        reportNotify = !reportNotify;
    }

    public boolean isReportNotify() {
        return reportNotify;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}