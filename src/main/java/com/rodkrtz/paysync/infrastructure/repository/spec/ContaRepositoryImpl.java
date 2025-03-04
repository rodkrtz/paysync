package com.rodkrtz.paysync.infrastructure.repository.spec;

import com.rodkrtz.paysync.api.dto.ContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroContaDTO;
import com.rodkrtz.paysync.domain.mapper.ContaMapper;
import com.rodkrtz.paysync.domain.model.Conta;
import com.rodkrtz.paysync.domain.repository.ContaRepository;
import com.rodkrtz.paysync.domain.repository.ContaRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class ContaRepositoryImpl implements ContaRepositoryCustom {

    @Autowired @Lazy
    private ContaRepository contaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ContaDTO> buscarContasPaginada(FiltroContaDTO filtro) {
        Specification<Conta> specification = ContaSpecification.comFiltros(filtro);

        Pageable pageable = PageRequest.of(filtro.getPage(), filtro.getSize());

        Page<Conta> contasPage = contaRepository.findAll(specification, pageable);

        return contasPage.map(ContaMapper::toDTO);
    }
}
