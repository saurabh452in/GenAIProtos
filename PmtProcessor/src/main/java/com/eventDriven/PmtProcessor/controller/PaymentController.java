package com.eventDriven.PmtProcessor.controller;

import com.eventDriven.PmtProcessor.converter.PaymentConverter;
import com.eventDriven.PmtProcessor.enums.Status;
import com.eventDriven.PmtProcessor.model.Payment;
import com.eventDriven.PmtProcessor.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    PaymentRepository repository;

    @Autowired
    PaymentConverter converter;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/create/{paymentId}")
    public ResponseEntity<Payment> createPayment(@PathVariable String paymentId) {
        Payment payment = new Payment();
        payment.setAmount(100.0); // Default amount
        payment.setCurrency("USD"); // Default currency
        payment.setCreditorAccount("123-456-789"); // Default creditor account
        payment.setDebtorAccount("987-654-321"); // Default debtor account
        payment.setStatus(Status.INITIATED);
        payment.setPaymentId(paymentId);
        repository.save(payment);

        logger.info("Received payment request: {}", payment);

        try {
            payment.setStatus(Status.INITIATED);
            logger.debug("Payment processed successfully");

            kafkaTemplate.send("gatewayTopic",payment.getPaymentId(), converter.convertToString(payment));
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
