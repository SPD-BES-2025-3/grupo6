package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.model.Usuario;

import java.time.LocalDateTime;

public record ReservaResponseDto (
        Long id,
        Usuario usuario,
        Exemplar exemplar,
        LocalDateTime dataReserva,
        LocalDateTime dataPrevistaRetirada,
        LocalDateTime dataLimiteRetirada,
        LocalDateTime dataRetirada,
        String statusReserva,
        String observacoes,
        Long emprestimoId
){
    public static ReservaResponseDto from(Reserva reserva) {
        return new ReservaResponseDto(
                reserva.getId(),
                getUsuario(reserva.getUsuario()),
                reserva.getExemplar(),
                reserva.getDataReserva(),
                reserva.getDataPrevistaRetirada(),
                reserva.getDataLimiteRetirada(),
                reserva.getDataRetirada(),
                reserva.getStatusReserva().name(),
                reserva.getObservacoes(),
                reserva.getEmprestimo() != null ? reserva.getEmprestimo().getId() : null
        );
    }

    private static Usuario getUsuario(Usuario usuario) {
        if (usuario == null) return null;
        return Usuario.builder().id(usuario.getId()).nome(usuario.getNome()).build();
    }
}
