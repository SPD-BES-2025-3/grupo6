package br.com.ufg.orm.config;

import br.com.ufg.orm.enums.Permissao;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.UsuarioRepository;
import br.com.ufg.orm.useCase.usuario.IncluirUsuario;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UsuarioRepository usuarioRepository;
    private final IncluirUsuario incluirUsuario;

    @PostConstruct
    public void init(){
        if (!usuarioRepository.existsByLogin("admin")){
            log.info("Incluindo usuário administrador padrão...");
            var admin = Usuario.builder()
                    .nome("Administrador")
                    .login("admin")
                    .senha("admin123")
                    .email("admin@administrador.com")
                    .cpf("123.456.789-01")
                    .ativo(true)
                    .permissoes(Arrays.asList(Permissao.values()))
                    .build();
            incluirUsuario.executar(admin);
        }
    }
}
