package com.example.paymentgateway.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "payment_request")
public class PaymentRequest {
    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "merchant_reference_id", length = 36, nullable = false)
    private String merchantReferenceId;

    @Column(name = "merchant_id", length = 36, nullable = false)
    private String merchantId;

    @Column(name = "credit_card_number", length = 16, nullable = false)
    private String creditCardNumber;

    @Column(name = "expiry_month", nullable = false)
    private int expiryMonth;

    @Column(name = "expiry_year", nullable = false)
    private int expiryYear;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "cvv", length = 3, nullable = false)
    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status;

    @Column(name = "message")
    private String message;

    public PaymentRequest() {
        // Default constructor
    }

    public String getId() {
        return id;
    }

    public String getMerchantReferenceId() {
        return merchantReferenceId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCvv() {
        return cvv;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequest that = (PaymentRequest) o;
        return expiryMonth == that.expiryMonth && expiryYear == that.expiryYear && Objects.equals(id, that.id)
                && Objects.equals(merchantReferenceId, that.merchantReferenceId)
                && Objects.equals(merchantId, that.merchantId)
                && Objects.equals(creditCardNumber, that.creditCardNumber)
                && Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency)
                && Objects.equals(cvv, that.cvv) && status == that.status && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, merchantReferenceId, merchantId, creditCardNumber, expiryMonth,
                expiryYear, amount, currency, cvv, status, message);
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "id='" + id + '\'' +
                ", merchantReferenceId='" + merchantReferenceId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", expiryMonth=" + expiryMonth +
                ", expiryYear=" + expiryYear +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", cvv='" + cvv + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    public static class Builder {
        private final PaymentRequest paymentRequest = new PaymentRequest();

        public Builder withId(String id) {
            paymentRequest.id = id;
            return this;
        }

        public Builder withMerchantReferenceId(String merchantReferenceId) {
            paymentRequest.merchantReferenceId = merchantReferenceId;
            return this;
        }

        public Builder withMerchantId(String merchantId) {
            paymentRequest.merchantId = merchantId;
            return this;
        }

        public Builder withCreditCardNumber(String creditCardNumber) {
            paymentRequest.creditCardNumber = creditCardNumber;
            return this;
        }

        public Builder withExpiryMonth(int expiryMonth) {
            paymentRequest.expiryMonth = expiryMonth;
            return this;
        }

        public Builder withExpiryYear(int expiryYear) {
            paymentRequest.expiryYear = expiryYear;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {
            paymentRequest.amount = amount;
            return this;
        }

        public Builder withCurrency(String currency) {
            paymentRequest.currency = currency;
            return this;
        }

        public Builder withCvv(String cvv) {
            paymentRequest.cvv = cvv;
            return this;
        }

        public Builder withStatus(PaymentStatus status) {
            paymentRequest.status = status;
            return this;
        }

        public Builder withMessage(String message) {
            paymentRequest.message = message;
            return this;
        }

        public PaymentRequest build() {
            // Add validation logic here if needed
            return paymentRequest;
        }
    }
}
