package com.rodkrtz.paysync.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodkrtz.paysync.api.dto.AtualizarSituacaoDTO;
import com.rodkrtz.paysync.api.dto.ContaDTO;
import com.rodkrtz.paysync.api.dto.FiltroPeriodoDTO;
import com.rodkrtz.paysync.domain.model.SituacaoConta;
import com.rodkrtz.paysync.domain.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAdicionarConta() throws Exception {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setDescricao("Conta de Luz");
        contaDTO.setValor(new BigDecimal("100.00"));

        when(contaService.salvar(any(ContaDTO.class))).thenReturn(contaDTO);

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Conta de Luz"))
                .andExpect(jsonPath("$.valor").value(100.00));

        verify(contaService, times(1)).salvar(any(ContaDTO.class));
    }

    @Test
    void testAtualizarConta() throws Exception {
        Long contaId = 1L;
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setDescricao("Conta de Luz");
        contaDTO.setValor(new BigDecimal("150.00"));

        when(contaService.buscar(contaId)).thenReturn(contaDTO);
        when(contaService.salvar(any(ContaDTO.class))).thenReturn(contaDTO);

        mockMvc.perform(put("/contas/{contaId}", contaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Conta de Luz"))
                .andExpect(jsonPath("$.valor").value(150.00));

        verify(contaService, times(1)).buscar(contaId);
        verify(contaService, times(1)).salvar(any(ContaDTO.class));
    }

    @Test
    void testAtualizarSituacao() throws Exception {
        Long contaId = 1L;
        AtualizarSituacaoDTO dto = new AtualizarSituacaoDTO();
        dto.setSituacao(SituacaoConta.PAGO);

        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setId(contaId);
        contaDTO.setSituacao(SituacaoConta.PAGO);

        when(contaService.atualizarSituacao(contaId, dto)).thenReturn(contaDTO);

        mockMvc.perform(patch("/contas/{contaId}", contaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.situacao").value("PAGO"));

        verify(contaService, times(1)).atualizarSituacao(contaId, dto);
    }

    @Test
    void testRemoverConta() throws Exception {
        Long contaId = 1L;

        mockMvc.perform(delete("/contas/{contaId}", contaId))
                .andExpect(status().isNoContent());

        verify(contaService, times(1)).excluir(contaId);
    }

    /*@Test
    void testBuscarContasPaginada() throws Exception {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setId(1L);
        contaDTO.setSituacao(SituacaoConta.PAGO);

        Page<ContaDTO> contasPage = new PageImpl<>(Collections.singletonList(contaDTO));

        when(contaService.buscarContasPaginada(any(FiltroContaDTO.class))).thenReturn(contasPage);

        mockMvc.perform(get("/contas")
                        .param("page", "0")
                        .param("size", "10")
                        .param("descricao", "teste")
                        .param("dataVencimentoInicio", "2025-03-10T12:00:00")
                        .param("dataVencimentoFim", "2025-03-10T12:00:00"))
                .andDo(print())
                .andDo(result -> System.out.println("Exceção resolvida: " + result.getResolvedException()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(contaService, times(1)).buscarContasPaginada(any(FiltroContaDTO.class));
    }*/

    @Test
    void testBuscarPorId() throws Exception {
        Long contaId = 1L;
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setId(contaId);
        contaDTO.setDescricao("Conta de Luz");

        when(contaService.buscar(contaId)).thenReturn(contaDTO);

        mockMvc.perform(get("/contas/{contaId}", contaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Conta de Luz"));

        verify(contaService, times(1)).buscar(contaId);
    }

    @Test
    void testObterTotalPagoPorPeriodo() throws Exception {
        FiltroPeriodoDTO filtro = new FiltroPeriodoDTO();
        filtro.setDataInicio(LocalDateTime.now().minusDays(1));
        filtro.setDataFim(LocalDateTime.now());

        when(contaService.obterTotalPagoPorPeriodo(filtro)).thenReturn(new BigDecimal("200.00"));

        mockMvc.perform(get("/contas/total-pago")
                        .param("dataInicio", filtro.getDataInicio().toString())
                        .param("dataFim", filtro.getDataFim().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("200.00"));

        verify(contaService, times(1)).obterTotalPagoPorPeriodo(filtro);
    }

    @Test
    void testImportarContas() throws Exception {
        String csvContent = "2024-03-01T12:00:00,2024-03-05T12:00:00,1000.00,Pagamento de fornecedor,PENDENTE";
        MockMultipartFile file = new MockMultipartFile(
                "file", "contas.csv", "text/csv", csvContent.getBytes());
        mockMvc.perform(multipart("/contas/importar")
                        .file(file))
                .andExpect(status().isOk());

        verify(contaService, times(1)).importarContasDeCSV(any(MultipartFile.class));
    }
}
