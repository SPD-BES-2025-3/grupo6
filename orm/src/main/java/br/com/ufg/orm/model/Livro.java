package br.com.ufg.orm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LIVROS")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_LIVRO")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "ANO_LANCAMENTO")
    private String anoLancamento;

    @Column(name = "AUTOR")
    private String autor;

    @Column(name = "EDITORA")
    private String editora;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exemplar> exemplares;

}
