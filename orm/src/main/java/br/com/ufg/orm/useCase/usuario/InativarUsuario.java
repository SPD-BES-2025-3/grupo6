package br.com.ufg.orm.useCase.usuario;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.UsuarioRepository;
import br.com.ufg.orm.useCase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InativarUsuario implements UseCase<Long, Usuario> {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario executar(Long id) {
        validar(id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado com o ID: " + id));

        usuario.setAtivo(false);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void validar(Long id) {
        if (id == null) {
            throw new NegocioException("ID do usuário não pode ser null.");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new NegocioException("Usuário não encontrado com o ID: " + id);
        }
    }
}
