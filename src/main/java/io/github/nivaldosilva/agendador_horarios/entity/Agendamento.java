package io.github.nivaldosilva.agendador_horarios.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.nivaldosilva.agendador_horarios.entity.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agendamentos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento", updatable = false)
    private Long idAgendamento;

    @Column(name = "codigo_agendamento", unique = true, nullable = false, updatable = false)
    private String codigoAgendamento;

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;

    @Column(name = "telefone_cliente", nullable = false)
    private String telefoneCliente;

    @Column(name = "email_cliente", nullable = false,unique = true)
    private String emailCliente;

    @Column(name = "profissional", nullable = false)
    private String profissional;

    @Column(name = "servico", nullable = false)
    private String servico;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    @Column(name = "observacao",columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "data_hora_agendada", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHoraAgendada;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAgendamento status;

    @CreationTimestamp
    @Column(name = "data_hora_criacao_registro", nullable = false)
    private LocalDateTime dataHoraCriacaoRegistro;

    @PrePersist
    public void prePersist() {
        if (this.codigoAgendamento == null) {
            this.codigoAgendamento = gerarCodigoAleatorio();
        }
        if (this.status == null) {
            this.status = StatusAgendamento.AGENDADO;
        }
    }

    private String gerarCodigoAleatorio() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}

