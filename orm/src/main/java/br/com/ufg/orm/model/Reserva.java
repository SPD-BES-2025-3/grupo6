package br.com.ufg.orm.model;

import br.com.ufg.orm.enums.StatusReserva;
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
@Table(name = "RESERVAS")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_RESERVA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXEMPLAR", nullable = false)
    private Exemplar exemplar;

    @Column(name = "DATA_RESERVA", nullable = false)
    private LocalDateTime dataReserva;

    @Column(name = "DATA_PREVISTA_RETIRADA", nullable = false)
    private LocalDateTime dataPrevistaRetirada;

    @Column(name = "DATA_LIMITE_RETIRADA", nullable = false)
    private LocalDateTime dataLimiteRetirada;

    @Column(name = "DATA_RETIRADA")
    private LocalDateTime dataRetirada;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_RESERVA", nullable = false)
    private StatusReserva statusReserva;

    @Column(name = "OBSERVACOES")
    private String observacoes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPRESTIMO")
    private Emprestimo emprestimo;


}
