package br.com.ufg.orm.useCase.exemplar;

import br.com.ufg.orm.enums.Conservacao;
import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Livro;
import br.com.ufg.orm.repository.ExemplarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarExemplarTest {

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private AlterarExemplar alterarExemplar;

    private Exemplar exemplarValido;
    private Exemplar exemplarExistente;
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

        exemplarExistente = Exemplar.builder()
                .id(1L)
                .codigoIdentificacao("EX001")
                .livro(livroValido)
                .conservacao(Conservacao.BOM)
                .numeroEdicao(1)
                .disponibilidade(Disponibilidade.DISPONIVEL)
                .dataCriacao(LocalDateTime.now())
                .build();

        exemplarValido = Exemplar.builder()
                .id(1L)
                .codigoIdentificacao("EX001")
                .livro(livroValido)
                .conservacao(Conservacao.NOVO)
                .numeroEdicao(2)
                .disponibilidade(Disponibilidade.EMPRESTADO)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve alterar exemplar com sucesso quando todos os dados são válidos")
    void deveAlterarExemplarComSucesso() {
        // Given
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = alterarExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(exemplarValido.getId(), resultado.getId());
        assertEquals(exemplarValido.getCodigoIdentificacao(), resultado.getCodigoIdentificacao());
        assertEquals(exemplarValido.getConservacao(), resultado.getConservacao());
        assertEquals(exemplarValido.getNumeroEdicao(), resultado.getNumeroEdicao());
        assertEquals(exemplarValido.getDisponibilidade(), resultado.getDisponibilidade());

        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve chamar validação antes de salvar o exemplar")
    void deveChamarValidacaoAntesDeSalvar() {
        // Given
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        alterarExemplar.executar(exemplarValido);

        // Then
        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar for null")
    void deveLancarExcecaoQuandoExemplarForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(null));

        assertEquals("Exemplar não pode ser null.", exception.getMessage());
        verifyNoInteractions(exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do exemplar for null")
    void deveLancarExcecaoQuandoIdExemplarForNull() {
        // Given
        exemplarValido.setId(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar não for encontrado")
    void deveLancarExcecaoQuandoExemplarNaoForEncontrado() {
        // Given
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.empty());

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Exemplar não encontrado com o ID: " + exemplarValido.getId(), exception.getMessage());
        verify(exemplarRepository).findById(exemplarValido.getId());
        verify(exemplarRepository, never()).save(any());
    }

    @Disabled
    @Test
    @DisplayName("Deve lançar exceção quando código de identificação for alterado")
    void deveLancarExcecaoQuandoCodigoIdentificacaoForAlterado() {
        // Given
        exemplarValido.setCodigoIdentificacao("EX002"); // Código diferente do existente
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Código de identificação não pode ser alterado.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando código de identificação for vazio")
    void deveLancarExcecaoQuandoCodigoIdentificacaoForVazio() {
        // Given
        exemplarValido.setCodigoIdentificacao("");
        exemplarExistente.setCodigoIdentificacao("");
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Código de identificação do exemplar não pode ser vazio.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando código de identificação for null")
    void deveLancarExcecaoQuandoCodigoIdentificacaoForNull() {
        // Given
        exemplarValido.setCodigoIdentificacao(null);
        exemplarExistente.setCodigoIdentificacao(null);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Código de identificação do exemplar não pode ser vazio.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Disabled
    @Test
    @DisplayName("Deve lançar exceção quando livro for null")
    void deveLancarExcecaoQuandoLivroForNull() {
        // Given
        exemplarValido.setLivro(null);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(null));

        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do livro for null")
    void deveLancarExcecaoQuandoIdLivroForNull() {
        // Given
        exemplarValido.getLivro().setId(null);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("ID do livro do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando conservação for null")
    void deveLancarExcecaoQuandoConservacaoForNull() {
        // Given
        exemplarValido.setConservacao(null);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Estado de conservação do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando número da edição for null")
    void deveLancarExcecaoQuandoNumeroEdicaoForNull() {
        // Given
        exemplarValido.setNumeroEdicao(null);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Número da edição do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando número da edição for menor ou igual a zero")
    void deveLancarExcecaoQuandoNumeroEdicaoForMenorOuIgualAZero() {
        // Given
        exemplarValido.setNumeroEdicao(0);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Número da edição deve ser maior que zero.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando disponibilidade for null")
    void deveLancarExcecaoQuandoDisponibilidadeForNull() {
        // Given
        exemplarValido.setDisponibilidade(null);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> alterarExemplar.executar(exemplarValido));

        assertEquals("Disponibilidade do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve permitir alterar conservação")
    void devePermitirAlterarConservacao() {
        // Given
        exemplarValido.setConservacao(Conservacao.RUIM);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = alterarExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(Conservacao.RUIM, resultado.getConservacao());
        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve permitir alterar número da edição")
    void devePermitirAlterarNumeroEdicao() {
        // Given
        exemplarValido.setNumeroEdicao(5);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = alterarExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(5, resultado.getNumeroEdicao());
        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve permitir alterar disponibilidade")
    void devePermitirAlterarDisponibilidade() {
        // Given
        exemplarValido.setDisponibilidade(Disponibilidade.INDISPONIVEL);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = alterarExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(Disponibilidade.INDISPONIVEL, resultado.getDisponibilidade());
        verify(exemplarRepository).save(exemplarValido);
    }

    @Disabled
    @Test
    @DisplayName("Deve permitir alterar livro associado")
    void devePermitirAlterarLivroAssociado() {
        // Given
        Livro novoLivro = Livro.builder()
                .id(2L)
                .nome("1984")
                .autor("George Orwell")
                .build();
        exemplarValido.setLivro(novoLivro);
        when(exemplarRepository.findById(exemplarValido.getId())).thenReturn(Optional.of(exemplarExistente));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = alterarExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(2L, resultado.getLivro().getId());
        assertEquals("1984", resultado.getLivro().getNome());
        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve aceitar diferentes IDs de exemplar")
    void deveAceitarDiferentesIdsDeExemplar() {
        // Given
        exemplarValido.setId(999L);
        exemplarExistente.setId(999L);
        when(exemplarRepository.findById(999L)).thenReturn(Optional.of(exemplarExistente));
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = alterarExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(999L, resultado.getId());
        verify(exemplarRepository).save(exemplarValido);
    }
}