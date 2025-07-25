package br.com.ufg.orm.controller;

import br.com.ufg.orm.dto.ReservaRequestDto;
import br.com.ufg.orm.dto.ReservaResponseDto;
import br.com.ufg.orm.enums.StatusReserva;
import br.com.ufg.orm.repository.ReservaRepository;
import br.com.ufg.orm.useCase.reserva.ReservarExemplar;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserva")
@Tag(name = "Reservas", description = "Operações relacionadas ao gerenciamento de reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final ReservarExemplar reservarExemplar;

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID", description = "Retorna uma reserva específica baseada no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<ReservaResponseDto> getReservaById(
            @Parameter(description = "ID da reserva a ser buscada", required = true)
            @PathVariable("id") Long id) {
        return reservaRepository.findById(id)
                .map(reserva -> ResponseEntity.ok(ReservaResponseDto.from(reserva)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional(readOnly = true)
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar reservas por ID do usuário", description = "Retorna todas as reservas de um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reservas retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<ReservaResponseDto>> getReservasByUsuarioId(
            @Parameter(description = "ID do usuário para buscar suas reservas", required = true)
            @PathVariable("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(reservaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ReservaResponseDto::from)
                .toList());
    }

    @PostMapping
    @Operation(summary = "Criar nova reserva", description = "Cria uma nova reserva de exemplar no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva criada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<ReservaResponseDto> incluirReserva(
            @Parameter(description = "Dados da reserva a ser criada", required = true)
            @RequestBody ReservaRequestDto requestDto){
        return ResponseEntity.ok(ReservaResponseDto.from(reservarExemplar.executar(requestDto.toReserva())));
    }

    @Transactional
    @PutMapping("/cancelar/{id}")
    @Operation(summary = "Cancelar reserva", description = "Cancela uma reserva existente alterando seu status para CANCELADA")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva cancelada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<ReservaResponseDto> cancelarReserva(
            @Parameter(description = "ID da reserva a ser cancelada", required = true)
            @PathVariable("id") Long id) {
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reserva.setStatusReserva(StatusReserva.CANCELADA);
                    return ResponseEntity.ok(ReservaResponseDto.from(reservaRepository.save(reserva)));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
