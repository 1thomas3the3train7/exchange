package com.udsu.auth.model.sell;

import com.udsu.auth.model.User;
import com.udsu.auth.model.buy.StatusBuy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellResponse {
    private String id;
    private User seller;
    private StatusSell status;

    public static SellResponse fromSellRequest(SellRequest sellRequest, StatusSell status) {
        return SellResponse.builder()
                .id(sellRequest.getId())
                .seller(sellRequest.getSeller())
                .status(status)
                .build();
    }
}
