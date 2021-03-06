package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserDto {

    @JsonProperty
    @NotNull
    @NotBlank(message = "Поле не должно быть пустым")
    private String lastName;

    @JsonProperty
    @NotNull
    @NotBlank(message = "Поле не должно быть пустым")
    private String firstName;

    @JsonProperty
    @NotNull
    @NotBlank(message = "Поле не должно быть пустым")
    private String patronymic;

    @JsonProperty
    @NotNull
    @Pattern(regexp = "^[\\w-]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$",
            message = "Email введен неккоректно")
    private String email;

    @JsonProperty
    @NotNull
    @Pattern(regexp = "^(039|050|063|066|067|068|073|091|092|093|094|095|096|097|098|099)\\d{3}\\d{2}\\d{2}$",
            message = "Используйте мобильный номер телефона: 0992001020")
    private String phoneNumber;
}
