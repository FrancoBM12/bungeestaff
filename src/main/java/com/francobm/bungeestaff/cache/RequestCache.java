package com.francobm.bungeestaff.cache;

public class RequestCache {
    private final String player;
    private final String reason;
    private final String server;
    private String answer;
    private String answerAuthor;

    public RequestCache(String player, String reason, String server){
        this.player = player;
        this.reason = reason;
        this.server = server;
        this.answer = "";
        this.answerAuthor = "";
    }

    public String getPlayer() {
        return player;
    }

    public String getReason() {
        return reason;
    }

    public String getServer() {
        return server;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerAuthor() {
        return answerAuthor;
    }

    public void setAnswerAuthor(String answerAuthor) {
        this.answerAuthor = answerAuthor;
    }
}
