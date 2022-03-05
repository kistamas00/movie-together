package org.kis.movietogether.model.websocket.user;

import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

public class User {

    private String userName;
    private WebSocketSession session;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    public User(WebSocketSession session) {
        this.session = session;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public WebSocketSession getSession() {
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(session, user.session) && Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, userName);
    }

    @Override
    public String toString() {

        return "User{" +
                "userName='" + userName + '\'' +
                ", session=" + session +
                '}';
    }
}
