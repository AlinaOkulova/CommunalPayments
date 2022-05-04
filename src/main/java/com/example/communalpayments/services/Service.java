package com.example.communalpayments.services;

public interface Service<E, T> {

    void save(E entity);

    E get(T id);
}
