package org.kis.movietogether.model.websocket.message;

import java.util.Objects;

public class UserDetailsMessage extends AbstractMessage {

    private String userName;

    public UserDetailsMessage() {
        super(MessageType.USER_DETAILS);
    }

    public String getUserName() {
        return this.userName;
    }

    public UserDetailsMessage setUserName(final String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsMessage that = (UserDetailsMessage) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
