package com.rodkrtz.paysync.infrastructure.repository.spec;

import com.rodkrtz.paysync.api.dto.FiltroContaDTO;
import com.rodkrtz.paysync.domain.model.Conta;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ContaSpecification {

    public static Specification<Conta> comFiltros(FiltroContaDTO filtro) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (filtro.getDataVencimentoInicio() != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("dataVencimento"), filtro.getDataVencimentoInicio()));
            }

            if (filtro.getDataVencimentoFim() != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("dataVencimento"), filtro.getDataVencimentoFim()));
            }

            if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("descricao")), "%" + filtro.getDescricao().toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
