package com.example.communalpayments.exceptions;

public class TemplateNotFoundException extends Exception {

    public TemplateNotFoundException() {
        super("Шаблон с заданным id не существует");
    }
}
