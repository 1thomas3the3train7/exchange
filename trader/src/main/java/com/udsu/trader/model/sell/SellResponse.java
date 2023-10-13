package com.udsu.trader.model.sell;

import com.udsu.trader.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellResponse {
    private String id;
    private User seller;
    private StatusSell status;
    private Float countWasSold;

    public static SellResponse fromSellRequest(SellRequest sellRequest, StatusSell status) {
        return SellResponse.builder()
                .id(sellRequest.getId())
                .seller(sellRequest.getSeller())
                .status(status)
                .build();
    }
}
