package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.PaymentStatus;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.PaymentDuplicateException;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.PaymentService;
import com.example.communalpayments.web.dto.PaymentDto;
import com.example.communalpayments.web.mappings.PaymentMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplTest {

    private PaymentRepository paymentRepository;
    private UserRepository userRepository;
    private PaymentMapping mapping;

    private PaymentService paymentService;

    private PaymentDto paymentDto;
    private Template template;
    private Payment unsavedPayment;
    private Payment savedPayment;

    @BeforeEach
    void setUp() {
        paymentRepository = Mockito.mock(PaymentRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        mapping = Mockito.mock(PaymentMapping.class);

        paymentService = new PaymentServiceImpl(paymentRepository, userRepository, mapping);

        paymentDto = new PaymentDto(5, "4441114450791395", 896.15);
        template = Template
                    .builder()
                    .id(5L)
                    .name("отопление")
                    .iban("UA231236580000000123654800023")
                    .purposeOfPayment("отопление лиц. счет 123658")
                    .build();
        unsavedPayment = new Payment(template, "4441114450791395", 896.15);
        savedPayment = Payment.builder()
                        .id(10L)
                        .template(template)
                        .cardNumber("4441114450791395")
                        .amount(896.15)
                        .status(unsavedPayment.getStatus())
                        .timeOfCreation(unsavedPayment.getTimeOfCreation())
                        .timeStatusChange(unsavedPayment.getTimeStatusChange())
                        .build();
    }

    @Test
    void createPaymentTest() throws TemplateNotFoundException, PaymentDuplicateException {

        when(mapping.convertDto(eq(paymentDto))).thenReturn(unsavedPayment);
        when(paymentRepository.checkDuplicates(unsavedPayment.getTemplate().getId(), unsavedPayment.getAmount()))
                .thenReturn(Optional.empty());
        when(paymentRepository.save(eq(unsavedPayment))).thenReturn(savedPayment);

        Payment payment = paymentService.createPayment(paymentDto);

        verify(mapping, times(1)).convertDto(eq(paymentDto));
        verify(paymentRepository, times(1)).checkDuplicates(eq(5L), eq(896.15));
        verify(paymentRepository, times(1)).save(eq(unsavedPayment));

        assertEquals(10, payment.getId());
        assertEquals(template, payment.getTemplate());
        assertEquals("4441114450791395", payment.getCardNumber());
        assertEquals(896.15, payment.getAmount());
        assertEquals(PaymentStatus.NEW, payment.getStatus());
        assertEquals(unsavedPayment.getTimeOfCreation(), payment.getTimeOfCreation());
        assertEquals(unsavedPayment.getTimeStatusChange(), payment.getTimeStatusChange());
    }

    @Test
    void createPaymentThrowsTemplateNotFoundExTest() throws TemplateNotFoundException {

        when(mapping.convertDto(eq(paymentDto))).thenThrow(new TemplateNotFoundException());

        TemplateNotFoundException exception = assertThrows(TemplateNotFoundException.class,
                () -> paymentService.createPayment(paymentDto));

        verify(mapping, times(1)).convertDto(eq(paymentDto));
        verify(paymentRepository, times(0)).checkDuplicates(anyLong(), anyDouble());
        verify(paymentRepository, times(0)).save(any());

        assertEquals("Шаблон с заданным id не существует", exception.getMessage());
    }

    @Test
    void createPaymentThrowsPaymentDuplicateExTest() throws TemplateNotFoundException {

        when(mapping.convertDto(paymentDto)).thenReturn(unsavedPayment);
        when(paymentRepository.checkDuplicates(unsavedPayment.getTemplate().getId(), unsavedPayment.getAmount()))
                .thenReturn(Optional.of(savedPayment));

        PaymentDuplicateException exception = assertThrows(PaymentDuplicateException.class,
                () -> paymentService.createPayment(paymentDto));

        verify(mapping, times(1)).convertDto(eq(paymentDto));
        verify(paymentRepository, times(1)).checkDuplicates(eq(5L), eq(896.15));
        verify(paymentRepository, times(0)).save(any());

        assertEquals("Такая оплата уже существует. Повторите действие через минуту", exception.getMessage());
    }

    @Test
    void getAllPaymentsByUserIdTest() throws UserNotFoundException {
        List<Payment> payments = new ArrayList<>();
        payments.add(savedPayment);

        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder()
                                                                    .id(2L)
                                                                    .lastName("Ivanov")
                                                                    .firstName("Ivan")
                                                                    .patronymic("Ivanovych")
                                                                    .email("ivan@gmail.com")
                                                                    .phoneNumber("0961254856")
                                                                    .build()));
        when(paymentRepository.getAllByUserId(2L)).thenReturn(payments);

        List<Payment> usersPayments = paymentService.getAllPaymentsByUserId(2L);

        verify(userRepository, times(1)).findById(eq(2L));
        verify(paymentRepository, times(1)).getAllByUserId(eq(2L));

        assertIterableEquals(payments, usersPayments);
    }

    @Test
    void getAllPaymentsByUserIdThrowsExTest() {

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> paymentService.getAllPaymentsByUserId(2L));

        verify(userRepository, times(1)).findById(eq(2L));
        verify(paymentRepository, times(0)).getAllByUserId(eq(2L));

        assertEquals("Пользователь с заданным id не существует", exception.getMessage());
    }

    @Test
    void getTest() throws PaymentNotFoundException {

        when(paymentRepository.findById(10L)).thenReturn(Optional.of(savedPayment));

        Payment payment = paymentService.get(10L);

        verify(paymentRepository, times(1)).findById(eq(10L));

        assertEquals(10, payment.getId());
        assertEquals(template, payment.getTemplate());
        assertEquals("4441114450791395", payment.getCardNumber());
        assertEquals(896.15, payment.getAmount());
        assertEquals(savedPayment.getStatus(), payment.getStatus());
        assertEquals(savedPayment.getTimeOfCreation(), payment.getTimeOfCreation());
        assertEquals(savedPayment.getTimeStatusChange(), payment.getTimeStatusChange());
    }

    @Test
    void getThrowsExTest() {

        when(paymentRepository.findById(10L)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class,
                () -> paymentService.get(10L));

        verify(paymentRepository, times(1)).findById(eq(10L));

        assertEquals("Платежа с заданным id не существует", exception.getMessage());
    }
}