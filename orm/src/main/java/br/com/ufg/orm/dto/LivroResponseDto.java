package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Livro;

import java.util.List;
import java.util.stream.StreamSupport;

public record LivroResponseDto(
        Long id,
        String nome,
        String anoLancamento,
        String autor,
        String editora,
        Integer quantidadeExemplares
) {
    public static LivroResponseDto from(Livro livro) {
        return new LivroResponseDto(
                livro.getId(),
                livro.getNome(),
                livro.getAnoLancamento(),
                livro.getAutor(),
                livro.getEditora(),
                livro.getExemplares() != null ? livro.getExemplares().size() : 0
        );
    }

    public static List<LivroResponseDto> from(Iterable<Livro> livros) {
        return StreamSupport.stream(livros.spliterator(), false)
                .map(LivroResponseDto::from)
                .toList();
    }
}
