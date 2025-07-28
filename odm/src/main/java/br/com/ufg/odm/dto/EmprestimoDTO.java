package br.com.ufg.odm.dto;

import br.com.ufg.odm.enums.StatusEmprestimo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDTO {
    private String id;
    private Long idOrm;
    private String nomeUsuario;
    private String idOrmExemplar;
    private String dataEmprestimo;
    private String dataDevolucaoPrevista;
    private String dataDevolucao;
    private StatusEmprestimo status;
    private Integer renovacoes;
}
