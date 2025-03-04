package com.rodkrtz.paysync.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FiltroContaDTO {

    private LocalDateTime dataVencimentoInicio;
    private LocalDateTime dataVencimentoFim;
    private String descricao;

    private int page = 0;
    private int size = 10;
}
