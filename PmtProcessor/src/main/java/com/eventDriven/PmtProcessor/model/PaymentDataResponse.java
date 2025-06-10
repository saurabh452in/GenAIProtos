package com.eventDriven.PmtProcessor.model;

import java.util.List;

public class PaymentDataResponse {

    List<Event> eventList;
    Payment payment;

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PaymentDataResponse{" +
                "eventList=" + eventList +
                ", payment=" + payment +
                '}' ;
    }
}
