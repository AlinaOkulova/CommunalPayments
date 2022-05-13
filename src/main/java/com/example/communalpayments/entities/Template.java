package com.example.communalpayments.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "communal_payments", name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "template_name")
    private String name;

    @Column(name = "iban")
    private String iban;

    @Column(name = "purpose_of_payment")
    private String purposeOfPayment;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "billing_address_id")
    @JsonBackReference
    private BillingAddress address;

    @Override
    public String toString() {
        return "Template{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iban='" + iban + '\'' +
                ", purposeOfPayment='" + purposeOfPayment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;
        return id == template.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
