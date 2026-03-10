package com.grepp.jms.greppstudy.payment.infrastructure;

import com.grepp.jms.greppstudy.payment.domain.model.Payment;
import com.grepp.jms.greppstudy.payment.domain.repository.PaymentRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return paymentJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return paymentJpaRepository.findById(id);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
