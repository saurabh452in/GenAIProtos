package com.eventDriven.PmtProcessor.repository;

import com.eventDriven.PmtProcessor.model.Payment;
import com.eventDriven.PmtProcessor.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Override
    Optional<Payment> findById(String s);

    List<Payment> findByStatus(Status status);
    List<Payment> findByDebtorAccount(String debtorAccount);
    List<Payment> findByCreditorAccount(String creditorAccount);
}
