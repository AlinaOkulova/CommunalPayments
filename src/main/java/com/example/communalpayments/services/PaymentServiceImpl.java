package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.services.interfaces.PaymentService;
import com.example.communalpayments.services.interfaces.Service;
import com.example.communalpayments.services.interfaces.UserService;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class PaymentServiceImpl implements Service<Payment, Long>, PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }

    @Override
    public List<Payment> getAllPaymentsByUserId(Long userId) throws UserNotFoundException {
        userService.checkUserById(userId);
        return paymentRepository.getAllByUserId(userId);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public Payment get(Long id) throws PaymentNotFoundException {
        Optional<Payment> optional = paymentRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new PaymentNotFoundException("Платежа с заданным id не существует");
    }
}
