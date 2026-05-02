package io.github.nivaldosilva.agendador_horarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AgendamentoNaoEncontradoException extends RuntimeException {

    public AgendamentoNaoEncontradoException(String codigo) {
        super(String.format("Agendamento com código '%s' não foi encontrado.", codigo));
    }
}
