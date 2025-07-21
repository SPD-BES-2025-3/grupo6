package br.com.ufg.orm.model;

import br.com.ufg.orm.enums.StatusEmprestimo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPRESTIMOS")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_EMPRESTIMO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXEMPLAR", nullable = false)
    private Exemplar exemplar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RESERVA")
    private Reserva reserva;

    @Column(name = "DATA_EMPRESTIMO", nullable = false)
    private LocalDateTime dataEmprestimo;

    @Column(name = "DATA_PREVISTA_DEVOLUCAO", nullable = false)
    private LocalDateTime dataPrevistaDevolucao;

    @Column(name = "DATA_DEVOLUCAO")
    private LocalDateTime dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusEmprestimo status;

    @Column(name = "RENOVACOES")
    private Integer renovacoes;

    @Column(name = "OBSERVACOES")
    private String observacoes;
}
