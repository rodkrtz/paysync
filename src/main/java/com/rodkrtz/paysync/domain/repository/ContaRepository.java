package com.rodkrtz.paysync.domain.repository;

import com.rodkrtz.paysync.domain.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>, ContaRepositoryCustom, JpaSpecificationExecutor<Conta> {

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :dataInicio AND :dataFim AND c.situacao = 'PAGO'")
    BigDecimal somarTotalPagoPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);

}
