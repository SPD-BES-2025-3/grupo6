package br.com.ufg.odm.model;

import br.com.ufg.odm.enums.StatusEmprestimo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataPrevistaDevolucao;
    private LocalDateTime dataDevolucao;
    private StatusEmprestimo status;
    private Integer renovacoes;
}
