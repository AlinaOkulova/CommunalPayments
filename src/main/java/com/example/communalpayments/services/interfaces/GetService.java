package com.example.communalpayments.services.interfaces;


public interface GetService<E, T> {

    E get(T id) throws Exception;
}
