package com.example.communalpayments.dao;

import com.example.communalpayments.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = """
            select p.*\s
            from communal_payments.payments as p
            inner join communal_payments.templates as t on p.template_id = t.id\s
            inner join communal_payments.billing_addresses as b on t.billing_address_id = b.id\s
            inner join communal_payments.users as d on b.user_id = d.id\s
            where d.id = :id order by status_change_time desc\s""", nativeQuery = true)
    List<Payment> getAllByUserId(@Param("id") long id);

    @Transactional
    @Query(value = "with new_payments as (select * from communal_payments.payments where status = 'NEW' " +
            "order by time_of_creation limit 50 for update), " +
            "in_process_payments as (update communal_payments.payments set status = 'IN_PROCESS', " +
            "status_change_time = current_timestamp where id in (select id from new_payments)) " +
            "select * from new_payments",
            nativeQuery = true)
    List<Payment> getPaymentsWhereStatusNewLimit50();

    @Transactional
    @Query(value = "select * from communal_payments.payments where template_id = :templateId and " +
            "payment_amount = :amount and transaction_timestamp()-time_of_creation < interval '00:01:00'",
            nativeQuery = true)
    Optional<Payment> checkDuplicates(long templateId, double amount);

    @Modifying
    @Transactional
    @Query(value = "truncate table communal_payments.payments restart identity cascade", nativeQuery = true)
    void truncateForTest();
}
