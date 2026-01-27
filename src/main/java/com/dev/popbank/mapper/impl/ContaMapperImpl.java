package com.dev.popbank.mapper.impl;

import com.dev.popbank.mapper.ContaMapper;
import com.dev.popbank.model.auth.ContaEntity;
import com.dev.popbank.model.auth.ContasRole;
import com.dev.popbank.model.dto.conta.ContaRequest;
import com.dev.popbank.model.dto.conta.ContaResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ContaMapperImpl implements ContaMapper {
    @Override
    public ContaResponse toContaResponse(ContaEntity contaEntity) {
        return new ContaResponse(
                contaEntity.getId(),
                contaEntity.getUsuario().getId(),
                contaEntity.getLogin(),
                contaEntity.getRole()
        );
    }

    @Override
    public ContaEntity toContaEntity(ContaRequest contaRequest) {
        return ContaEntity.builder()
                .login(contaRequest.login())
                .senha(contaRequest.password())
                .role(ContasRole.USER)
                .build();
    }

    @Override
    public Page<ContaResponse> toContaResponsePage(Page<ContaEntity> contaEntities) {
        if (contaEntities.isEmpty()) {
            throw new NullPointerException("A lista de contas está vazia");
        }

        return contaEntities.map(this::toContaResponse);
    }
}
