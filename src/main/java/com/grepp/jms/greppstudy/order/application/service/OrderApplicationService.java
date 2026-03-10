package com.grepp.jms.greppstudy.order.application.service;

import com.grepp.jms.greppstudy.order.application.usecase.OrderUseCase;
import com.grepp.jms.greppstudy.order.domain.model.Order;
import com.grepp.jms.greppstudy.order.domain.repository.OrderRepository;
import com.grepp.jms.greppstudy.order.presentation.dto.request.CreateOrderRequest;
import com.grepp.jms.greppstudy.order.presentation.dto.response.OrderResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderApplicationService implements OrderUseCase {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        UUID actorId = resolveActorId(request);

        Order order = Order.create(
                request.orderNo(),
                request.buyerId(),
                request.sellerId(),
                request.productId(),
                request.quantity(),
                request.grossAmount(),
                request.feeAmount(),
                request.refundAmount(),
                request.status(),
                request.paidAt(),
                actorId
        );

        return toResponse(orderRepository.save(order));
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> findSettlementCandidates(LocalDate settlementDate) {
        LocalDate targetDate = settlementDate == null ? LocalDate.now() : settlementDate;
        LocalDateTime fromInclusive = targetDate.atStartOfDay();
        LocalDateTime toExclusive = fromInclusive.plusDays(1);

        return orderRepository.findUnsettledPaidOrders(fromInclusive, toExclusive).stream()
                .map(this::toResponse)
                .toList();
    }

    private UUID resolveActorId(CreateOrderRequest request) {
        if (request.actorId() != null) {
            return request.actorId();
        }
        if (request.buyerId() != null) {
            return request.buyerId();
        }
        return UUID.randomUUID();
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNo(),
                order.getBuyerId(),
                order.getSellerId(),
                order.getProductId(),
                order.getQuantity(),
                order.getGrossAmount(),
                order.getFeeAmount(),
                order.getRefundAmount(),
                order.getNetAmount(),
                order.getStatus(),
                order.getPaidAt(),
                order.getSettled(),
                order.getSettlementBatchId(),
                order.getRegId(),
                order.getRegDt(),
                order.getModifyId(),
                order.getModifyDt()
        );
    }
}
