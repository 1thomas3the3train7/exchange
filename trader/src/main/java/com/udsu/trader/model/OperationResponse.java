package com.udsu.trader.model;

import com.udsu.trader.model.entity.OperationHistory;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class OperationResponse<T> {
    private T operationResponse;
    private List<OperationHistory> operationHistory = new ArrayList<>();

}
