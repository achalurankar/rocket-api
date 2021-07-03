package com.padfoot.rocket.query;

import com.padfoot.rocket.entity.Message;
import com.padfoot.rocket.entity.User;
import org.springframework.jdbc.core.RowMapper;

/**
 * Query class to store database queries for service
 */
public class Query {

    public String loginQuery() {
        return "select * from users where username = :username and password = :password";
    }

    public String registerQuery() {
        return "insert into users(username, password, email_id, picture, picture_version)" +
                " values(:username, :password, :email_id, :picture, :picture_version)";
    }

    public String getUsersQuery() {
        return "select * from users where user_id != :user_id";
    }

    public String sendMessageQuery() {
        String messageId = System.currentTimeMillis() + "";
        return "insert into chat_logs" +
                "(message_id, conversation_id, sender_id, receiver_id, picture, text, type, seen, reply_text, reply_owner, date_sent, date_updated)" +
                " values " +
                "(:message_id, :conversation_id_1, :sender_id, :receiver_id, :picture, :text, :type, false, :reply_text, :reply_owner, now(), now())" +
                ", " +
                "(:message_id, :conversation_id_2, :sender_id, :receiver_id, :picture, :text, :type, false, :reply_text, :reply_owner, now(), now())";
    }

    public String getMessagesQuery() {
        return "select * from chat_logs" +
                " where conversation_id = :conversation_id" +
                " order by date_sent desc";
    }

    public RowMapper<Message> GET_MESSAGES = (rs, rowNum) -> {
        Message message = new Message();
        message.setMessageId(rs.getString("message_id"));
        message.setConversationId(rs.getString("conversation_id"));
        message.setSenderId(rs.getInt("sender_id"));
        message.setReceiverId(rs.getInt("receiver_id"));
        message.setPicture(rs.getString("picture"));
        message.setText(rs.getString("text"));
        message.setType(rs.getString("type"));
        message.setSeen(rs.getBoolean("seen"));
        message.setReplyText(rs.getString("reply_text"));
        message.setReplyOwner(rs.getString("reply_owner"));
        message.setDateSent(rs.getString("date_sent"));
        message.setDateUpdated(rs.getString("date_updated"));
        return message;
    };

    public String sendRequestQuery() {
        return "insert into requests(request_from, request_to)" +
                " values(:user_id, :friend_id)";
    }

    public String getRequestsQuery() {
        return "select ut.user_id, ut.username, ut.email_id, ut.picture, ut.picture_version from" +
                " users ut" +
                " inner join requests rt on ut.user_id = rt.request_from" +
                " where rt.request_to = :user_id;";
    }

    public RowMapper<User> GET_USERS_MAPPER = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmailId(rs.getString("email_id"));
        user.setPicture(rs.getString("picture"));
        user.setPictureVersion(rs.getLong("picture_version"));
        return user;
    };

    public String respondToRequestQuery(boolean accepted) {
        String sql = "delete from requests where request_from = :friend_id and request_to = :user_id;";
        if (accepted)
            sql += " insert into friends(user_id, friend_id)" +
                    " values(:user_id, :friend_id)," +
                    " (:friend_id, :user_id);";
        return sql;
    }

    public String getFriendsQuery() {
        return "select ut.user_id, ut.username, ut.email_id, ut.picture, ut.picture_version from" +
                " users ut" +
                " inner join friends ft on ut.user_id = ft.friend_id" +
                " where ft.user_id = :user_id;";
    }

    public String removeFriendQuery() {
        return "delete from friends where user_id = :user_id and friend_id = :friend_id;" +
                "delete from friends where user_id = :friend_id and friend_id = :user_id;";
    }

    public String setSeenQuery() {
        return "update chat_logs " +
                "set seen=true, date_updated=now() " +
                "where " +
                "(conversation_id = :conversation_id_1 or conversation_id = :conversation_id_2) " +
                "and " +
                "sender_id=:friend_id " +
                "and " +
                "seen=false";
    }
}
