package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Reserva;

public record ReservaPublisherDto(
    Long idOrm,
    String nomeUsuario,
    Long idOrmExemplar,
    String statusReserva
) {
    public static ReservaPublisherDto from(Reserva reserva) {
        return new ReservaPublisherDto(
            reserva.getId(),
            reserva.getUsuario() != null ? reserva.getUsuario().getNome() : null,
            reserva.getExemplar() != null ? reserva.getExemplar().getId() : null,
            reserva.getStatusReserva().name()
        );
    }
}
