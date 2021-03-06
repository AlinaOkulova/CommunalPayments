package com.example.communalpayments.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "communal_payments", name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "payment_amount")
    private double amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "time_of_creation")
    private LocalDateTime timeOfCreation;

    @Column(name = "status_change_time")
    private LocalDateTime timeStatusChange;

    public Payment(Template template, String cardNumber, double amount) {
        this.template = template;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.status = PaymentStatus.NEW;
        this.timeOfCreation = LocalDateTime.now();
        this.timeStatusChange = null;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id == payment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
