package org.kis.movietogether.controller.websocket.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kis.movietogether.model.websocket.message.AbstractMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class MessageToStringConverter implements Converter<AbstractMessage, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageToStringConverter.class);
    private final ObjectMapper objectMapper;

    public MessageToStringConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(final AbstractMessage source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while converting to JSON:", e);
        }
        return null;
    }
}
