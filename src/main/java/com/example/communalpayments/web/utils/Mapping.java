package com.example.communalpayments.web.utils;

public interface Mapping<D, T> {

    T convertDtoTo(D dto) throws Exception;
}
