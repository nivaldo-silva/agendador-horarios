package io.github.nivaldosilva.agendador_horarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class AgendamentoJaCanceladoException extends RuntimeException {

    public AgendamentoJaCanceladoException(String codigo) {
        super(String.format("O agendamento '%s' já se encontra com status CANCELADO.", codigo));
    }
}
