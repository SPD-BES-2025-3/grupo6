package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Emprestimo;

import java.time.LocalDateTime;

public record EmprestimoPublisherDto(
    Long idOrm,
    String nomeUsuario,
    Long idOrmExemplar,
    LocalDateTime dataEmprestimo,
    LocalDateTime dataPrevistaDevolucao,
    LocalDateTime dataDevolucao,
    String status,
    Integer renovacoes
) {
    public static EmprestimoPublisherDto from(Emprestimo emprestimo) {
        return new EmprestimoPublisherDto(
            emprestimo.getId(),
            emprestimo.getUsuario() != null ? emprestimo.getUsuario().getNome() : null,
            emprestimo.getExemplar() != null ? emprestimo.getExemplar().getId() : null,
            emprestimo.getDataEmprestimo(),
            emprestimo.getDataPrevistaDevolucao(),
            emprestimo.getDataDevolucao(),
            emprestimo.getStatus().name(),
            emprestimo.getRenovacoes()
        );
    }
}
