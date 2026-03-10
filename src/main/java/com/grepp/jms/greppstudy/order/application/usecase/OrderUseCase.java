package com.grepp.jms.greppstudy.order.application.usecase;

import com.grepp.jms.greppstudy.order.presentation.dto.request.CreateOrderRequest;
import com.grepp.jms.greppstudy.order.presentation.dto.response.OrderResponse;
import java.time.LocalDate;
import java.util.List;

public interface OrderUseCase {

    OrderResponse create(CreateOrderRequest request);

    List<OrderResponse> findAll();

    List<OrderResponse> findSettlementCandidates(LocalDate settlementDate);
}
