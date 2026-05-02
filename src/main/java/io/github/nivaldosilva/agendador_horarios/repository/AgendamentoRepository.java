package io.github.nivaldosilva.agendador_horarios.repository;

import io.github.nivaldosilva.agendador_horarios.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findByServicoAndDataHoraAgendadaBetween(
            String servico, LocalDateTime inicio, LocalDateTime fim);

    Agendamento findByCodigoAgendamento(String codigoAgendamento);
    List<Agendamento> findByNomeClienteContainingIgnoreCase(String nome);
    List<Agendamento> findByDataHoraAgendadaBetween(LocalDateTime inicio, LocalDateTime fim);
}
