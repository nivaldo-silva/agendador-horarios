package io.github.nivaldosilva.agendador_horarios.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.agendador_horarios.entity.enums.StatusAgendamento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDateTime;

public class AgendamentoDTO {


    @Builder
    public record AgendamentoCreateDTO(

            @JsonProperty("nome_cliente")
            @NotBlank
            String nomeCliente,

            @JsonProperty("telefone_cliente")
            @NotBlank
            String telefoneCliente,

            @JsonProperty("email_cliente")
            @NotBlank @Email
            String emailCliente,

            @NotBlank @Length(max = 100)
            String profissional,

            @NotBlank @Length(max = 100)
            String servico,

            @JsonProperty("duracao_minutos")
            Integer duracaoMinutos,

            String observacao,

            @NotNull
            @JsonProperty("data_hora_agendada")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
            LocalDateTime dataHoraAgendada
    ) {}


    @Builder
    public record AgendamentoUpdateDTO(

            @JsonProperty("nome_cliente")
            String nomeCliente,

            @JsonProperty("telefone_cliente")
            String telefoneCliente,

            @JsonProperty("email_cliente")
            @Email
            String emailCliente,

            @Length(max = 100)
            String profissional,

            @Length(max = 100)
            String servico,

            @JsonProperty("duracao_minutos")
            Integer duracaoMinutos,

            String observacao,

            @JsonProperty("data_hora_agendada")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
            LocalDateTime dataHoraAgendada
    ) {}


    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record AgendamentoResponseDTO(

            @JsonProperty("codigo_agendamento")
            String codigoAgendamento,

            @JsonProperty("nome_cliente")
            String nomeCliente,

            @JsonProperty("telefone_cliente")
            String telefoneCliente,

            @JsonProperty("email_cliente")
            String emailCliente,

            String profissional,
            String servico,

            @JsonProperty("duracao_minutos")
            Integer duracaoMinutos,

            String observacao,

            @JsonProperty("data_hora_agendada")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
            LocalDateTime dataHoraAgendada,

            StatusAgendamento status
    ) {}
}