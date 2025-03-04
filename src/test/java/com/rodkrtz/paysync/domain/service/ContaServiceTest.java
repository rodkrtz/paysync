package com.rodkrtz.paysync.domain.service;

import com.opencsv.exceptions.CsvValidationException;
import com.rodkrtz.paysync.api.dto.AtualizarSituacaoDTO;
import com.rodkrtz.paysync.api.dto.ContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroPeriodoDTO;
import com.rodkrtz.paysync.domain.exception.ContaNaoEncontradaException;
import com.rodkrtz.paysync.domain.model.Conta;
import com.rodkrtz.paysync.domain.model.SituacaoConta;
import com.rodkrtz.paysync.domain.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    private ContaDTO contaDTO;
    private Conta conta;
    private AtualizarSituacaoDTO atualizarSituacaoDTO;
    private FiltroContaDTO filtroContaDTO;
    private FiltroPeriodoDTO filtroPeriodoDTO;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        contaDTO = new ContaDTO();
        contaDTO.setId(1L);
        contaDTO.setDescricao("Conta de Luz");
        contaDTO.setValor(new BigDecimal("100.00"));
        contaDTO.setSituacao(SituacaoConta.PENDENTE);

        conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Conta de Luz");
        conta.setValor(new BigDecimal("100.00"));
        conta.setSituacao(SituacaoConta.PENDENTE);

        atualizarSituacaoDTO = new AtualizarSituacaoDTO();
        atualizarSituacaoDTO.setSituacao(SituacaoConta.PAGO);

        filtroContaDTO = new FiltroContaDTO();
        filtroContaDTO.setDescricao("Conta de Luz");

        filtroPeriodoDTO = new FiltroPeriodoDTO();
        filtroPeriodoDTO.setDataInicio(LocalDateTime.now().minusDays(1));
        filtroPeriodoDTO.setDataFim(LocalDateTime.now());

        file = mock(MultipartFile.class);
    }

    @Test
    void testSalvarConta() {
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        ContaDTO result = contaService.salvar(contaDTO);

        assertNotNull(result);
        assertEquals(contaDTO.getId(), result.getId());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void testBuscarConta() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        ContaDTO result = contaService.buscar(1L);

        assertNotNull(result);
        assertEquals(contaDTO.getId(), result.getId());
        verify(contaRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarContaNaoEncontrada() {
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> contaService.buscar(1L));
        verify(contaRepository, times(1)).findById(1L);
    }

    @Test
    void testExcluirConta() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        contaService.excluir(1L);

        verify(contaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExcluirContaNaoEncontrada() {
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> contaService.excluir(1L));

        verify(contaRepository, never()).deleteById(1L);
    }

    @Test
    void testAtualizarSituacao() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        ContaDTO result = contaService.atualizarSituacao(1L, atualizarSituacaoDTO);

        assertNotNull(result);
        assertEquals(SituacaoConta.PAGO, result.getSituacao());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void testBuscarContasPaginadas() {
        Pageable pageable = PageRequest.of(filtroContaDTO.getPage(), filtroContaDTO.getSize());
        Page<ContaDTO> contasPage = new PageImpl<>(Collections.singletonList(contaDTO), pageable, 1);

        when(contaRepository.buscarContasPaginada(filtroContaDTO)).thenReturn(contasPage);

        Page<ContaDTO> result = contaService.buscarContasPaginada(filtroContaDTO);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(contaRepository, times(1)).buscarContasPaginada(filtroContaDTO);
    }

    @Test
    void testObterTotalPagoPorPeriodo() {
        when(contaRepository.somarTotalPagoPorPeriodo(filtroPeriodoDTO.getDataInicio(), filtroPeriodoDTO.getDataFim()))
                .thenReturn(new BigDecimal("200.00"));

        BigDecimal result = contaService.obterTotalPagoPorPeriodo(filtroPeriodoDTO);

        assertNotNull(result);
        assertEquals(new BigDecimal("200.00"), result);
        verify(contaRepository, times(1)).somarTotalPagoPorPeriodo(filtroPeriodoDTO.getDataInicio(), filtroPeriodoDTO.getDataFim());
    }

    @Test
    void testImportarContasDeCSV() throws IOException, CsvValidationException {
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("2023-01-01T00:00:00,2023-01-02T00:00:00,100.00,Conta de Luz,PENDENTE".getBytes()));

        contaService.importarContasDeCSV(file);

        verify(contaRepository, times(1)).saveAll(anyList());
    }

}
