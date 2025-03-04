package com.rodkrtz.paysync.domain.mapper;

import com.rodkrtz.paysync.api.dto.ContaDTO;
import com.rodkrtz.paysync.domain.model.Conta;

public class ContaMapper {

    public static Conta toEntity(ContaDTO contaDTO) {
        return new Conta(
                contaDTO.getId(),
                contaDTO.getDataVencimento(),
                contaDTO.getDataPagamento(),
                contaDTO.getValor(),
                contaDTO.getDescricao(),
                contaDTO.getSituacao(),
                contaDTO.getDataCadastro(),
                contaDTO.getDataAtualizacao()
        );
    }

    public static ContaDTO toDTO(Conta conta) {
        return new ContaDTO(
                conta.getId(),
                conta.getDataVencimento(),
                conta.getDataPagamento(),
                conta.getValor(),
                conta.getDescricao(),
                conta.getSituacao(),
                conta.getDataCadastro(),
                conta.getDataAtualizacao()
        );
    }
}
