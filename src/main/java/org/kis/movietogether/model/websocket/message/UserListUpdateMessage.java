package org.kis.movietogether.model.websocket.message;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class UserListUpdateMessage extends AbstractMessage {

    private Set<String> userNames;

    public UserListUpdateMessage() {
        super(MessageType.USER_LIST);
    }

    public Set<String> getUserNames() {
        return Collections.unmodifiableSet(userNames);
    }

    public UserListUpdateMessage setUserNames(Set<String> userNames) {
        this.userNames = Collections.unmodifiableSet(userNames);
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
        return Objects.equals(userNames, that.userNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNames);
    }
}
