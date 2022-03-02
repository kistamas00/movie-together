package org.kis.movietogether.controller.websocket.converter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kis.movietogether.controller.websocket.converter.MessageToStringConverter;
import org.kis.movietogether.controller.websocket.converter.StringToMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MessageToStringConverter messageToStringConverter(final ObjectMapper objectMapper) {
        return new MessageToStringConverter(objectMapper);
    }

    @Bean
    public StringToMessageConverter stringToMessageConverter(final ObjectMapper objectMapper) {
        return new StringToMessageConverter(objectMapper);
    }
}
