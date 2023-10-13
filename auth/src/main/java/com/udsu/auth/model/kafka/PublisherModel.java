package com.udsu.auth.model.kafka;

import com.udsu.auth.model.buy.BuyRequest;
import com.udsu.auth.model.sell.SellRequest;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PublisherModel<T> {
    private String id;
    private T payload;
    private LocalDateTime date;

    public static PublisherModel<BuyRequest> fromBuyRequest(BuyRequest buyRequest) {
        return PublisherModel.<BuyRequest>builder()
                .id(buyRequest.getId())
                .payload(buyRequest)
                .date(LocalDateTime.now())
                .build();
    }

    public static PublisherModel<SellRequest> fromSellRequest(SellRequest sellRequest) {
        return PublisherModel.<SellRequest>builder()
                .id(sellRequest.getId())
                .payload(sellRequest)
                .date(LocalDateTime.now())
                .build();
    }
}
