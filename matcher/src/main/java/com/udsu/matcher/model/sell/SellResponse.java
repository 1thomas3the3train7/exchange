package com.udsu.matcher.model.sell;

import com.udsu.matcher.model.entity.User;
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
