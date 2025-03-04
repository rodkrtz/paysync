package com.rodkrtz.paysync.domain.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.rodkrtz.paysync.api.dto.AtualizarSituacaoDTO;
import com.rodkrtz.paysync.api.dto.ContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroPeriodoDTO;
import com.rodkrtz.paysync.domain.exception.ContaNaoEncontradaException;
import com.rodkrtz.paysync.domain.mapper.ContaMapper;
import com.rodkrtz.paysync.domain.model.Conta;
import com.rodkrtz.paysync.domain.model.SituacaoConta;
import com.rodkrtz.paysync.domain.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public ContaDTO salvar(ContaDTO contaDTO) {
        var conta = ContaMapper.toEntity(contaDTO);

        conta = contaRepository.save(conta);

        return ContaMapper.toDTO(conta);
    }

    public ContaDTO buscar(Long contaId) {
        var conta =  contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontradaException(contaId));

        return ContaMapper.toDTO(conta);
    }

    public void excluir(Long contaId) {
        buscar(contaId);

        contaRepository.deleteById(contaId);
    }

    public ContaDTO atualizarSituacao(Long contaId, AtualizarSituacaoDTO atualizarSituacaoDTO) {
        var conta =  contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontradaException(contaId));

        conta.setSituacao(atualizarSituacaoDTO.getSituacao());

        conta = contaRepository.save(conta);

        return ContaMapper.toDTO(conta);
    }

    public Page<ContaDTO> buscarContasPaginada(FiltroContaDTO filtro) {
        return contaRepository.buscarContasPaginada(filtro);
    }

    public BigDecimal obterTotalPagoPorPeriodo(FiltroPeriodoDTO filtro) {
        return contaRepository.somarTotalPagoPorPeriodo(filtro.getDataInicio(), filtro.getDataFim());
    }

    public void importarContasDeCSV(MultipartFile file) throws IOException, CsvValidationException {
        List<Conta> contas = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] linha;
            while ((linha = csvReader.readNext()) != null) {
                Conta conta = new Conta();
                conta.setDataVencimento(LocalDateTime.parse(linha[0]));
                conta.setDataPagamento(linha[1] != null ? LocalDateTime.parse(linha[1]) : null);
                conta.setValor(new BigDecimal(linha[2]));
                conta.setDescricao(linha[3]);
                conta.setSituacao(SituacaoConta.valueOf(linha[4]));

                contas.add(conta);
            }
        }

        contaRepository.saveAll(contas);
    }
}
