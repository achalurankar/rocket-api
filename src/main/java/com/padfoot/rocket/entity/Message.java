package com.padfoot.rocket.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    public String messageId;
    public String conversationId;
    public int senderId;
    public int receiverId;
    public String picture;
    public String text;
    public String type;
    public boolean seen;
    public String replyText;
    public String replyOwner;
    public String dateSent;
    public String dateUpdated;
}
