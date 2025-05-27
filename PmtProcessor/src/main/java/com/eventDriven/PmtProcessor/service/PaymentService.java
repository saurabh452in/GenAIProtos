/*
package com.eventDriven.PmtProcessor.service;

import com.eventDriven.PmtProcessor.enums.PaymentEvent;
import com.eventDriven.PmtProcessor.enums.Status;
import com.eventDriven.PmtProcessor.model.Payment;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.statemachine.StateMachine;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    @PostConstruct
    public void init() {
        stateMMap = new ConcurrentHashMap<String, StateMachine<Status, PaymentEvent>>();
    }

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentStateService paymentStateService;

    private ConcurrentHashMap<String, StateMachine<Status, PaymentEvent>> stateMMap;

    public PaymentService(PaymentStateService paymentStateService) {
        this.paymentStateService = paymentStateService;
    }

    public void instantiateOrRetrieveSm(Payment payment) {
        logger.info("Instantiating payment processing for payment: {}", payment.getTxnId());
        StateMachine<Status, PaymentEvent> sm = stateMMap.get(payment.getTxnId(), payment);
        if (sm == null) {
            payment.setStatus(Status.INITIATED);
            sm = paymentStateService.build(payment);
            stateMMap.put(payment.getTxnId(), sm);

        }
    }


    public boolean processReceivedState(Payment payment) {

        logger.info("Starting payment processing for payment: {}", payment.getTxnId());

        // Move to RECEIVED state
        boolean receiveSuccess = paymentStateService.sendEvent(payment.getTxnId(), sm, PaymentEvent.RECEIVE);
        if (!receiveSuccess) {
            logger.error("Failed to receive payment {}", payment.getTxnId());
            return false;
        }

        // Move to ROUTED state
        boolean routeSuccess = paymentStateService.sendEvent(payment.getTxnId(), sm, PaymentEvent.PROCESS);
        if (!routeSuccess) {
            logger.error("Failed to route payment {}", payment.getTxnId());
            // Example of transitioning to error state
            paymentStateService.sendEvent(payment.getTxnId(), sm, PaymentEvent.FAIL);
            return false;
        }

        // Complete the payment
        boolean completeSuccess = paymentStateService.sendEvent(payment.getTxnId(), sm, PaymentEvent.COMPLETE);
        if (!completeSuccess) {
            logger.error("Failed to complete payment {}", payment.getTxnId());
            paymentStateService.sendEvent(payment.getTxnId(), sm, PaymentEvent.FAIL);
            return false;
        }

        logger.info("Payment {} processed successfully", payment.getTxnId());
        return true;
    }

    public boolean rejectPayment(Payment payment) {
        StateMachine<Status, PaymentEvent> sm = paymentStateService.build(payment);
        return paymentStateService.sendEvent(payment.getTxnId(), sm, PaymentEvent.REJECT);
    }

    public boolean markPaymentAsFailed(Payment payment) {
        StateMachine<Status, PaymentEvent> sm = paymentStateService.build(payment);
        return paymentStateService.sendEvent(payment.getTxnId(), sm, PaymentEvent.FAIL);
    }
}
*/
