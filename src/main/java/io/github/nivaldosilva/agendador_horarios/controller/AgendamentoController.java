package io.github.nivaldosilva.agendador_horarios.controller;

import io.github.nivaldosilva.agendador_horarios.dto.AgendamentoDTO;
import io.github.nivaldosilva.agendador_horarios.entity.enums.StatusAgendamento;
import io.github.nivaldosilva.agendador_horarios.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
@Slf4j
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> salvar(
            @RequestBody @Valid AgendamentoDTO.AgendamentoCreateDTO dto) {
        log.info("Requisição para salvar agendamento recebida");
        return ResponseEntity.accepted().body(agendamentoService.salvarAgendamento(dto));
    }


    @GetMapping
    public ResponseEntity<List<AgendamentoDTO.AgendamentoResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data) {
        log.info("Requisição de busca recebida | nome: {} | codigo: {} | data: {}", nome, codigo, data);
        return ResponseEntity.ok(agendamentoService.buscarComFiltros(nome, codigo, data));
    }

    @PatchMapping("/{codigo}")
    public ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> atualizar(
            @PathVariable String codigo,
            @RequestBody AgendamentoDTO.AgendamentoUpdateDTO dto) {
        log.info("Requisição para atualizar agendamento | codigo: {}", codigo);
        return ResponseEntity.ok(agendamentoService.alterarAgendamento(codigo, dto));
    }

    @PatchMapping("/{codigo}/cancelar")
    public ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> cancelar(@PathVariable String codigo) {
        log.info("Requisição para cancelar agendamento | codigo: {}", codigo);
        return ResponseEntity.ok(agendamentoService.cancelarAgendamento(codigo));
    }

    @PatchMapping("/{codigo}/status")
    public ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> atualizarStatus(
            @PathVariable String codigo,
            @RequestParam StatusAgendamento novoStatus) {
        log.info("Requisição para alterar status | codigo: {} | novoStatus: {}", codigo, novoStatus);
        return ResponseEntity.ok(agendamentoService.atualizarStatus(codigo, novoStatus));
    }
}