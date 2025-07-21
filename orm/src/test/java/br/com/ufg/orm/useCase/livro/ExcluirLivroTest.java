package br.com.ufg.orm.useCase.livro;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirLivroTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private ExcluirLivro excluirLivro;

    private Long idLivroValido;

    @BeforeEach
    void setUp() {
        idLivroValido = 1L;
    }

    @Test
    @DisplayName("Deve excluir livro com sucesso quando ID é válido e livro existe")
    void deveExcluirLivroComSucesso() {
        // Given
        when(livroRepository.existsById(idLivroValido)).thenReturn(true);

        // When
        Void resultado = excluirLivro.executar(idLivroValido);

        // Then
        assertNull(resultado);
        verify(livroRepository).existsById(idLivroValido);
        verify(livroRepository).deleteById(idLivroValido);
    }

    @Test
    @DisplayName("Deve verificar se livro existe antes de excluir")
    void deveVerificarSeLivroExisteAntesDeDeletar() {
        // Given
        when(livroRepository.existsById(idLivroValido)).thenReturn(true);

        // When
        excluirLivro.executar(idLivroValido);

        // Then
        verify(livroRepository).existsById(idLivroValido);
        verify(livroRepository).deleteById(idLivroValido);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do livro for null")
    void deveLancarExcecaoQuandoIdLivroForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> excluirLivro.executar(null));

        assertEquals("ID do livro não pode ser null.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro não existir")
    void deveLancarExcecaoQuandoLivroNaoExistir() {
        // Given
        Long idLivroInexistente = 999L;
        when(livroRepository.existsById(idLivroInexistente)).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> excluirLivro.executar(idLivroInexistente));

        assertEquals("Não existe um livro cadastrado com o ID: " + idLivroInexistente,
            exception.getMessage());

        verify(livroRepository).existsById(idLivroInexistente);
        verify(livroRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve chamar validação antes de executar exclusão")
    void deveChamarValidacaoAntesDeExecutarExclusao() {
        // Given
        when(livroRepository.existsById(idLivroValido)).thenReturn(true);

        // When
        excluirLivro.executar(idLivroValido);

        // Then
        // Verifica se a validação foi executada (existsById é chamado na validação)
        verify(livroRepository).existsById(idLivroValido);
        verify(livroRepository).deleteById(idLivroValido);
    }

    @Test
    @DisplayName("Deve aceitar IDs diferentes de 1")
    void deveAceitarIdsDiferentesDe1() {
        // Given
        Long idLivroAlternativo = 42L;
        when(livroRepository.existsById(idLivroAlternativo)).thenReturn(true);

        // When
        Void resultado = excluirLivro.executar(idLivroAlternativo);

        // Then
        assertNull(resultado);
        verify(livroRepository).existsById(idLivroAlternativo);
        verify(livroRepository).deleteById(idLivroAlternativo);
    }

    @Test
    @DisplayName("Deve aceitar IDs grandes")
    void deveAceitarIdsGrandes() {
        // Given
        Long idLivroGrande = 999999999L;
        when(livroRepository.existsById(idLivroGrande)).thenReturn(true);

        // When
        Void resultado = excluirLivro.executar(idLivroGrande);

        // Then
        assertNull(resultado);
        verify(livroRepository).existsById(idLivroGrande);
        verify(livroRepository).deleteById(idLivroGrande);
    }

    @Test
    @DisplayName("Não deve chamar deleteById quando validação falhar por ID null")
    void naoDeveChamarDeleteByIdQuandoValidacaoFalharPorIdNull() {
        // When & Then
        assertThrows(NegocioException.class, () -> excluirLivro.executar(null));

        // Verifica que deleteById nunca foi chamado
        verify(livroRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Não deve chamar deleteById quando livro não existir")
    void naoDeveChamarDeleteByIdQuandoLivroNaoExistir() {
        // Given
        Long idInexistente = 999L;
        when(livroRepository.existsById(idInexistente)).thenReturn(false);

        // When & Then
        assertThrows(NegocioException.class, () -> excluirLivro.executar(idInexistente));

        // Verifica que deleteById nunca foi chamado
        verify(livroRepository, never()).deleteById(any());
    }
}