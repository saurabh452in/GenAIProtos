package com.eventDriven.PmtProcessor.controller;

import com.eventDriven.PmtProcessor.converter.PaymentConverter;
import com.eventDriven.PmtProcessor.enums.Status;
import com.eventDriven.PmtProcessor.model.Payment;
import com.eventDriven.PmtProcessor.model.PaymentDataResponse;
import com.eventDriven.PmtProcessor.repository.EventRepository;
import com.eventDriven.PmtProcessor.repository.PaymentRepository;
import org.slf4j.Logger;
import java.util.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentRepository repository;
    @Autowired
    EventRepository eventRepository;

    @Autowired
    PaymentConverter converter;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/{txnId}")
    public ResponseEntity<PaymentDataResponse> getAllPaymentData(@PathVariable String txnId) {
        logger.info("getting all Payment data with id: {}", txnId);
        StringBuilder builder = new StringBuilder();
        Payment pmt =
                repository.findById(txnId).orElse(null);
        PaymentDataResponse paymentDataResponse = new PaymentDataResponse();
        paymentDataResponse.setPayment(pmt);
        paymentDataResponse.setEventList(eventRepository.findByPaymentId(txnId));

        /*if (pmt!=null) {
            builder.append(converter.convertToString(pmt));
            builder.append("\n");
        }
        List<Event> events =
                eventRepository.findByPaymentId(txnId);

        if (!CollectionUtils.isEmpty(events)) {
            events.forEach(event -> {
                builder.append(converter.convertToString(event));
                builder.append("\n");
            });
        };*/

        return new ResponseEntity(paymentDataResponse, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create/{paymentId}")
    public ResponseEntity<Payment> createPayment(@PathVariable String paymentId) {
        Payment payment = new Payment();
        payment.setAmount(100.0); // Default amount
        payment.setCurrency("USD"); // Default currency
        payment.setCreditorAccount("123-456-789"); // Default creditor account
        payment.setDebtorAccount("987-654-321"); // Default debtor account
        payment.setStatus(Status.INITIATED);
        payment.setPaymentId(paymentId);
        payment.setPaymentCreationTimestamp(new Date());
        repository.save(payment);

        logger.info("Received payment request: {}", payment);

        try {
            payment.setStatus(Status.INITIATED);
            logger.debug("Payment processed successfully");

            kafkaTemplate.send("gatewayTopic", payment.getPaymentId(), converter.convertToString(payment));
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
