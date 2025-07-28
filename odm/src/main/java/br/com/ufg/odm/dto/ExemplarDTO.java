package br.com.ufg.odm.dto;

import br.com.ufg.odm.enums.Conservacao;
import br.com.ufg.odm.enums.Disponibilidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExemplarDTO {
    private String id;
    private Long idOrm;
    private String codigoIdentificacao;
    private Conservacao conservacao;
    private Integer numeroEdicao;
    private Disponibilidade disponibilidade;
    private LocalDateTime dataCriacao;

    // Informações básicas do livro (evitando referência circular)
    private String livroId;
    private String livroNome;
    private String livroAutor;
}
