package com.eventDriven.PmtProcessor.svc;

import com.eventDriven.PmtProcessor.converter.PaymentConverter;
import com.eventDriven.PmtProcessor.enums.Status;
import com.eventDriven.PmtProcessor.model.Event;
import com.eventDriven.PmtProcessor.model.Payment;
import com.eventDriven.PmtProcessor.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TopicListeners {
    @Autowired
    PaymentConverter converter;

    @Autowired
    EventRepository eventRepository;

    private static final Logger logger = LoggerFactory.getLogger(TopicListeners.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "gatewayTopic", groupId = "gatewayTopicCG", concurrency = "3")
    public void listen1(@Payload String message, Acknowledgment acknowledgment ) {

        Payment payment = converter.convertFromString(message);
        logger.info("Received message from gatewayTopic: {}", payment);
        logger.debug("Processing message in gateway listener");
        payment.setStatus(Status.RECEIVED);

        Event e = new Event();
        e.setPaymentId(payment.getPaymentId());
        e.setEventId(UUID.randomUUID().toString());
        e.setPayment(converter.convertToString(payment));
        e.setStatus(Status.RECEIVED);


        eventRepository.save(e);

        kafkaTemplate.send("routingTopic",payment.getPaymentId(),converter.convertToString(payment));


        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "routingTopic", groupId = "routingTopicCG", concurrency = "3")
    public void listen2(@Payload String message, Acknowledgment acknowledgment) {

        Payment payment = converter.convertFromString(message);
        logger.info("Received message from routingTopic: {}", message);
        logger.debug("Processing message in routing listener");

        payment.setStatus(Status.ROUTED);

        Event e = new Event();
        e.setPaymentId(payment.getPaymentId());
        e.setEventId(UUID.randomUUID().toString());
        e.setPayment(converter.convertToString(payment));
        e.setStatus(Status.ROUTED);

        eventRepository.save(e);

        kafkaTemplate.send("outputTopic",converter.convertToString(payment));
        acknowledgment.acknowledge();
    }
}

