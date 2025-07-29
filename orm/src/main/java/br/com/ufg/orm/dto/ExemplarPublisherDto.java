package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Exemplar;

public record ExemplarPublisherDto(
        Long idOrm,
        Long idLivroOrm,
        String codigoIdentificacao,
        String conservacao,
        Integer numeroEdicao,
        String disponibilidade
) {
    public static ExemplarPublisherDto from(Exemplar exemplar) {
        return new ExemplarPublisherDto(
                exemplar.getId(),
                exemplar.getLivro() != null ? exemplar.getLivro().getId() : null,
                exemplar.getCodigoIdentificacao(),
                exemplar.getConservacao() != null ? exemplar.getConservacao().name() : null,
                exemplar.getNumeroEdicao(),
                exemplar.getDisponibilidade() != null ? exemplar.getDisponibilidade().name() : null
        );
    }
}
