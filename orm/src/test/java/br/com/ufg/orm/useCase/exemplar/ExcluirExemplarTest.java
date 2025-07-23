package br.com.ufg.orm.useCase.exemplar;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.repository.ExemplarRepository;
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
class ExcluirExemplarTest {

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private ExcluirExemplar excluirExemplar;

    private Long idExemplarValido;

    @BeforeEach
    void setUp() {
        idExemplarValido = 1L;
    }

    @Test
    @DisplayName("Deve excluir exemplar com sucesso quando ID é válido e exemplar existe")
    void deveExcluirExemplarComSucesso() {
        // Given
        when(exemplarRepository.existsById(idExemplarValido)).thenReturn(true);

        // When
        Void resultado = excluirExemplar.executar(idExemplarValido);

        // Then
        assertNull(resultado);
        verify(exemplarRepository).existsById(idExemplarValido);
        verify(exemplarRepository).deleteById(idExemplarValido);
    }

    @Test
    @DisplayName("Deve verificar se exemplar existe antes de excluir")
    void deveVerificarSeExemplarExisteAntesDeDeletar() {
        // Given
        when(exemplarRepository.existsById(idExemplarValido)).thenReturn(true);

        // When
        excluirExemplar.executar(idExemplarValido);

        // Then
        verify(exemplarRepository).existsById(idExemplarValido);
        verify(exemplarRepository).deleteById(idExemplarValido);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do exemplar for null")
    void deveLancarExcecaoQuandoIdExemplarForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> excluirExemplar.executar(null));

        assertEquals("ID do exemplar não pode ser null.", exception.getMessage());
        verifyNoInteractions(exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar não existir")
    void deveLancarExcecaoQuandoExemplarNaoExistir() {
        // Given
        Long idExemplarInexistente = 999L;
        when(exemplarRepository.existsById(idExemplarInexistente)).thenReturn(false);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
            () -> excluirExemplar.executar(idExemplarInexistente));

        assertEquals("Não existe um exemplar cadastrado com o ID: " + idExemplarInexistente,
            exception.getMessage());

        verify(exemplarRepository).existsById(idExemplarInexistente);
        verify(exemplarRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve chamar validação antes de executar exclusão")
    void deveChamarValidacaoAntesDeExecutarExclusao() {
        // Given
        when(exemplarRepository.existsById(idExemplarValido)).thenReturn(true);

        // When
        excluirExemplar.executar(idExemplarValido);

        // Then
        // Verifica se a validação foi executada (existsById é chamado na validação)
        verify(exemplarRepository).existsById(idExemplarValido);
        verify(exemplarRepository).deleteById(idExemplarValido);
    }

    @Test
    @DisplayName("Deve aceitar IDs diferentes de 1")
    void deveAceitarIdsDiferentesDe1() {
        // Given
        Long idExemplarAlternativo = 42L;
        when(exemplarRepository.existsById(idExemplarAlternativo)).thenReturn(true);

        // When
        Void resultado = excluirExemplar.executar(idExemplarAlternativo);

        // Then
        assertNull(resultado);
        verify(exemplarRepository).existsById(idExemplarAlternativo);
        verify(exemplarRepository).deleteById(idExemplarAlternativo);
    }

    @Test
    @DisplayName("Deve aceitar IDs grandes")
    void deveAceitarIdsGrandes() {
        // Given
        Long idExemplarGrande = 999999999L;
        when(exemplarRepository.existsById(idExemplarGrande)).thenReturn(true);

        // When
        Void resultado = excluirExemplar.executar(idExemplarGrande);

        // Then
        assertNull(resultado);
        verify(exemplarRepository).existsById(idExemplarGrande);
        verify(exemplarRepository).deleteById(idExemplarGrande);
    }

    @Test
    @DisplayName("Não deve chamar deleteById quando validação falhar por ID null")
    void naoDeveChamarDeleteByIdQuandoValidacaoFalharPorIdNull() {
        // When & Then
        assertThrows(NegocioException.class, () -> excluirExemplar.executar(null));

        // Verifica que deleteById nunca foi chamado
        verify(exemplarRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Não deve chamar deleteById quando exemplar não existir")
    void naoDeveChamarDeleteByIdQuandoExemplarNaoExistir() {
        // Given
        Long idInexistente = 999L;
        when(exemplarRepository.existsById(idInexistente)).thenReturn(false);

        // When & Then
        assertThrows(NegocioException.class, () -> excluirExemplar.executar(idInexistente));

        // Verifica que deleteById nunca foi chamado
        verify(exemplarRepository, never()).deleteById(any());
    }
}