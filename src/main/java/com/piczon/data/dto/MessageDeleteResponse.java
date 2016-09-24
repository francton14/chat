package com.piczon.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by franc on 9/20/2016.
 */
public class MessageDeleteResponse {

    @JsonProperty
    private Long messageId;

    @JsonProperty
    private String status;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
