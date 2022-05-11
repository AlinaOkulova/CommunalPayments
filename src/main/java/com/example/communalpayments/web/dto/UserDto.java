package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;

@Data
@Component
public class UserDto {

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String patronymic;

    @JsonProperty
    @Pattern(regexp = "^[\\w-]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$",
            message = "email введен неккоректно")
    private String email;

    @JsonProperty
    private String phoneNumber;
}
