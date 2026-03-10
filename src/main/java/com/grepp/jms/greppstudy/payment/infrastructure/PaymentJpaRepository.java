package com.grepp.jms.greppstudy.payment.infrastructure;

import com.grepp.jms.greppstudy.payment.domain.model.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
}
