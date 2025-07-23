package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Usuario;

import java.util.List;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String login,
        String cpf,
        String email,
        Boolean ativo,
        String dataCadastro,
        List<String> permissoes
) {
    public static UsuarioResponseDto from(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getDataCadastro().toString(),
                usuario.getPermissoes().stream()
                        .map(Enum::name)
                        .toList()
        );
    }

    public static List<UsuarioResponseDto> from(Iterable<Usuario> usuarios) {
        return ((List<Usuario>) usuarios).stream()
                .map(UsuarioResponseDto::from)
                .toList();
    }
}
