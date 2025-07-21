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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncluirUsuarioTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private IncluirUsuario incluirUsuario;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        usuarioValido = Usuario.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .cpf("12345678901")
                .login("joao.silva")
                .senha("123456")
                .build();
    }

    @Test
    @DisplayName("Deve incluir usuário com sucesso quando todos os dados são válidos")
    void deveIncluirUsuarioComSucesso() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);

        // When
        Usuario resultado = incluirUsuario.executar(usuarioValido);

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioValido.getNome(), resultado.getNome());
        assertEquals(usuarioValido.getEmail(), resultado.getEmail());
        assertEquals(usuarioValido.getCpf(), resultado.getCpf());
        assertEquals(usuarioValido.getLogin(), resultado.getLogin());

        verify(usuarioRepository).existsByEmail(usuarioValido.getEmail());
        verify(usuarioRepository).existsByCpf(usuarioValido.getCpf());
        verify(usuarioRepository).existsByLogin(usuarioValido.getLogin());
        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(usuarioValido);
    }

    @Test
    @DisplayName("Deve criptografar a senha antes de salvar o usuário")
    void deveCriptografarSenhaAntesDeSalvar() {
        // Given
        String senhaOriginal = "minhasenha123";
        String senhaCriptografada = "$2a$10$xyz123..."; // Exemplo de hash BCrypt

        usuarioValido.setSenha(senhaOriginal);

        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(senhaOriginal)).thenReturn(senhaCriptografada);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);

        // When
        incluirUsuario.executar(usuarioValido);

        // Then
        verify(passwordEncoder).encode(senhaOriginal);
        assertEquals(senhaCriptografada, usuarioValido.getSenha());
        verify(usuarioRepository).save(usuarioValido);
    }

    @Test
    @DisplayName("Deve garantir que a senha não seja salva em texto plano")
    void deveGarantirQueSenhaNaoSejaSalvaEmTextoPlano() {
        // Given
        String senhaOriginal = "senhaSimples";
        String senhaCriptografada = "$2a$10$hash.criptografado.diferente";

        usuarioValido.setSenha(senhaOriginal);

        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(senhaOriginal)).thenReturn(senhaCriptografada);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioParaSalvar = invocation.getArgument(0);
            // Verificar que a senha foi alterada antes de salvar
            assertNotEquals(senhaOriginal, usuarioParaSalvar.getSenha());
            assertEquals(senhaCriptografada, usuarioParaSalvar.getSenha());
            return usuarioParaSalvar;
        });

        // When
        incluirUsuario.executar(usuarioValido);

        // Then
        verify(passwordEncoder).encode(senhaOriginal);
        verify(usuarioRepository).save(usuarioValido);

        // Garantir que a senha foi realmente alterada no objeto
        assertNotEquals(senhaOriginal, usuarioValido.getSenha());
        assertEquals(senhaCriptografada, usuarioValido.getSenha());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário for null")
    void deveLancarExcecaoQuandoUsuarioForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(null));

        assertEquals("Usuário não pode ser null.", exception.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email for null")
    void deveLancarExcecaoQuandoEmailForNull() {
        // Given
        usuarioValido.setEmail(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Email do usuário não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email for vazio")
    void deveLancarExcecaoQuandoEmailForVazio() {
        // Given
        usuarioValido.setEmail("");

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Email do usuário não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for null")
    void deveLancarExcecaoQuandoNomeForNull() {
        // Given
        usuarioValido.setNome(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Nome do usuário não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for vazio")
    void deveLancarExcecaoQuandoNomeForVazio() {
        // Given
        usuarioValido.setNome("");

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Nome do usuário não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existir")
    void deveLancarExcecaoQuandoEmailJaExistir() {
        // Given
        when(usuarioRepository.existsByEmail(usuarioValido.getEmail())).thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Já existe um usuário cadastrado com o email: " + usuarioValido.getEmail(),
            exception.getMessage());

        verify(usuarioRepository).existsByEmail(usuarioValido.getEmail());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for null")
    void deveLancarExcecaoQuandoCpfForNull() {
        // Given
        usuarioValido.setCpf(null);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("CPF do usuário não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF for vazio")
    void deveLancarExcecaoQuandoCpfForVazio() {
        // Given
        usuarioValido.setCpf("");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("CPF do usuário não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF já existir")
    void deveLancarExcecaoQuandoCpfJaExistir() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuarioValido.getCpf())).thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Já existe um usuário cadastrado com o CPF: " + usuarioValido.getCpf(),
            exception.getMessage());

        verify(usuarioRepository).existsByCpf(usuarioValido.getCpf());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando login for null")
    void deveLancarExcecaoQuandoLoginForNull() {
        // Given
        usuarioValido.setLogin(null);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Login do usuário não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando login for vazio")
    void deveLancarExcecaoQuandoLoginForVazio() {
        // Given
        usuarioValido.setLogin("");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Login do usuário não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando login já existir")
    void deveLancarExcecaoQuandoLoginJaExistir() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(usuarioValido.getLogin())).thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Já existe um usuário cadastrado com o login: " + usuarioValido.getLogin(),
            exception.getMessage());

        verify(usuarioRepository).existsByLogin(usuarioValido.getLogin());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha for null")
    void deveLancarExcecaoQuandoSenhaForNull() {
        // Given
        usuarioValido.setSenha(null);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Senha do usuário não pode ser vazia.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha for vazia")
    void deveLancarExcecaoQuandoSenhaForVazia() {
        // Given
        usuarioValido.setSenha("");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Senha do usuário não pode ser vazia.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha tiver menos de 6 caracteres")
    void deveLancarExcecaoQuandoSenhaTiverMenosDe6Caracteres() {
        // Given
        usuarioValido.setSenha("12345");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> incluirUsuario.executar(usuarioValido));

        assertEquals("Senha do usuário deve ter pelo menos 6 caracteres.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar senha com exatamente 6 caracteres")
    void deveAceitarSenhaComExatamente6Caracteres() {
        // Given
        usuarioValido.setSenha("123456");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);

        // When & Then
        assertDoesNotThrow(() -> incluirUsuario.executar(usuarioValido));
        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(usuarioValido);
    }

    @Test
    @DisplayName("Deve aceitar senha com mais de 6 caracteres")
    void deveAceitarSenhaComMaisDe6Caracteres() {
        // Given
        usuarioValido.setSenha("1234567890");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);

        // When & Then
        assertDoesNotThrow(() -> incluirUsuario.executar(usuarioValido));
        verify(passwordEncoder).encode("1234567890");
        verify(usuarioRepository).save(usuarioValido);
    }
}