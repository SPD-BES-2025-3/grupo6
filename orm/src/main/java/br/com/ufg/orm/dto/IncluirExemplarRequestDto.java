package br.com.ufg.orm.dto;

import br.com.ufg.orm.enums.Conservacao;
import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Livro;

public record IncluirExemplarRequestDto(
        Long id,
        Long idLivro,
        String codigoIdentificacao,
        Conservacao conservacao,
        Integer numeroEdicao,
        Disponibilidade disponibilidade
) {
    public Exemplar toExemplar() {
        return Exemplar.builder()
                .id(id)
                .livro(Livro.builder().id(idLivro).build())
                .codigoIdentificacao(codigoIdentificacao)
                .conservacao(conservacao)
                .numeroEdicao(numeroEdicao)
                .disponibilidade(disponibilidade)
                .build();
    }
}
