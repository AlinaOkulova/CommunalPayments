package com.example.communalpayments.entities;

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
@Table(schema = "communal_payments", name = "client_data")
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL,
                mappedBy = "user")
    @JsonManagedReference
    private List<BillingAddress> billingAddresses;

//    public void addAddressToUser(BillingAddress billingAddress) {
//        if(billingAddresses == null) {
//            billingAddresses = new ArrayList<>();
//        }
//        billingAddresses.add(billingAddress);
//        billingAddress.setUser(this);
//    }

}
