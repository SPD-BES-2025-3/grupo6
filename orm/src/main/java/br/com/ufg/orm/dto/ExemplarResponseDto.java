package br.com.ufg.orm.dto;

import br.com.ufg.orm.enums.Conservacao;
import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.model.Exemplar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ExemplarResponseDto(
        Long id,
        String codigoIdentificacao,
        Conservacao conservacao,
        Integer numeroEdicao,
        Disponibilidade disponibilidade,
        LocalDateTime dataCriacao
) {
    public static ExemplarResponseDto from(Exemplar exemplar) {
        return new ExemplarResponseDto(
                exemplar.getId(),
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
                .collect(Collectors.toList());
    }
}
