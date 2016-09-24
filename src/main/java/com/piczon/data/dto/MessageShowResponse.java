package com.piczon.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by franc on 9/19/2016.
 */
public class MessageShowResponse {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String text;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a", timezone = "Asia/Shanghai")
    private Date timeStamp;

    @JsonProperty
    private Boolean isOwner;

    @JsonProperty
    private UserShow user;

    @JsonProperty
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public UserShow getUser() {
        return user;
    }

    public void setUser(UserShow user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
