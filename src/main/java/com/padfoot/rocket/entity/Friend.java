package com.padfoot.rocket.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friend {
    private int userId;
    private String username;
    private String picture;
    private long pictureVersion;
    private RecentMessage recentMessage;

    @Getter
    @Setter
    public static class RecentMessage {
        private int senderId;
        private int receiverId;
        private String text;
        private int unseenCount;
        private String dateSent;
        private boolean seen;
    }
}
