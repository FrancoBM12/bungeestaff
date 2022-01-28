package com.francobm.bungeestaff.cache;

public class ReportCache {
    private final String player;
    private final String reported;
    private final String reason;
    private final String server;

    public ReportCache(String player, String reported, String reason, String server){
        this.player = player;
        this.reported = reported;
        this.reason = reason;
        this.server = server;
    }

    public String getPlayer() {
        return player;
    }

    public String getReported() {
        return reported;
    }

    public String getReason() {
        return reason;
    }

    public String getServer() {
        return server;
    }
}
