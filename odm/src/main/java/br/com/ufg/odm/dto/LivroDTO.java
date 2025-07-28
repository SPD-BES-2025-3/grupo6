package br.com.ufg.odm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroDTO {
    private String id;
    private Long idOrm;
    private String nome;
    private String anoLancamento;
    private String autor;
    private String editora;
}
