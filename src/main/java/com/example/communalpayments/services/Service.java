package com.example.communalpayments.services;

import java.util.List;

public interface Service<E, T> {
    List<E> getAllById(T id);

    void save(E entity);

    E get(T id);
}
