package com.example.communalpayments.dao;

import com.example.communalpayments.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "truncate table communal_payments.users restart identity cascade", nativeQuery = true)
    void truncateForTest();
}
