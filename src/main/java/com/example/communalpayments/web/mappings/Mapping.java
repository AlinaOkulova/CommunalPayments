package com.example.communalpayments.web.mappings;

public interface Mapping<D, T> {

    T convertDtoTo(D dto) throws Exception;
}
