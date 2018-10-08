package io.pivotal.workshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RequestHeader {


    String channel;

    String terminal;

    @JsonProperty(value = "user_id", required = true)
    String userId;


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
