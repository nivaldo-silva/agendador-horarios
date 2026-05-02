package io.github.nivaldosilva.agendador_horarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ResponseStatus(HttpStatus.CONFLICT)
public class HorarioIndisponivelException extends RuntimeException {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public HorarioIndisponivelException(String servico, LocalDateTime dataHora) {
        super(String.format(
                "Horário %s já está ocupado para o serviço '%s'. Por favor, escolha outro horário.",
                dataHora.format(FORMATTER), servico
        ));
    }
}
