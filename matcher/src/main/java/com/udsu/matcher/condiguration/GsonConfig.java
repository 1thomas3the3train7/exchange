package com.udsu.matcher.condiguration;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.udsu.matcher.model.buy.BuyRequest;
import com.udsu.matcher.model.kafka.PublisherModel;
import com.udsu.matcher.model.sell.SellRequest;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    public static Type publisherModelBuyRequest = new TypeToken<PublisherModel<BuyRequest>>(){}.getType();
    public static Type publisherModelSellRequest = new TypeToken<PublisherModel<SellRequest>>(){}.getType();


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
