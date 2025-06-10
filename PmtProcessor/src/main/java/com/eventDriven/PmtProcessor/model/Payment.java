package com.eventDriven.PmtProcessor.model;

import com.eventDriven.PmtProcessor.enums.Status;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    private String paymentId;

    @Column(nullable = true)
    private double amount;

    @Column(nullable = true)
    private String currency;

    @Column(name = "debtor_account", nullable = true)
    private String debtorAccount;

    @Column(name = "creditor_account", nullable = true)
    private String creditorAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Status status;

    @Column(name = "paymentCreationTimestamp",nullable = true)
    private Date paymentCreationTimestamp;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;
        return Double.compare(getAmount(), payment.getAmount()) == 0 && Objects.equals(getPaymentId(), payment.getPaymentId()) && Objects.equals(getCurrency(), payment.getCurrency()) && Objects.equals(getDebtorAccount(), payment.getDebtorAccount()) && Objects.equals(getCreditorAccount(), payment.getCreditorAccount()) && getStatus() == payment.getStatus() && Objects.equals(getPaymentCreationTimestamp(), payment.getPaymentCreationTimestamp());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getPaymentId());
        result = 31 * result + Double.hashCode(getAmount());
        result = 31 * result + Objects.hashCode(getCurrency());
        result = 31 * result + Objects.hashCode(getDebtorAccount());
        result = 31 * result + Objects.hashCode(getCreditorAccount());
        result = 31 * result + Objects.hashCode(getStatus());
        result = 31 * result + Objects.hashCode(getPaymentCreationTimestamp());
        return result;
    }

    public Date getPaymentCreationTimestamp() {
        return paymentCreationTimestamp;
    }

    public void setPaymentCreationTimestamp(Date paymentCreationTimestamp) {
        this.paymentCreationTimestamp = paymentCreationTimestamp;
    }

    // Default constructor
    public Payment() {
        // Default constructor for JPA
    }
    // All args constructor
    public Payment(String paymentId, double amount, String currency,
                   String debtorAccount, String creditorAccount, Status status) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.debtorAccount = debtorAccount;
        this.creditorAccount = creditorAccount;
        this.status = status;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDebtorAccount() {
        return debtorAccount;
    }

    public void setDebtorAccount(String debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public String getCreditorAccount() {
        return creditorAccount;
    }

    public void setCreditorAccount(String creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", debtorAccount='" + debtorAccount + '\'' +
                ", creditorAccount='" + creditorAccount + '\'' +
                ", status=" + status +
                '}';
    }
}
