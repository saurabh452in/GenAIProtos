package com.eventDriven.PmtProcessor.model;

import com.eventDriven.PmtProcessor.enums.Status;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "eventStore")
public class Event {
    @Id
    private String eventId;

    @Column(nullable = true)
    private String paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Status status;

    @Column(nullable = true)
    private String payment;

    // Default constructor
    public Event() {
        // Default constructor for JPA
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;
        return Objects.equals(getEventId(), event.getEventId()) && Objects.equals(getPaymentId(), event.getPaymentId()) && getStatus() == event.getStatus() && Objects.equals(getPayment(), event.getPayment());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getEventId());
        result = 31 * result + Objects.hashCode(getPaymentId());
        result = 31 * result + Objects.hashCode(getStatus());
        result = 31 * result + Objects.hashCode(getPayment());
        return result;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
