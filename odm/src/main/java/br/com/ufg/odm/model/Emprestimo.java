package br.com.ufg.odm.model;

import br.com.ufg.odm.enums.StatusEmprestimo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "emprestimos")
public class Emprestimo {

    @Id
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
