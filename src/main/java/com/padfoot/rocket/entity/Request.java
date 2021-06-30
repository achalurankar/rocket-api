package com.padfoot.rocket.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    int userId;
    int friendId;
    boolean accepted;
}
