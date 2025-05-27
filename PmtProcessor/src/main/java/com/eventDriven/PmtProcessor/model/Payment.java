package com.eventDriven.PmtProcessor.model;

import com.eventDriven.PmtProcessor.enums.Status;
import jakarta.persistence.*;

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

    @Override
    public int hashCode() {
        return 37* paymentId.hashCode() +
               37 * Double.hashCode(amount) +
               37 * currency.hashCode() +
               37 * debtorAccount.hashCode() +
               37 * creditorAccount.hashCode() +
               37 * status.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return paymentId.equals(((Payment)obj).getPaymentId());
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
