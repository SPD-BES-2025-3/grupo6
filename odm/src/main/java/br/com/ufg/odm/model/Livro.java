package br.com.ufg.odm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "livros")
public class Livro {

    @Id
    private String id;
    private Long idOrm;
    private String nome;
    private String anoLancamento;
    private String autor;
    private String editora;

    @DBRef
    private List<Exemplar> exemplares;
}
