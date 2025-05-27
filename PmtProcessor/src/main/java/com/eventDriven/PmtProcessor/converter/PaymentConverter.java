package com.eventDriven.PmtProcessor.converter;

import com.eventDriven.PmtProcessor.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {
    private static final Logger logger = LoggerFactory.getLogger(PaymentConverter.class);
    private final ObjectMapper objectMapper;

    public PaymentConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convertToString(Payment payment) {
        try {
            return objectMapper.writeValueAsString(payment);
        } catch (JsonProcessingException e) {
            logger.error("Error converting payment to string", e);
            throw new RuntimeException("Failed to convert payment to string", e);
        }
    }

    public Payment convertFromString(String paymentString) {
        try {
            return objectMapper.readValue(paymentString, Payment.class);
        } catch (JsonProcessingException e) {
            logger.error("Error converting string to payment", e);
            throw new RuntimeException("Failed to convert string to payment", e);
        }
    }
}
