package br.com.ufg.orm.useCase.usuario;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.UsuarioRepository;
import br.com.ufg.orm.useCase.UseCase;
import br.com.ufg.orm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncluirUsuario implements UseCase<Usuario, Usuario> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario executar(Usuario usuario) {
        validar(usuario);
        // Criptografar a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validar(Usuario usuario) {
        if (usuario == null) {
            throw new NegocioException("Usuário não pode ser null.");
        }

        if (StringUtil.isNUllOuVazio(usuario.getEmail())) {
            throw new NegocioException("Email do usuário não pode ser vazio.");
        }

        if (StringUtil.isNUllOuVazio(usuario.getNome())) {
            throw new NegocioException("Nome do usuário não pode ser vazio.");
        }

        if (usuarioRepository.existsByEmail((usuario.getEmail()))) {
            throw new NegocioException("Já existe um usuário cadastrado com o email: " + usuario.getEmail());
        }

        if (StringUtil.isNUllOuVazio(usuario.getCpf())) {
            throw new NegocioException("CPF do usuário não pode ser vazio.");
        }

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new NegocioException("Já existe um usuário cadastrado com o CPF: " + usuario.getCpf());
        }

        if (StringUtil.isNUllOuVazio(usuario.getLogin())){
            throw new NegocioException("Login do usuário não pode ser vazio.");
        }

        if (usuarioRepository.existsByLogin(usuario.getLogin())) {
            throw new NegocioException("Já existe um usuário cadastrado com o login: " + usuario.getLogin());
        }

        validarSenha(usuario.getSenha());
    }

    private void validarSenha(String senha) {
        if (StringUtil.isNUllOuVazio(senha)) {
            throw new NegocioException("Senha do usuário não pode ser vazia.");
        }

        if (senha.length() < 6) {
            throw new NegocioException("Senha do usuário deve ter pelo menos 6 caracteres.");
        }
    }
}

