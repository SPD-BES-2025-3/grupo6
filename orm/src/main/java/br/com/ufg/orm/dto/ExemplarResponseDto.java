package br.com.ufg.orm.dto;

import br.com.ufg.orm.enums.Conservacao;
import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.model.Exemplar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

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
        return new ExemplarResponseDto(
                exemplar.getId(),
                exemplar.getLivro().getId(),
                exemplar.getLivro().getNome(),
                exemplar.getLivro().getAutor(),
                exemplar.getCodigoIdentificacao(),
                exemplar.getConservacao(),
                exemplar.getNumeroEdicao(),
                exemplar.getDisponibilidade(),
                exemplar.getDataCriacao()
        );
    }

    public static List<ExemplarResponseDto> from(Iterable<Exemplar> exemplares) {
        return StreamSupport.stream(exemplares.spliterator(), false)
                .map(ExemplarResponseDto::from)
                .toList();
    }
}
