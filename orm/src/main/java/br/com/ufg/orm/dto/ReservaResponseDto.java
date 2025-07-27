package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Livro;
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
                getExemplar(reserva.getExemplar()),
                reserva.getDataReserva(),
                reserva.getDataPrevistaRetirada(),
                reserva.getDataLimiteRetirada(),
                reserva.getDataRetirada(),
                reserva.getStatusReserva().name(),
                reserva.getObservacoes(),
                reserva.getEmprestimo() != null ? reserva.getEmprestimo().getId() : null
        );
    }

    private static Exemplar getExemplar(Exemplar exemplar) {
        if (exemplar == null) return null;
        return Exemplar.builder()
                .id(exemplar.getId())
                .livro(getLivro(exemplar.getLivro()))
                .disponibilidade(exemplar.getDisponibilidade())
                .build();
    }

    private static Livro getLivro(Livro livro) {
        if (livro == null) return null;
        return Livro.builder()
                .id(livro.getId())
                .nome(livro.getNome())
                .autor(livro.getAutor())
                .editora(livro.getEditora())
                .anoLancamento(livro.getAnoLancamento())
                .build();
    }

    private static Usuario getUsuario(Usuario usuario) {
        if (usuario == null) return null;
        return Usuario.builder().id(usuario.getId()).nome(usuario.getNome()).build();
    }
}
