package br.com.ufg.odm.model;

import br.com.ufg.odm.enums.Conservacao;
import br.com.ufg.odm.enums.Disponibilidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "exemplares")
public class Exemplar {

    @Id
    private String id;
    private Long idOrm;

    @DBRef
    private Livro livro;

    private String codigoIdentificacao;
    private Conservacao conservacao;
    private Integer numeroEdicao;
    private Disponibilidade disponibilidade;

    @CreatedDate
    private LocalDateTime dataCriacao;
}
