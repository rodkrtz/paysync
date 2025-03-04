package com.rodkrtz.paysync.domain.repository;

import com.rodkrtz.paysync.api.dto.ContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroContaDTO;
import org.springframework.data.domain.Page;

public interface ContaRepositoryCustom {

    Page<ContaDTO> buscarContasPaginada(FiltroContaDTO filtro);
}
