package com.eventDriven.PmtProcessor.config;

import com.eventDriven.PmtProcessor.enums.PaymentEvent;
import com.eventDriven.PmtProcessor.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<Status, PaymentEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StateMachineConfig.class);

    @Override
    public void configure(StateMachineStateConfigurer<Status, PaymentEvent> states) throws Exception {
        states
            .withStates()
            .initial(Status.INITIATED)
            .states(EnumSet.allOf(Status.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Status, PaymentEvent> transitions) throws Exception {
        transitions
            .withExternal()
                .source(Status.INITIATED)
                .target(Status.RECEIVED)
                .event(PaymentEvent.RECEIVE)
                .and()
            .withExternal()
                .source(Status.RECEIVED)
                .target(Status.ROUTED)
                .event(PaymentEvent.PROCESS)
                .and()
            .withExternal()
                .source(Status.ROUTED)
                .target(Status.ACCEPTED)
                .event(PaymentEvent.COMPLETE)
                .and()
            .withExternal()
                .source(Status.RECEIVED)
                .target(Status.TECHNICAL_ERROR)
                .event(PaymentEvent.FAIL)
                .and()
            .withExternal()
                .source(Status.ROUTED)
                .target(Status.TECHNICAL_ERROR)
                .event(PaymentEvent.FAIL)
                .and()
            .withExternal()
                .source(Status.RECEIVED)
                .target(Status.REJECTED)
                .event(PaymentEvent.REJECT);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<Status, PaymentEvent> config) throws Exception {
        StateMachineListenerAdapter<Status, PaymentEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<Status, PaymentEvent> from, State<Status, PaymentEvent> to) {
                logger.info("State changed from: {} to: {}",
                    from != null ? from.getId() : "none",
                    to != null ? to.getId() : "none");
            }
        };

        config.withConfiguration()
            .listener(adapter);
    }
}
