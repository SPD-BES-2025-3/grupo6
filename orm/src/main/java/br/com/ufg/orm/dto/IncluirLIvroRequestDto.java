package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Livro;

public record IncluirLIvroRequestDto(
        Long id,
        String nome,
        String anoLancamento,
        String autor,
        String editora
) {
    public Livro toLivro() {
        return Livro.builder()
                .id(id)
                .nome(nome)
                .anoLancamento(anoLancamento)
                .autor(autor)
                .editora(editora)
                .build();
    }
}
