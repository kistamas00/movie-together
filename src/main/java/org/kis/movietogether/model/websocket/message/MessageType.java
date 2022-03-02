package org.kis.movietogether.model.websocket.message;

public enum MessageType {

    USER_DETAILS(UserDetailsMessage.class),
    USER_LIST(UserListUpdateMessage.class);

    private final Class<? extends AbstractMessage> type;

    MessageType(Class<? extends AbstractMessage> type) {
        this.type = type;
    }

    public Class<? extends AbstractMessage> getType() {
        return type;
    }
}
