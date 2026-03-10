package com.grepp.jms.greppstudy.payment.domain.repository;

import com.grepp.jms.greppstudy.payment.domain.model.PaymentFailure;

public interface PaymentFailureRepository {

    PaymentFailure save(PaymentFailure failure);
}
