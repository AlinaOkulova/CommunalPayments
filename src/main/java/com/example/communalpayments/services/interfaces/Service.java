package com.example.communalpayments.services.interfaces;


public interface Service<E, T> {

    void save(E entity);

    E get(T id) throws Exception;
}
