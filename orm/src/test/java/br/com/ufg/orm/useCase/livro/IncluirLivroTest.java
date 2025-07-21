package br.com.ufg.orm.useCase.livro;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Livro;
import br.com.ufg.orm.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncluirLivroTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private IncluirLivro incluirLivro;

    private Livro livroValido;

    @BeforeEach
    void setUp() {
        livroValido = Livro.builder()
                .nome("Clean Code")
                .autor("Robert C. Martin")
                .editora("Prentice Hall")
                .anoLancamento("2008")
                .build();
    }

    @Test
    @DisplayName("Deve incluir livro com sucesso quando todos os dados são válidos")
    void deveIncluirLivroComSucesso() {
        // Given
        when(livroRepository.existsByNomeAndAutor(anyString(), anyString())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroValido);

        // When
        Livro resultado = incluirLivro.executar(livroValido);

        // Then
        assertNotNull(resultado);
        assertEquals(livroValido.getNome(), resultado.getNome());
        assertEquals(livroValido.getAutor(), resultado.getAutor());
        assertEquals(livroValido.getEditora(), resultado.getEditora());
        assertEquals(livroValido.getAnoLancamento(), resultado.getAnoLancamento());

        verify(livroRepository).existsByNomeAndAutor(livroValido.getNome(), livroValido.getAutor());
        verify(livroRepository).save(livroValido);
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro for null")
    void deveLancarExcecaoQuandoLivroForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(null));

        assertEquals("Livro não pode ser null.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for null")
    void deveLancarExcecaoQuandoNomeForNull() {
        // Given
        livroValido.setNome(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Nome do livro não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for vazio")
    void deveLancarExcecaoQuandoNomeForVazio() {
        // Given
        livroValido.setNome("");

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Nome do livro não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor for null")
    void deveLancarExcecaoQuandoAutorForNull() {
        // Given
        livroValido.setAutor(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Autor do livro não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor for vazio")
    void deveLancarExcecaoQuandoAutorForVazio() {
        // Given
        livroValido.setAutor("");

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Autor do livro não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando editora for null")
    void deveLancarExcecaoQuandoEditoraForNull() {
        // Given
        livroValido.setEditora(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Editora do livro não pode ser vazia.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando editora for vazia")
    void deveLancarExcecaoQuandoEditoraForVazia() {
        // Given
        livroValido.setEditora("");

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Editora do livro não pode ser vazia.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ano de lançamento for null")
    void deveLancarExcecaoQuandoAnoLancamentoForNull() {
        // Given
        livroValido.setAnoLancamento(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Ano de lançamento do livro não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ano de lançamento for vazio")
    void deveLancarExcecaoQuandoAnoLancamentoForVazio() {
        // Given
        livroValido.setAnoLancamento("");

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Ano de lançamento do livro não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando já existir livro com mesmo nome e autor")
    void deveLancarExcecaoQuandoJaExistirLivroComMesmoNomeEAutor() {
        // Given
        when(livroRepository.existsByNomeAndAutor(livroValido.getNome(), livroValido.getAutor())).thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirLivro.executar(livroValido));

        assertEquals("Já existe um livro cadastrado com o nome: " + livroValido.getNome() +
                " e autor: " + livroValido.getAutor(), exception.getMessage());

        verify(livroRepository).existsByNomeAndAutor(livroValido.getNome(), livroValido.getAutor());
        verify(livroRepository, never()).save(any());
    }
}