package com.rodkrtz.paysync.api.dto;

import com.rodkrtz.paysync.domain.model.SituacaoConta;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarSituacaoDTO {

    @NotNull
    private SituacaoConta situacao;
}
