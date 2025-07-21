package br.com.ufg.orm.useCase.usuario;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InativarUsuarioTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private InativarUsuario inativarUsuario;

    private Usuario usuario;
    private Long idUsuario;

    @BeforeEach
    void setUp() {
        idUsuario = 1L;
        usuario = Usuario.builder()
                .id(idUsuario)
                .nome("João Silva")
                .email("joao@email.com")
                .cpf("12345678901")
                .login("joao.silva")
                .senha("senhaEncriptada")
                .ativo(true)
                .build();
    }

    @Test
    @DisplayName("Deve inativar usuário com sucesso quando ID existir e usuário estiver ativo")
    void deveInativarUsuarioComSucesso() {
        // Given
        when(usuarioRepository.existsById(idUsuario)).thenReturn(true);
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario resultado = inativarUsuario.executar(idUsuario);

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.getAtivo());

        verify(usuarioRepository).existsById(idUsuario);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID for nulo")
    void deveLancarExcecaoQuandoIdForNulo() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class, () -> inativarUsuario.validar(null));
        assertEquals("ID do usuário não pode ser null.", exception.getMessage());

        verify(usuarioRepository, never()).existsById(any());
        verify(usuarioRepository, never()).findById(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Given
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class, () -> inativarUsuario.validar(idUsuario));
        assertEquals("Usuário não encontrado com o ID: " + idUsuario, exception.getMessage());

        verify(usuarioRepository).existsById(idUsuario);
        verify(usuarioRepository, never()).findById(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar executar com ID inexistente")
    void deveLancarExcecaoAoTentarExecutarComIdInexistente() {
        // Given
        Long idInexistente = 999L;
        when(usuarioRepository.existsById(idInexistente)).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> inativarUsuario.executar(idInexistente));
        assertEquals("Usuário não encontrado com o ID: " + idInexistente, exception.getMessage());

        verify(usuarioRepository).existsById(idInexistente);
        verify(usuarioRepository, never()).findById(any());
        verify(usuarioRepository, never()).save(any());
    }
}
