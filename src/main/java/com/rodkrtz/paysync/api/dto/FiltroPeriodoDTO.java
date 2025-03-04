package com.rodkrtz.paysync.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FiltroPeriodoDTO {

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
}
