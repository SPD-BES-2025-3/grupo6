package br.com.ufg.orm.useCase.exemplar;

import br.com.ufg.orm.enums.Conservacao;
import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Livro;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.util.CodigoIdentificacaoGenerator;
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
class IncluirExemplarTest {

    @Mock
    private ExemplarRepository exemplarRepository;

    @Mock
    private CodigoIdentificacaoGenerator codigoGenerator;

    @InjectMocks
    private IncluirExemplar incluirExemplar;

    private Exemplar exemplarValido;
    private Livro livroMock;

    @BeforeEach
    void setUp() {
        livroMock = Livro.builder()
                .id(1L)
                .nome("Clean Code")
                .autor("Robert C. Martin")
                .editora("Prentice Hall")
                .anoLancamento("2008")
                .build();

        exemplarValido = Exemplar.builder()
                .livro(livroMock)
                .codigoIdentificacao("EX-202501211230-1234")
                .conservacao(Conservacao.BOM)
                .numeroEdicao(1)
                .disponibilidade(Disponibilidade.DISPONIVEL)
                .build();
    }

    @Test
    @DisplayName("Deve incluir exemplar com sucesso quando todos os dados são válidos")
    void deveIncluirExemplarComSucesso() {
        // Given
        when(exemplarRepository.existsByCodigoIdentificacao(anyString())).thenReturn(false);
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = incluirExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(exemplarValido.getLivro(), resultado.getLivro());
        assertEquals(exemplarValido.getCodigoIdentificacao(), resultado.getCodigoIdentificacao());
        assertEquals(exemplarValido.getConservacao(), resultado.getConservacao());
        assertEquals(exemplarValido.getNumeroEdicao(), resultado.getNumeroEdicao());
        assertEquals(exemplarValido.getDisponibilidade(), resultado.getDisponibilidade());

        verify(exemplarRepository).existsByCodigoIdentificacao(exemplarValido.getCodigoIdentificacao());
        verify(exemplarRepository).save(exemplarValido);
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve gerar código automaticamente quando código não for fornecido")
    void deveGerarCodigoAutomaticamenteQuandoCodigoNaoForFornecido() {
        // Given
        String codigoGerado = "EX-202501211235-5678";
        exemplarValido.setCodigoIdentificacao(null);

        when(codigoGenerator.gerarCodigo()).thenReturn(codigoGerado);
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        Exemplar resultado = incluirExemplar.executar(exemplarValido);

        // Then
        assertNotNull(resultado);
        assertEquals(codigoGerado, exemplarValido.getCodigoIdentificacao());

        verify(codigoGenerator).gerarCodigo();
        verify(exemplarRepository).save(exemplarValido);
        verify(exemplarRepository).save(any(Exemplar.class));
    }

    @Test
    @DisplayName("Deve gerar código automaticamente quando código for vazio")
    void deveGerarCodigoAutomaticamenteQuandoCodigoForVazio() {
        // Given
        String codigoGerado = "EX-202501211240-9999";
        exemplarValido.setCodigoIdentificacao("");

        when(codigoGenerator.gerarCodigo()).thenReturn(codigoGerado);
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When
        incluirExemplar.executar(exemplarValido);

        // Then
        assertEquals(codigoGerado, exemplarValido.getCodigoIdentificacao());

        verify(codigoGenerator).gerarCodigo();
        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar for null")
    void deveLancarExcecaoQuandoExemplarForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(null));

        assertEquals("Exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro for null")
    void deveLancarExcecaoQuandoLivroForNull() {
        // Given
        exemplarValido.setLivro(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(exemplarValido));

        assertEquals("Livro do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando código já existir")
    void deveLancarExcecaoQuandoCodigoJaExistir() {
        // Given
        when(exemplarRepository.existsByCodigoIdentificacao(exemplarValido.getCodigoIdentificacao())).thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(exemplarValido));

        assertEquals("Já existe um exemplar cadastrado com o código de identificação: " +
                    exemplarValido.getCodigoIdentificacao(), exception.getMessage());

        verify(exemplarRepository).existsByCodigoIdentificacao(exemplarValido.getCodigoIdentificacao());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando conservação for null")
    void deveLancarExcecaoQuandoConservacaoForNull() {
        // Given
        exemplarValido.setConservacao(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(exemplarValido));

        assertEquals("Estado de conservação do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando número da edição for null")
    void deveLancarExcecaoQuandoNumeroEdicaoForNull() {
        // Given
        exemplarValido.setNumeroEdicao(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(exemplarValido));

        assertEquals("Número da edição do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando número da edição for menor ou igual a zero")
    void deveLancarExcecaoQuandoNumeroEdicaoForMenorOuIgualAZero() {
        // Given
        exemplarValido.setNumeroEdicao(0);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(exemplarValido));

        assertEquals("Número da edição deve ser maior que zero.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando número da edição for negativo")
    void deveLancarExcecaoQuandoNumeroEdicaoForNegativo() {
        // Given
        exemplarValido.setNumeroEdicao(-1);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(exemplarValido));

        assertEquals("Número da edição deve ser maior que zero.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve lançar exceção quando disponibilidade for null")
    void deveLancarExcecaoQuandoDisponibilidadeForNull() {
        // Given
        exemplarValido.setDisponibilidade(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> incluirExemplar.executar(exemplarValido));

        assertEquals("Disponibilidade do exemplar não pode ser null.", exception.getMessage());
        verify(exemplarRepository, never()).save(any());
        verifyNoInteractions(codigoGenerator);
    }

    @Test
    @DisplayName("Deve aceitar exemplar com diferentes estados de conservação")
    void deveAceitarExemplarComDiferentesEstadosDeConservacao() {
        // Given
        exemplarValido.setConservacao(Conservacao.NOVO);
        when(exemplarRepository.existsByCodigoIdentificacao(anyString())).thenReturn(false);
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When & Then
        assertDoesNotThrow(() -> incluirExemplar.executar(exemplarValido));
        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve aceitar exemplar com diferentes disponibilidades")
    void deveAceitarExemplarComDiferentesDisponibilidades() {
        // Given
        exemplarValido.setDisponibilidade(Disponibilidade.EMPRESTADO);
        when(exemplarRepository.existsByCodigoIdentificacao(anyString())).thenReturn(false);
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When & Then
        assertDoesNotThrow(() -> incluirExemplar.executar(exemplarValido));
        verify(exemplarRepository).save(exemplarValido);
    }

    @Test
    @DisplayName("Deve aceitar número da edição válido")
    void deveAceitarNumeroEdicaoValido() {
        // Given
        exemplarValido.setNumeroEdicao(5);
        when(exemplarRepository.existsByCodigoIdentificacao(anyString())).thenReturn(false);
        when(exemplarRepository.save(any(Exemplar.class))).thenReturn(exemplarValido);

        // When & Then
        assertDoesNotThrow(() -> incluirExemplar.executar(exemplarValido));
        verify(exemplarRepository).save(exemplarValido);
    }
}