package io.github.nivaldosilva.agendador_horarios.service;

import io.github.nivaldosilva.agendador_horarios.dto.AgendamentoDTO;
import io.github.nivaldosilva.agendador_horarios.entity.Agendamento;
import io.github.nivaldosilva.agendador_horarios.entity.enums.StatusAgendamento;
import io.github.nivaldosilva.agendador_horarios.exception.HorarioIndisponivelException;
import io.github.nivaldosilva.agendador_horarios.exception.AgendamentoNaoEncontradoException;
import io.github.nivaldosilva.agendador_horarios.exception.AgendamentoJaCanceladoException;
import io.github.nivaldosilva.agendador_horarios.mapper.AgendamentoMapper;
import io.github.nivaldosilva.agendador_horarios.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    @Transactional
    public AgendamentoDTO.AgendamentoResponseDTO salvarAgendamento(AgendamentoDTO.AgendamentoCreateDTO dto) {
        log.info("Processando novo agendamento para o cliente: {}", dto.nomeCliente());

        LocalDateTime horaAgendamento = dto.dataHoraAgendada();
        LocalDateTime horaFim = horaAgendamento.plusMinutes(1);

        boolean horarioOcupado = agendamentoRepository
                .findByServicoAndDataHoraAgendadaBetween(dto.servico(), horaAgendamento, horaFim)
                .isPresent();

        if (horarioOcupado) {
            throw new HorarioIndisponivelException(dto.servico(), horaAgendamento);
        }

        Agendamento agendamento = AgendamentoMapper.toEntity(dto);
        Agendamento salvo = agendamentoRepository.save(agendamento);
        log.info("Agendamento salvo com sucesso | codigo: {} | cliente: {}", salvo.getCodigoAgendamento(), salvo.getNomeCliente());
        return AgendamentoMapper.toResponse(salvo);
    }

    public List<AgendamentoDTO.AgendamentoResponseDTO> buscarComFiltros(String nome, String codigo, LocalDate data) {
        List<Agendamento> agendamentos;

        if (codigo != null && !codigo.isEmpty()) {
            agendamentos = List.of(buscarEntidadePorCodigo(codigo));
        } else if (data != null) {
            agendamentos = buscarEntidadesPorDia(data);
        } else if (nome != null && !nome.isEmpty()) {
            agendamentos = agendamentoRepository.findByNomeClienteContainingIgnoreCase(nome);
        } else {
            agendamentos = agendamentoRepository.findAll();
        }

        return agendamentos.stream()
                .map(AgendamentoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AgendamentoDTO.AgendamentoResponseDTO alterarAgendamento(String codigo, AgendamentoDTO.AgendamentoUpdateDTO dto) {
        Agendamento existente = buscarEntidadePorCodigo(codigo);

        if (dto.dataHoraAgendada() != null) {
            LocalDateTime novaHora = dto.dataHoraAgendada();
            LocalDateTime horaFim = novaHora.plusMinutes(1);

            agendamentoRepository
                    .findByServicoAndDataHoraAgendadaBetween(existente.getServico(), novaHora, horaFim)
                    .ifPresent(conflito -> {
                        if (!conflito.getCodigoAgendamento().equals(codigo)) {
                            throw new HorarioIndisponivelException(existente.getServico(), novaHora);
                        }
                    });

            existente.setDataHoraAgendada(novaHora);
        }

        if (dto.nomeCliente() != null && !dto.nomeCliente().isBlank()) {
            existente.setNomeCliente(dto.nomeCliente());
        }
        if (dto.telefoneCliente() != null && !dto.telefoneCliente().isBlank()) {
            existente.setTelefoneCliente(dto.telefoneCliente());
        }
        if (dto.emailCliente() != null && !dto.emailCliente().isBlank()) {
            existente.setEmailCliente(dto.emailCliente());
        }
        if (dto.profissional() != null && !dto.profissional().isBlank()) {
            existente.setProfissional(dto.profissional());
        }
        if (dto.servico() != null && !dto.servico().isBlank()) {
            existente.setServico(dto.servico());
        }
        if (dto.duracaoMinutos() != null) {
            existente.setDuracaoMinutos(dto.duracaoMinutos());
        }
        if (dto.observacao() != null) {
            existente.setObservacao(dto.observacao());
        }

        Agendamento atualizado = agendamentoRepository.save(existente);
        log.info("Agendamento atualizado com sucesso | codigo: {} | cliente: {}", atualizado.getCodigoAgendamento(), atualizado.getNomeCliente());
        return AgendamentoMapper.toResponse(atualizado);
    }

    @Transactional
    public AgendamentoDTO.AgendamentoResponseDTO atualizarStatus(String codigo, StatusAgendamento novoStatus) {
        Agendamento agendamento = buscarEntidadePorCodigo(codigo);
        agendamento.setStatus(novoStatus);
        log.info("Status atualizado | codigo: {} | novoStatus: {}", codigo, novoStatus);
        return AgendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoDTO.AgendamentoResponseDTO cancelarAgendamento(String codigo) {
        Agendamento agendamento = buscarEntidadePorCodigo(codigo);
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new AgendamentoJaCanceladoException(codigo);
        }
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        log.info("Agendamento cancelado | codigo: {}", codigo);
        return AgendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    private Agendamento buscarEntidadePorCodigo(String codigo) {
        Agendamento agendamento = agendamentoRepository.findByCodigoAgendamento(codigo);
        if (agendamento == null) {
            throw new AgendamentoNaoEncontradoException(codigo);
        }
        return agendamento;
    }

    private List<Agendamento> buscarEntidadesPorDia(LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(23, 59, 59);
        return agendamentoRepository.findByDataHoraAgendadaBetween(inicioDia, fimDia);
    }
}