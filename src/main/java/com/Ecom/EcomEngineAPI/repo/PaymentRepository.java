package com.Ecom.EcomEngineAPI.repo;


import com.Ecom.EcomEngineAPI.entity.Payment;
import com.Ecom.EcomEngineAPI.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByPaymentReference(String reference);
    List<Payment> findByStatus(PaymentStatus status);
}
