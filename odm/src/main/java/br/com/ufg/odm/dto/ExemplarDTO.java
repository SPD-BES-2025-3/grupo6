package br.com.ufg.odm.dto;

import br.com.ufg.odm.enums.Conservacao;
import br.com.ufg.odm.enums.Disponibilidade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados de um exemplar de livro")
public class ExemplarDTO {
    @Schema(description = "ID único do exemplar no MongoDB", example = "507f1f77bcf86cd799439012")
    private String id;

    @Schema(description = "ID do exemplar no sistema ORM", example = "1")
    private Long idOrm;

    @Schema(description = "Código de identificação do exemplar", example = "LIV001-EX001")
    private String codigoIdentificacao;

    @Schema(description = "Estado de conservação do exemplar")
    private Conservacao conservacao;

    @Schema(description = "Número da edição", example = "1")
    private Integer numeroEdicao;

    @Schema(description = "Status de disponibilidade do exemplar")
    private Disponibilidade disponibilidade;

    @Schema(description = "Data de criação do registro", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCriacao;

    // Informações básicas do livro (evitando referência circular)
    @Schema(description = "ID do livro associado", example = "507f1f77bcf86cd799439011")
    private String livroId;

    @Schema(description = "Nome do livro associado", example = "Dom Casmurro")
    private String livroNome;

    @Schema(description = "Autor do livro associado", example = "Machado de Assis")
    private String livroAutor;
}
