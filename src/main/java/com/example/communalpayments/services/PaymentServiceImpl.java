package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.exceptions.PaymentDuplicateException;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.GetService;
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
public class PaymentServiceImpl implements GetService<Payment, Long>, PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserServiceImpl userService;
    private final PaymentMapping mapping;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserServiceImpl userService,
                              PaymentMapping mapping) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
        this.mapping = mapping;
    }

    @Override
    public Payment createPayment(PaymentDto paymentDto) throws PaymentDuplicateException, TemplateNotFoundException {
        checkDuplicates(paymentDto.getTemplateId(), paymentDto.getAmount());
        Payment payment = paymentRepository.save(mapping.convertDtoTo(paymentDto));
        log.info("Сохранил оплату: " + payment);
        return payment;
    }

    @Override
    public List<Payment> getAllPaymentsByUserId(Long userId) throws UserNotFoundException {
        userService.get(userId);
        return paymentRepository.getAllByUserId(userId);
    }

    @Override
    public Payment get(Long id) throws PaymentNotFoundException {
        Optional<Payment> optional = paymentRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new PaymentNotFoundException("Платежа с заданным id не существует");
    }

    public void checkDuplicates(long templateId, double amount) throws PaymentDuplicateException {
        List<Payment> payments = paymentRepository.checkDuplicates(templateId, amount);

        if (!payments.isEmpty())
            throw new PaymentDuplicateException("Такая оплата уже существует. Повторите действие через минуту");
    }
}
