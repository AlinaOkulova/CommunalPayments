package com.example.communalpayments.dao;

import com.example.communalpayments.entities.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {

    List<BillingAddress> getBillingAddressesByUserId(long id);

    @Modifying
    @Transactional
    @Query(value = "truncate table communal_payments.billing_addresses restart identity cascade", nativeQuery = true)
    void truncateForTest();
}
