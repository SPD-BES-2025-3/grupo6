package br.com.ufg.odm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados de um livro")
public class LivroDTO {
    @Schema(description = "ID único do livro no MongoDB", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "ID do livro no sistema ORM", example = "1")
    private Long idOrm;

    @Schema(description = "Nome do livro", example = "Dom Casmurro")
    private String nome;

    @Schema(description = "Ano de lançamento do livro", example = "1899")
    private String anoLancamento;

    @Schema(description = "Autor do livro", example = "Machado de Assis")
    private String autor;

    @Schema(description = "Editora do livro", example = "Editora Garnier")
    private String editora;
}
