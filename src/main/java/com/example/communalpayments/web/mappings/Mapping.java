package com.example.communalpayments.web.mappings;

public interface Mapping<D, T> {

    T convertDto(D dto) throws Exception;
}
