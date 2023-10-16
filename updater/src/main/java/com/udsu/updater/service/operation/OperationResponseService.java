package com.udsu.updater.service.operation;

import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.request.LastPriceRequest;
import com.udsu.updater.model.request.OperationByDateRequest;
import com.udsu.updater.repository.OperationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationResponseService {
    private final OperationHistoryRepository operationHistoryRepository;

    public Mono<List<OperationHistory>> buildFindByDateAndCurrResponse(OperationByDateRequest request) {
        return operationHistoryRepository.findByDate(request.getFromDate(), request.getToDate(),
                        request.getFromCurrency(), request.getToCurrency(), request.getOperationStatus())
                .collectList();
    }

    public Mono<OperationHistory> buildFindLastPriceResponse(LastPriceRequest request) {
        return operationHistoryRepository.findLastPriceForCurrency(request);
    }
}
