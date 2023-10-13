package com.udsu.auth.model.buy;

import com.udsu.auth.model.User;
import com.udsu.auth.model.sell.SellRequest;
import com.udsu.auth.model.sell.SellResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyResponse {
    private String id;
    private User buyer;
    private StatusBuy status;

    public static BuyResponse fromBuyRequest(BuyRequest buyRequest, StatusBuy status) {
        return BuyResponse.builder()
                .id(buyRequest.getId())
                .buyer(buyRequest.getBuyer())
                .status(status)
                .build();
    }
}
