package com.dev.popbank.mapper;

import com.dev.popbank.model.auth.ContaEntity;
import com.dev.popbank.model.dto.conta.ContaRequest;
import com.dev.popbank.model.dto.conta.ContaResponse;
import org.springframework.data.domain.Page;

public interface ContaMapper {
    ContaResponse toContaResponse(ContaEntity contaEntity);
    ContaEntity toContaEntity(ContaRequest contaRequest);
    Page<ContaResponse> toContaResponsePage(Page<ContaEntity> contaEntities);
}
