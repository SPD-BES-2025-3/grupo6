package br.com.ufg.odm.dto;

import br.com.ufg.odm.enums.StatusReserva;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados de uma reserva")
public class ReservaDTO {
    @Schema(description = "ID único da reserva no MongoDB", example = "507f1f77bcf86cd799439014")
    private String id;

    @Schema(description = "ID da reserva no sistema ORM", example = "1")
    private Long idOrm;

    @Schema(description = "Nome do usuário que fez a reserva", example = "Maria Santos")
    private String nomeUsuario;

    @Schema(description = "ID do exemplar reservado no sistema ORM", example = "1")
    private String idOrmExemplar;

    @Schema(description = "Status atual da reserva")
    private StatusReserva statusReserva;
}
