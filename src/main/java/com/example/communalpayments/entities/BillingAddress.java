package com.example.communalpayments.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "communal_payments", name = "client_billing_addresses")
public class BillingAddress {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "billing_address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
                mappedBy = "address")
    @JsonManagedReference
    private List<Template> templates;

//    public void addTemplateToAddress(Template template) {
//        if (templates == null) {
//            templates = new ArrayList<>();
//        }
//        templates.add(template);
//        template.setAddress(this);
//    }
}