package com.padfoot.rocket.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private int userId;
    private String username;
    private String password;
    private String emailId;
    private String picture;
    private String lastSeen;
    private long pictureVersion;
}
