package br.com.ufg.orm.model;

import br.com.ufg.orm.enums.Conservacao;
import br.com.ufg.orm.enums.Disponibilidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EXEMPLARES")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_EXEMPLAR")
    private Long id;

    @JoinColumn(name = "ID_LIVRO", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Livro livro;

    @Column(name = "CODIGO_IDENTIFICACAO", nullable = false)
    private String codigoIdentificacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONSERVACAO", nullable = false)
    private Conservacao conservacao;

    @Column(name = "NUMERO_EDICAO", nullable = false)
    private Integer numeroEdicao;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISPONIBILIDADE", nullable = false)
    private Disponibilidade disponibilidade;

    @CreatedDate
    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;
}
