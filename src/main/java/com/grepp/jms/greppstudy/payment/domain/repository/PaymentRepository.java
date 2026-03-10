package com.grepp.jms.greppstudy.payment.domain.repository;

import com.grepp.jms.greppstudy.payment.domain.model.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepository {

    Page<Payment> findAll(Pageable pageable);

    Optional<Payment> findById(UUID id);

    Payment save(Payment payment);
}
