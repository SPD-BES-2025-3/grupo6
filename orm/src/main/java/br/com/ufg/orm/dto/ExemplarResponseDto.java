package br.com.ufg.orm.dto;

import br.com.ufg.orm.enums.Conservacao;
import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.model.Exemplar;

import java.time.LocalDateTime;
import java.util.List;

public record ExemplarResponseDto(
        Long id,
        Long livroId,
        String livroNome,
        String livroAutor,
        String codigoIdentificacao,
        Conservacao conservacao,
        Integer numeroEdicao,
        Disponibilidade disponibilidade,
        LocalDateTime dataCriacao
) {
    public static ExemplarResponseDto from(Exemplar exemplar) {
        Long idLivro = null;
        String livroNome = null;
        String livroAutor = null;
        if (exemplar.getLivro() != null) {
            idLivro = exemplar.getLivro().getId();
            livroNome = exemplar.getLivro().getNome();
            livroAutor = exemplar.getLivro().getAutor();
        }
        return new ExemplarResponseDto(
                exemplar.getId(),
                idLivro,
                livroNome,
                livroAutor,
                exemplar.getCodigoIdentificacao(),
                exemplar.getConservacao(),
                exemplar.getNumeroEdicao(),
                exemplar.getDisponibilidade(),
                exemplar.getDataCriacao()
        );
    }

    public static List<ExemplarResponseDto> from(List<Exemplar> exemplares) {
        return exemplares.stream()
                .map(ExemplarResponseDto::from)
                .toList();
    }
}
