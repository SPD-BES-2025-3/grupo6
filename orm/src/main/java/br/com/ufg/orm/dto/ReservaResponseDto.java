package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.*;

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
                getExemplar(reserva.getEmprestimo()),
                reserva.getDataReserva(),
                reserva.getDataPrevistaRetirada(),
                reserva.getDataLimiteRetirada(),
                reserva.getDataRetirada(),
                reserva.getStatusReserva().name(),
                reserva.getObservacoes(),
                reserva.getEmprestimo() != null ? reserva.getEmprestimo().getId() : null
        );
    }

    private static Exemplar getExemplar(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getExemplar() == null) return null;
        Exemplar exemplar = emprestimo.getExemplar();
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
