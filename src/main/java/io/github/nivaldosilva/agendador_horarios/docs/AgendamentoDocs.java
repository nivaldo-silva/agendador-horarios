package io.github.nivaldosilva.agendador_horarios.docs;

import io.github.nivaldosilva.agendador_horarios.dto.AgendamentoDTO;
import io.github.nivaldosilva.agendador_horarios.entity.enums.StatusAgendamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Agendamentos", description = "Endpoints para gerenciamento de agendamentos")
public interface AgendamentoDocs {

    @Operation(
            summary = "Criar agendamento",
            description = "Cria um novo agendamento. Retorna 409 se o horário já estiver ocupado para o serviço informado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Agendamento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.AgendamentoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos na requisição",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Horário indisponível para o serviço informado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            )
    })
    ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> salvar(
            @RequestBody AgendamentoDTO.AgendamentoCreateDTO dto
    );

    @Operation(
            summary = "Buscar agendamentos",
            description = """
            Busca agendamentos com filtros opcionais. Prioridade: código > data > nome > todos.
            A data deve estar no formato dd/MM/yyyy.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de agendamentos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AgendamentoDTO.AgendamentoResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado pelo código informado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            )
    })
    ResponseEntity<List<AgendamentoDTO.AgendamentoResponseDTO>> buscar(
            @Parameter(description = "Nome parcial do cliente (busca case-insensitive)")
            @RequestParam(required = false) String nome,

            @Parameter(description = "Código único do agendamento")
            @RequestParam(required = false) String codigo,

            @Parameter(description = "Data do agendamento no formato dd/MM/yyyy")
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data
    );

    @Operation(
            summary = "Atualizar agendamento",
            description = "Atualiza parcialmente um agendamento existente pelo código. Apenas os campos enviados serão alterados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.AgendamentoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Novo horário indisponível",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            )
    })
    ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> atualizar(
            @Parameter(description = "Código único do agendamento", required = true)
            @PathVariable String codigo,

            @RequestBody AgendamentoDTO.AgendamentoUpdateDTO dto
    );

    @Operation(
            summary = "Cancelar agendamento",
            description = "Cancela um agendamento pelo código. Retorna erro se já estiver cancelado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento cancelado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.AgendamentoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Agendamento já está cancelado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            )
    })
    ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> cancelar(
            @Parameter(description = "Código único do agendamento", required = true)
            @PathVariable String codigo
    );

    @Operation(
            summary = "Atualizar status do agendamento",
            description = "Altera o status de um agendamento. Valores possíveis: AGENDADO, CONFIRMADO, CONCLUIDO, CANCELADO."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.AgendamentoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))
            )
    })
    ResponseEntity<AgendamentoDTO.AgendamentoResponseDTO> atualizarStatus(
            @Parameter(description = "Código único do agendamento", required = true)
            @PathVariable String codigo,

            @Parameter(description = "Novo status do agendamento", required = true, schema = @Schema(implementation = StatusAgendamento.class))
            @RequestParam StatusAgendamento novoStatus
    );
}