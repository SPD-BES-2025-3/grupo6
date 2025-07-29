package br.com.ufg.odm.dto;

import br.com.ufg.odm.enums.StatusEmprestimo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados de um empréstimo")
public class EmprestimoDTO {
    @Schema(description = "ID único do empréstimo no MongoDB", example = "507f1f77bcf86cd799439013")
    private String id;

    @Schema(description = "ID do empréstimo no sistema ORM", example = "1")
    private Long idOrm;

    @Schema(description = "Nome do usuário que fez o empréstimo", example = "João Silva")
    private String nomeUsuario;

    @Schema(description = "ID do exemplar emprestado no sistema ORM", example = "1")
    private String idOrmExemplar;

    @Schema(description = "Data do empréstimo", example = "2024-01-15")
    private LocalDateTime dataEmprestimo;

    @Schema(description = "Data prevista para devolução", example = "2024-01-29")
    private LocalDateTime dataDevolucaoPrevista;

    @Schema(description = "Data da devolução (se já devolvido)", example = "2024-01-28")
    private LocalDateTime dataDevolucao;

    @Schema(description = "Status atual do empréstimo")
    private StatusEmprestimo status;

    @Schema(description = "Número de renovações realizadas", example = "0")
    private Integer renovacoes;
}
