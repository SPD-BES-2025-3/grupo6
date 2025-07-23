package br.com.ufg.orm.dto;

import br.com.ufg.orm.enums.Permissao;
import br.com.ufg.orm.model.Usuario;

import java.util.List;

public record IncluirUsuarioRequestDto(
        String login,
        String nome,
        String senha,
        String cpf,
        String email,
        List<Permissao> permissoes
) {
    public Usuario toUsuario() {
        return Usuario.builder()
                .login(login)
                .nome(nome)
                .senha(senha)
                .cpf(cpf)
                .email(email)
                .ativo(true)
                .permissoes(permissoes)
                .build();
    }
}
