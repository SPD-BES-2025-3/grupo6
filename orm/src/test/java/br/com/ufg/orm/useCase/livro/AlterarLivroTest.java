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
class AlterarLivroTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private AlterarLivro alterarLivro;

    private Livro livroValido;

    @BeforeEach
    void setUp() {
        livroValido = Livro.builder()
                .id(1L)
                .nome("O Senhor dos Anéis")
                .autor("J.R.R. Tolkien")
                .editora("HarperCollins")
                .anoLancamento("1954")
                .build();
    }

    @Test
    @DisplayName("Deve alterar livro com sucesso quando todos os dados são válidos")
    void deveAlterarLivroComSucesso() {
        // Given
        when(livroRepository.existsAnotherByNomeAndAutor(anyLong(), anyString(), anyString())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroValido);

        // When
        Livro resultado = alterarLivro.executar(livroValido);

        // Then
        assertNotNull(resultado);
        assertEquals(livroValido.getId(), resultado.getId());
        assertEquals(livroValido.getNome(), resultado.getNome());
        assertEquals(livroValido.getAutor(), resultado.getAutor());
        assertEquals(livroValido.getEditora(), resultado.getEditora());
        assertEquals(livroValido.getAnoLancamento(), resultado.getAnoLancamento());

        verify(livroRepository).existsAnotherByNomeAndAutor(livroValido.getId(), livroValido.getNome(), livroValido.getAutor());
        verify(livroRepository).save(livroValido);
    }

    @Test
    @DisplayName("Deve chamar validação antes de salvar o livro")
    void deveChamarValidacaoAntesDeSalvar() {
        // Given
        when(livroRepository.existsAnotherByNomeAndAutor(anyLong(), anyString(), anyString())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroValido);

        // When
        alterarLivro.executar(livroValido);

        // Then
        verify(livroRepository).existsAnotherByNomeAndAutor(livroValido.getId(), livroValido.getNome(), livroValido.getAutor());
        verify(livroRepository).save(livroValido);
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro for null")
    void deveLancarExcecaoQuandoLivroForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarLivro.executar(null));

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
            () -> alterarLivro.executar(livroValido));

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
            () -> alterarLivro.executar(livroValido));

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
            () -> alterarLivro.executar(livroValido));

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
            () -> alterarLivro.executar(livroValido));

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
            () -> alterarLivro.executar(livroValido));

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
            () -> alterarLivro.executar(livroValido));

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
            () -> alterarLivro.executar(livroValido));

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
            () -> alterarLivro.executar(livroValido));

        assertEquals("Ano de lançamento do livro não pode ser vazio.", exception.getMessage());
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando já existir outro livro com mesmo nome e autor")
    void deveLancarExcecaoQuandoJaExistirOutroLivroComMesmoNomeEAutor() {
        // Given
        when(livroRepository.existsAnotherByNomeAndAutor(livroValido.getId(), livroValido.getNome(), livroValido.getAutor())).thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarLivro.executar(livroValido));

        assertEquals("Já existe um livro cadastrado com o nome: " + livroValido.getNome() + " e autor: " + livroValido.getAutor(),
            exception.getMessage());

        verify(livroRepository).existsAnotherByNomeAndAutor(livroValido.getId(), livroValido.getNome(), livroValido.getAutor());
        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve permitir alterar livro quando não existe outro com mesmo nome e autor")
    void devePermitirAlterarLivroQuandoNaoExisteOutroComMesmoNomeEAutor() {
        // Given
        when(livroRepository.existsAnotherByNomeAndAutor(livroValido.getId(), livroValido.getNome(), livroValido.getAutor())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroValido);

        // When
        Livro resultado = alterarLivro.executar(livroValido);

        // Then
        assertNotNull(resultado);
        verify(livroRepository).existsAnotherByNomeAndAutor(livroValido.getId(), livroValido.getNome(), livroValido.getAutor());
        verify(livroRepository).save(livroValido);
    }

    @Test
    @DisplayName("Deve validar dados antes de verificar duplicidade")
    void deveValidarDadosAntesDeVerificarDuplicidade() {
        // Given
        livroValido.setNome(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarLivro.executar(livroValido));

        assertEquals("Nome do livro não pode ser vazio.", exception.getMessage());
        // Não deve chamar o repository se a validação básica falhar
        verifyNoInteractions(livroRepository);
    }

    @Test
    @DisplayName("Deve aceitar diferentes IDs de livro")
    void deveAceitarDiferentesIdsDeLivro() {
        // Given
        livroValido.setId(999L);
        when(livroRepository.existsAnotherByNomeAndAutor(999L, livroValido.getNome(), livroValido.getAutor())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroValido);

        // When
        Livro resultado = alterarLivro.executar(livroValido);

        // Then
        assertNotNull(resultado);
        assertEquals(999L, resultado.getId());
        verify(livroRepository).existsAnotherByNomeAndAutor(999L, livroValido.getNome(), livroValido.getAutor());
        verify(livroRepository).save(livroValido);
    }

    @Test
    @DisplayName("Deve aceitar anos de lançamento diferentes")
    void deveAceitarAnosDeLancamentoDiferentes() {
        // Given
        livroValido.setAnoLancamento("2023");
        when(livroRepository.existsAnotherByNomeAndAutor(anyLong(), anyString(), anyString())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroValido);

        // When
        Livro resultado = alterarLivro.executar(livroValido);

        // Then
        assertNotNull(resultado);
        assertEquals("2023", resultado.getAnoLancamento());
        verify(livroRepository).save(livroValido);
    }

    @Test
    @DisplayName("Deve aceitar diferentes editoras")
    void deveAceitarDiferentesEditoras() {
        // Given
        livroValido.setEditora("Nova Editora");
        when(livroRepository.existsAnotherByNomeAndAutor(anyLong(), anyString(), anyString())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroValido);

        // When
        Livro resultado = alterarLivro.executar(livroValido);

        // Then
        assertNotNull(resultado);
        assertEquals("Nova Editora", resultado.getEditora());
        verify(livroRepository).save(livroValido);
    }
}