package com.padfoot.rocket.service;

import com.padfoot.rocket.entity.*;
import com.padfoot.rocket.query.Query;
import com.padfoot.rocket.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Chat service class contains all methods for controller
 */
@Service
public class ChatService extends Query {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Login method
     */
    public List<User> login(String username, String password) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("username", username);
        parameters.addValue("password", password);
        String sql = loginQuery();
        return jdbcTemplate.query(sql, parameters, GET_USERS_MAPPER);
    }

    /**
     * Register new user
     * TODO check if username exists
     */
    public String register(User user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("username", user.getUsername());
        parameters.addValue("password", user.getPassword());
        parameters.addValue("email_id", user.getEmailId());
        parameters.addValue("picture", user.getPicture());
        parameters.addValue("picture_version", user.getPictureVersion());
        String sql = registerQuery();
        int result = jdbcTemplate.update(sql, parameters);
        if (result > 0) {
            return "Register Successful";
        } else {
            return "Register Failed";
        }
    }

    /**
     * To send message from one user to other
     */
    public String sendMessage(Message message) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String messageId = System.currentTimeMillis() + "";
        parameters.addValue("message_id", messageId);
        parameters.addValue("conversation_id_1", message.getSenderId() + "/" + message.getReceiverId());
        parameters.addValue("conversation_id_2", message.getReceiverId() + "/" + message.getSenderId());
        parameters.addValue("sender_id", message.getSenderId());
        parameters.addValue("receiver_id", message.getReceiverId());
        parameters.addValue("picture", message.getPicture());
        parameters.addValue("text", message.getText());
        parameters.addValue("type", message.getType());
        parameters.addValue("reply_text", message.getReplyText());
        parameters.addValue("reply_owner", message.getReplyOwner());
        String sql = sendMessageQuery();
        int result = jdbcTemplate.update(sql, parameters);
        if (result > 0) {
            return Constants.SUCCESS;
        } else {
            return Constants.FAILED;
        }
    }

    /**
     * Get all messages for a particular user
     */
    public List<Message> getMessages(int senderId, int receiverId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        List<Message> messages;
        parameters.addValue("conversation_id", senderId + "/" + receiverId);
        String sql = getMessagesQuery();
        messages = jdbcTemplate.query(sql, parameters, GET_MESSAGES);
        return messages;
    }

    /**
     * Send request to one of the registered user
     */
    public String sendRequest(Request request) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", request.getUserId());
        parameters.addValue("friend_id", request.getFriendId());
        String sql = sendRequestQuery();
        int result = jdbcTemplate.update(sql, parameters);
        if (result > 0) {
            return Constants.SUCCESS;
        } else {
            return Constants.FAILED;
        }
    }

    /**
     * Get all received requests for a user
     */
    public List<User> getRequests(int userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", userId);
        String sql = getRequestsQuery();
        return jdbcTemplate.query(sql, parameters, GET_USERS_MAPPER);
    }


    /**
     * Respond to request either accept or reject
     */
    public String respondToRequest(Request request) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", request.getUserId());
        parameters.addValue("friend_id", request.getFriendId());
        String sql = respondToRequestQuery(request.isAccepted());
        int result = jdbcTemplate.update(sql, parameters);
        if (result > 0) {
            return Constants.SUCCESS;
        } else {
            return Constants.FAILED;
        }
    }

    /**
     * Get list of friends for a user
     */
    public List<Friend> getFriends(int userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", userId);
        String sql = getFriendsQuery();
        //get friend list
        List<Friend> friends = jdbcTemplate.query(sql, parameters, GET_FRIENDS_MAPPER);
        for (Friend friend : friends) {
            parameters.addValue("conversation_id", userId + "/" + friend.getUserId());
            sql = getRecentMessageQuery();
            //get recent message with particular friend
            List<Friend.RecentMessage> recentMessage = jdbcTemplate.query(sql, parameters, GET_RECENT_MESSAGE_MAPPER);
            if (recentMessage.size() != 0) {
                //attach recent message to friend list
                friend.getRecentMessage().setText(recentMessage.get(0).getText());
                friend.getRecentMessage().setDateSent(recentMessage.get(0).getDateSent());
                friend.getRecentMessage().setSenderId(recentMessage.get(0).getSenderId());
                friend.getRecentMessage().setSeen(recentMessage.get(0).isSeen());
            } else
                //no messages for this chat
                friend.setRecentMessage(null);
        }
        return friends;
    }

    /**
     * Remove friend from friend list of a user
     */
    public String removeFriend(Request request) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", request.getUserId());
        parameters.addValue("friend_id", request.getFriendId());
        String sql = removeFriendQuery();
        int result = jdbcTemplate.update(sql, parameters);
        if (result > 0) {
            return Constants.SUCCESS;
        } else {
            return Constants.FAILED;
        }
    }

    /**
     * Get all the users except the user's who made request
     */
    public Object getUsers(int userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", userId);
        String sql = getUsersQuery();
        return jdbcTemplate.query(sql, parameters, GET_USERS_MAPPER);
    }

    public String setSeen(Request request) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", request.getUserId());
        parameters.addValue("friend_id", request.getFriendId());
        parameters.addValue("conversation_id_1", request.getUserId() + "/" + request.getFriendId());
        parameters.addValue("conversation_id_2", request.getFriendId() + "/" + request.getUserId());
        String sql = setSeenQuery();
        int result = jdbcTemplate.update(sql, parameters);
        return Constants.SUCCESS;
    }

    public String setUserStatus(UserStatus userStatus) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", userStatus.getUserId());
        parameters.addValue("status", userStatus.getStatus());
        String sql = setUserStatusQuery();
        int result = jdbcTemplate.update(sql, parameters);
        return Constants.SUCCESS;
    }

    public String getUserStatus(int userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", userId);
        String sql = getUserStatusQuery();
        List<String> result = jdbcTemplate.query(sql, parameters, LAST_SEEN_MAPPER);
        return result.size() == 0 ? "" : result.get(0);
    }
}