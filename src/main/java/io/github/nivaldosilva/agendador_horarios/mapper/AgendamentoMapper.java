package io.github.nivaldosilva.agendador_horarios.mapper;

import io.github.nivaldosilva.agendador_horarios.dto.AgendamentoDTO;
import io.github.nivaldosilva.agendador_horarios.entity.Agendamento;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoDTO.AgendamentoCreateDTO dto) {
        return Agendamento.builder()
                .nomeCliente(dto.nomeCliente())
                .telefoneCliente(dto.telefoneCliente())
                .emailCliente(dto.emailCliente())
                .profissional(dto.profissional())
                .servico(dto.servico())
                .duracaoMinutos(dto.duracaoMinutos())
                .observacao(dto.observacao())
                .dataHoraAgendada(dto.dataHoraAgendada())
                .build();
    }

    public static AgendamentoDTO.AgendamentoResponseDTO toResponse(Agendamento agendamento) {
        return AgendamentoDTO.AgendamentoResponseDTO.builder()
                .codigoAgendamento(agendamento.getCodigoAgendamento())
                .nomeCliente(agendamento.getNomeCliente())
                .telefoneCliente(agendamento.getTelefoneCliente())
                .emailCliente(agendamento.getEmailCliente())
                .profissional(agendamento.getProfissional())
                .servico(agendamento.getServico())
                .duracaoMinutos(agendamento.getDuracaoMinutos())
                .observacao(agendamento.getObservacao())
                .dataHoraAgendada(agendamento.getDataHoraAgendada())
                .status(agendamento.getStatus())
                .build();
    }
}