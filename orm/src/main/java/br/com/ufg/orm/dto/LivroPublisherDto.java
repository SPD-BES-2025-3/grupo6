package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Livro;

public record LivroPublisherDto(
        Long idOrm,
        String nome,
        String anoLancamento,
        String autor,
        String editora
) {
    public static LivroPublisherDto from(Livro livro) {
        return new LivroPublisherDto(
                livro.getId(),
                livro.getNome(),
                livro.getAnoLancamento(),
                livro.getAutor(),
                livro.getEditora()
        );
    }
}
