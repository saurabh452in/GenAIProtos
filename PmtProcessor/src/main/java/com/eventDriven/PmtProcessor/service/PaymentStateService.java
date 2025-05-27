/*
 package com.eventDriven.PmtProcessor.service;

import com.eventDriven.PmtProcessor.enums.PaymentEvent;
import com.eventDriven.PmtProcessor.enums.Status;
import com.eventDriven.PmtProcessor.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
public class PaymentStateService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentStateService.class);
    private static final String PAYMENT_ID_HEADER = "paymentId";

    private final StateMachineFactory<Status, PaymentEvent> stateMachineFactory;

    public PaymentStateService(StateMachineFactory<Status, PaymentEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public StateMachine<Status, PaymentEvent> build(Payment payment) {
        StateMachine<Status, PaymentEvent> sm = stateMachineFactory.getStateMachine(payment.getTxnId());

        sm.stop();

        sm.getStateMachineAccessor()
            .doWithAllRegions(sma -> {
                sma.resetStateMachine(new DefaultStateMachineContext<>(
                    payment.getStatus(), null, null, null));
            });

        sm.start();
        return sm;
    }

    public boolean sendEvent(String paymentId, StateMachine<Status, PaymentEvent> sm, PaymentEvent event) {
        Message<PaymentEvent> msg = MessageBuilder.withPayload(event)
            .setHeader(PAYMENT_ID_HEADER, paymentId)
            .build();

        logger.info("Sending event {} for payment {}", event, paymentId);
        return sm.sendEvent(msg);
    }
}
*/
