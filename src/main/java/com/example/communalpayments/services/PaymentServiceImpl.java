package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.PaymentDuplicateException;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.PaymentService;
import com.example.communalpayments.web.dto.PaymentDto;
import com.example.communalpayments.web.mappings.PaymentMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PaymentMapping mapping;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository,
                              PaymentMapping mapping) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.mapping = mapping;
    }

    @Override
    public Payment createPayment(PaymentDto paymentDto) throws TemplateNotFoundException, PaymentDuplicateException {
        Payment convertedDto = mapping.convertDto(paymentDto);
        Optional<Payment> optional = paymentRepository
                .checkDuplicates(convertedDto.getTemplate().getId(), convertedDto.getAmount());
        if (optional.isPresent()) throw new PaymentDuplicateException();
        Payment payment = paymentRepository.save(convertedDto);
        log.info("Сохранил оплату: " + payment);
        return payment;
    }

    @Override
    public List<Payment> getAllPaymentsByUserId(Long userId) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()) throw new UserNotFoundException();
        return paymentRepository.getAllByUserId(userId);
    }

    @Override
    public Payment get(Long id) throws PaymentNotFoundException {
        Optional<Payment> optional = paymentRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new PaymentNotFoundException();
    }
}
