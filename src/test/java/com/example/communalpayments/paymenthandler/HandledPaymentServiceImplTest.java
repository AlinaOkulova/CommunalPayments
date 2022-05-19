package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.PaymentStatus;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HandledPaymentServiceImplTest {

    private PaymentRepository repository;
    private HandledPaymentMapping mapping;

    private HandledPaymentService service;

    private HandledPaymentDto paymentDto;
    private Payment handledPayment;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PaymentRepository.class);
        mapping = Mockito.mock(HandledPaymentMapping.class);
        service = new HandledPaymentServiceImpl(repository, mapping);

        paymentDto = new HandledPaymentDto(10L, PaymentStatus.DONE);
        handledPayment = Payment.builder()
                .id(10L)
                .template(Template
                        .builder()
                        .id(5L)
                        .name("отопление")
                        .iban("UA231236580000000123654800023")
                        .purposeOfPayment("отопление лиц. счет 123658")
                        .build())
                .cardNumber("4441114450791395")
                .amount(896.15)
                .status(PaymentStatus.DONE)
                .timeOfCreation(LocalDateTime.now())
                .timeStatusChange(LocalDateTime.now())
                .build();
    }

    @Test
    void addToSaveQueueTest() throws PaymentNotFoundException {

        when(mapping.convertDto(eq(paymentDto))).thenReturn(handledPayment);

        service.addToSaveQueue(paymentDto);

        verify(mapping, times(1)).convertDto(eq(paymentDto));
    }

    @Test
    void addToSaveQueueThrowsExTest() throws PaymentNotFoundException {

        when(mapping.convertDto(eq(paymentDto))).thenThrow(new PaymentNotFoundException());

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class,
                () -> service.addToSaveQueue(paymentDto));

        assertEquals("Платежа с заданным id не существует", exception.getMessage());
    }
}