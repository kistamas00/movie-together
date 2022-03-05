package org.kis.movietogether.model.websocket.message;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class UserListUpdateMessage extends AbstractMessage {

    private Set<String> users;

    public UserListUpdateMessage() {
        super(MessageType.USER_LIST);
    }

    public Set<String> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public UserListUpdateMessage setUsers(Set<String> users) {
        this.users = Collections.unmodifiableSet(users);
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
        UserListUpdateMessage that = (UserListUpdateMessage) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }
}
