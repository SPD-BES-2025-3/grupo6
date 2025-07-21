package br.com.ufg.orm.model;

import br.com.ufg.orm.enums.Permissao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "SENHA", nullable = false)
    private String senha;

    @Column(name = "CPF", nullable = false)
    private String cpf;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ATIVO", nullable = false)
    private Boolean ativo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERMISSAO_USUARIO", joinColumns = @JoinColumn(name = "ID_USUARIO"))
    @Enumerated(EnumType.STRING)
    private List<Permissao> permissoes;

    @CreatedDate
    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

}
