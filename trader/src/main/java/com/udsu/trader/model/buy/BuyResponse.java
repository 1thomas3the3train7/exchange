package com.udsu.trader.model.buy;



import com.udsu.trader.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyResponse {
    private String id;
    private User buyer;
    private StatusBuy status;
    private Float countWasBought;

    public static BuyResponse fromBuyRequest(BuyRequest buyRequest, StatusBuy status) {
        return BuyResponse.builder()
                .id(buyRequest.getId())
                .buyer(buyRequest.getBuyer())
                .status(status)
                .build();
    }
}
