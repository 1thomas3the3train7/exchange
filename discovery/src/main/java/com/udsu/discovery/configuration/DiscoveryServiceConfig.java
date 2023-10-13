package com.udsu.discovery.configuration;

import com.discovery.service.server.controller.DiscoveryController;
import com.discovery.service.server.service.DiscoveryService;
import com.discovery.service.server.service.impl.DiscoveryServiceImpl;
import com.discovery.service.server.utils.DiscoveryServiceUtils;
import com.discovery.service.server.utils.impl.DiscoveryServiceUtilsImpl;
import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class DiscoveryServiceConfig {

    @Bean
    public DiscoveryService discoveryService(DiscoveryServiceUtils discoveryServiceUtils,
                                             ReactiveRedisTemplate<String, String> reactiveRedisTemplate,
                                             Gson gson,
                                             WebClient webClient) {
        DiscoveryServiceImpl discoveryService = DiscoveryServiceImpl.builder()
                .discoveryServiceUtils(discoveryServiceUtils)
                .reactiveRedisTemplate(reactiveRedisTemplate)
                .gson(gson)
                .webClient(webClient)
                .build();
        return discoveryService;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public DiscoveryServiceUtils discoveryServiceUtils() {
        return new DiscoveryServiceUtilsImpl();
    }

    @Bean
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}

class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(formatter.format(dateTime));
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDateTime.parse(jsonElement.getAsString(), formatter);
    }
}

