package io.github.nivaldosilva.agendador_horarios.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ExceptionDetails(

        int status,
        String erro,
        String mensagem,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime timestamp
) {
    public static ExceptionDetails of(int status, String erro, String mensagem) {
        return new ExceptionDetails(status, erro, mensagem, LocalDateTime.now());
    }
}
