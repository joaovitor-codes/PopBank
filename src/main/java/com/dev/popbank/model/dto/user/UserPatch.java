package com.dev.popbank.model.dto.user;

import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;
import java.util.Optional;

public record UserPatch(
        Optional<String> nome,
        Optional<String> nomeDaMae,
        @CPF
        Optional<String> cpf,
        @Past
        Optional<Date> dataNascimento,
        Optional<String> senha
) {

}
