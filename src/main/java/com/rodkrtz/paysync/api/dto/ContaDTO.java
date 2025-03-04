package com.rodkrtz.paysync.api.dto;

import com.rodkrtz.paysync.domain.model.SituacaoConta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTO {

    private Long id;

    private LocalDateTime dataVencimento;

    private LocalDateTime dataPagamento;

    private BigDecimal valor;

    private String descricao;

    private SituacaoConta situacao;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;

}
