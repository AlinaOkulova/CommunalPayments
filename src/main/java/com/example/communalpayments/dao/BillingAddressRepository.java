package com.example.communalpayments.dao;

import com.example.communalpayments.entities.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {

    List<BillingAddress> getBillingAddressesByUserId(long id);
}
