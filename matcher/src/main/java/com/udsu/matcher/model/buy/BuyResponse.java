package com.udsu.matcher.model.buy;


import com.udsu.matcher.model.entity.User;
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
