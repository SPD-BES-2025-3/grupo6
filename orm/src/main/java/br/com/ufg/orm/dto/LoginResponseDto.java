package br.com.ufg.orm.dto;

import br.com.ufg.orm.enums.Permissao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String nome;
    private String login;
    private Long id;
    private List<Permissao> permissoes;
}
