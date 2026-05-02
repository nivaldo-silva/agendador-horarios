package io.github.nivaldosilva.agendador_horarios.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ErroResponseDTO(

        int status,
        String erro,
        String mensagem,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime timestamp
) {
    public static ErroResponseDTO of(int status, String erro, String mensagem) {
        return new ErroResponseDTO(status, erro, mensagem, LocalDateTime.now());
    }
}
