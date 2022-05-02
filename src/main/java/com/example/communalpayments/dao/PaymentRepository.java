package com.example.communalpayments.dao;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select p.* \n" +
            "from communal_payments.client_payments as p\n" +
            "inner join communal_payments.client_templates as t on p.template_id = t.id \n" +
            "inner join communal_payments.client_billing_addresses as b on t.billing_address_id = b.id \n" +
            "inner join communal_payments.client_data as d on b.user_id = d.id \n" +
            "where d.id = :id ", nativeQuery = true)
    List<Payment> getAllByUserId(@Param("id") long id);

    @Query(value = "select * from communal_payments.client_payments where status = 'NEW' for update",
            nativeQuery = true)
    List<Payment> getAllWhereStatusNew();
}
