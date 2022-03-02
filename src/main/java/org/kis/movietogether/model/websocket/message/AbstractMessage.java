package org.kis.movietogether.model.websocket.message;

public abstract class AbstractMessage {

    private final MessageType messageType;

    protected AbstractMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
