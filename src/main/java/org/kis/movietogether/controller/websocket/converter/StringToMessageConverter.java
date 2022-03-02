package org.kis.movietogether.controller.websocket.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kis.movietogether.model.websocket.message.AbstractMessage;
import org.kis.movietogether.model.websocket.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToMessageConverter implements Converter<String, AbstractMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringToMessageConverter.class);
    private static final String MESSAGE_TYPE_FIELD_NAME = "messageType";
    private final ObjectMapper objectMapper;

    public StringToMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AbstractMessage convert(final String source) {
        try {
            final JsonNode jsonNode = objectMapper.readTree(source);
            final String messageTypeName = jsonNode.get(MESSAGE_TYPE_FIELD_NAME).asText();
            final MessageType messageType = MessageType.valueOf(messageTypeName);
            return objectMapper.readValue(source, messageType.getType());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while converting from JSON:", e);
        }
        return null;
    }
}
