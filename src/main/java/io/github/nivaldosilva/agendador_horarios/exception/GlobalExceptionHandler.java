package io.github.nivaldosilva.agendador_horarios.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String BASE_URI = "https://api.agendador-horarios.io/problems";

    @ExceptionHandler(HorarioIndisponivelException.class)
    public ResponseEntity<ProblemDetail> handleHorarioIndisponivel(HorarioIndisponivelException ex) {
        log.warn("Conflito de horário: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Horário Indisponível");
        problem.setType(URI.create(BASE_URI + "/horario-indisponivel"));
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(AgendamentoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleAgendamentoNaoEncontrado(AgendamentoNaoEncontradoException ex) {
        log.warn("Agendamento não encontrado: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Agendamento Não Encontrado");
        problem.setType(URI.create(BASE_URI + "/agendamento-nao-encontrado"));
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(AgendamentoJaCanceladoException.class)
    public ResponseEntity<ProblemDetail> handleAgendamentoJaCancelado(AgendamentoJaCanceladoException ex) {
        log.warn("Tentativa de cancelar agendamento já cancelado: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Operação Inválida");
        problem.setType(URI.create(BASE_URI + "/agendamento-ja-cancelado"));
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleMensagemIlegivel(HttpMessageNotReadableException ex) {
        log.warn("Erro de deserialização no corpo da requisição: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Formato inválido no corpo da requisição. Verifique os tipos e formatos dos campos. " +
                        "Datas devem seguir o padrão: dd/MM/yyyy HH:mm:ss"
        );
        problem.setTitle("Requisição Inválida");
        problem.setType(URI.create(BASE_URI + "/requisicao-invalida"));
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFound(NoResourceFoundException ex) {
        log.debug("Recurso não encontrado: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Recurso Não Encontrado");
        problem.setType(URI.create(BASE_URI + "/recurso-nao-encontrado"));
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenerico(Exception ex) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );
        problem.setTitle("Erro Interno");
        problem.setType(URI.create(BASE_URI + "/erro-interno"));
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}