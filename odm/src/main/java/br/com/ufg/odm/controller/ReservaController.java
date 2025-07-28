package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.ReservaDTO;
import br.com.ufg.odm.model.Reserva;
import br.com.ufg.odm.repository.ReservaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reserva")
@Tag(name = "Reservas", description = "API para gerenciamento de reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping
    @Operation(summary = "Listar todas as reservas", description = "Retorna uma lista com todas as reservas cadastradas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<Reserva> reservas = reservaRepository.findAll();

        List<ReservaDTO> reservasDTO = reservas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservasDTO);
    }

    private ReservaDTO convertToDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setIdOrm(reserva.getIdOrm());
        dto.setNomeUsuario(reserva.getNomeUsuario());
        dto.setIdOrmExemplar(reserva.getIdOrmExemplar());
        dto.setStatusReserva(reserva.getStatusReserva());
        return dto;
    }
}
