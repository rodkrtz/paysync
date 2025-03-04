package com.rodkrtz.paysync.api.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.rodkrtz.paysync.api.dto.AtualizarSituacaoDTO;
import com.rodkrtz.paysync.api.dto.ContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroPeriodoDTO;
import com.rodkrtz.paysync.domain.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaDTO adicionar(@RequestBody ContaDTO contaDTO) {
        return contaService.salvar(contaDTO);
    }

    @PutMapping("/{contaId}")
    public ContaDTO atualizar(@PathVariable Long contaId, @RequestBody ContaDTO contaDTO) {
        var contaDTOAtual = contaService.buscar(contaId);

        BeanUtils.copyProperties(contaDTO, contaDTOAtual, "id", "dataCadastro");

        return contaService.salvar(contaDTOAtual);
    }

    @PatchMapping("/{contaId}")
    public ContaDTO atualizarSituacao(@PathVariable Long contaId,
                                      @RequestBody @Valid AtualizarSituacaoDTO dto) {
        return contaService.atualizarSituacao(contaId, dto);
    }

    @DeleteMapping("/{contaId}")
    public ResponseEntity<?> remover(@PathVariable Long contaId) {
        contaService.excluir(contaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<ContaDTO> buscarContasPaginada(FiltroContaDTO filtro) {
        return contaService.buscarContasPaginada(filtro);
    }

    @GetMapping("/{contaId}")
    public ContaDTO buscar(@PathVariable Long contaId) {
        return contaService.buscar(contaId);
    }

    @GetMapping("/total-pago")
    public BigDecimal obterTotalPagoPorPeriodo(FiltroPeriodoDTO filtro) {
        return contaService.obterTotalPagoPorPeriodo(filtro);
    }

    @PostMapping("/importar")
    public ResponseEntity<?> importarContas(@RequestParam("file") MultipartFile file) throws CsvValidationException, IOException {
        contaService.importarContasDeCSV(file);
        return ResponseEntity.ok().build();

    }
}
