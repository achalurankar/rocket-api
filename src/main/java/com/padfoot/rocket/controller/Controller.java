package com.padfoot.rocket.controller;

import com.padfoot.rocket.entity.Message;
import com.padfoot.rocket.entity.Request;
import com.padfoot.rocket.entity.User;
import com.padfoot.rocket.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for endpoints.
 */
@RestController
@RequestMapping("/api/v1")
public class Controller {

    @Autowired
    ChatService chatService;

    @GetMapping(value = "/login/{username}/{password}")
    public ResponseEntity<?> login(
            @PathVariable(value = "username") String username,
            @PathVariable(value = "password") String password) throws Exception {
        List<User> user = chatService.login(username, password);
        if (user.size() != 0) {
            return new ResponseEntity<>(user.get(0), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(chatService.register(user), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<?> getUsers(
            @PathVariable(value = "userId") int userId) throws Exception {
        return new ResponseEntity<>(chatService.getUsers(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/message")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) throws Exception {
        return new ResponseEntity<>(chatService.sendMessage(message), HttpStatus.OK);
    }

    @GetMapping(value = "/message/{senderId}/{receiverId}")
    public ResponseEntity<?> getMessages(
            @PathVariable(value = "senderId") int senderId,
            @PathVariable(value = "receiverId") int receiverId) throws Exception {
        return new ResponseEntity<>(chatService.getMessages(senderId, receiverId), HttpStatus.OK);
    }

    @PostMapping(value = "/request")
    public ResponseEntity<?> postRequest(@RequestBody Request request) throws Exception {
        return new ResponseEntity<>(chatService.sendRequest(request), HttpStatus.OK);
    }

    @GetMapping(value = "/request/{userId}")
    public ResponseEntity<?> getRequests(
            @PathVariable(value = "userId") int userId) throws Exception {
        return new ResponseEntity<>(chatService.getRequests(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/request/respond")
    public ResponseEntity<?> respondToRequest(@RequestBody Request request) throws Exception {
        return new ResponseEntity<>(chatService.respondToRequest(request), HttpStatus.OK);
    }

    @GetMapping(value = "/friends/{userId}")
    public ResponseEntity<?> getFriends(
            @PathVariable(value = "userId") int userId) throws Exception {
        return new ResponseEntity<>(chatService.getFriends(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/friends/remove")
    public ResponseEntity<?> removeFriend(@RequestBody Request request) throws Exception {
        return new ResponseEntity<>(chatService.removeFriend(request), HttpStatus.OK);
    }

    @GetMapping(value = "/message/listener/{userId}/{friendId}")
    public ResponseEntity<?> messageListener(
            @PathVariable(value = "userId") int userId,
            @PathVariable(value = "friendId") int friend_id) throws Exception {
        return new ResponseEntity<>(chatService.messageListener(userId, friend_id), HttpStatus.OK);
    }

    @PostMapping(value = "/seen")
    public ResponseEntity<?> setSeen(@RequestBody Request request) throws Exception {
        return new ResponseEntity<>(chatService.setSeen(request), HttpStatus.OK);
    }

}