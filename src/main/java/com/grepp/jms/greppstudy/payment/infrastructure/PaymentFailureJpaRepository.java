package com.grepp.jms.greppstudy.payment.infrastructure;

import com.grepp.jms.greppstudy.payment.domain.model.PaymentFailure;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentFailureJpaRepository extends JpaRepository<PaymentFailure, UUID> {
}
