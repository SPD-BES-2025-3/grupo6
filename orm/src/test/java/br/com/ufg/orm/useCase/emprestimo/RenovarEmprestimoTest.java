package br.com.ufg.orm.useCase.emprestimo;

import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
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
class RenovarEmprestimoTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @InjectMocks
    private RenovarEmprestimo renovarEmprestimo;

    private Usuario usuarioValido;
    private Exemplar exemplarValido;
    private Emprestimo emprestimoAtrasado;
    private Emprestimo emprestimoAtivo;
    private Emprestimo emprestimoDevolvido;

    @BeforeEach
    void setUp() {
        usuarioValido = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .build();

        exemplarValido = Exemplar.builder()
                .id(1L)
                .build();

        emprestimoAtrasado = Emprestimo.builder()
                .id(1L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .status(StatusEmprestimo.ATRASADO)
                .dataEmprestimo(LocalDateTime.now().minusWeeks(3))
                .dataPrevistaDevolucao(LocalDateTime.now().minusWeeks(1))
                .renovacoes(0)
                .build();

        emprestimoAtivo = Emprestimo.builder()
                .id(2L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .status(StatusEmprestimo.ATIVO)
                .dataEmprestimo(LocalDateTime.now().minusWeeks(1))
                .dataPrevistaDevolucao(LocalDateTime.now().plusWeeks(1))
                .renovacoes(0)
                .build();

        emprestimoDevolvido = Emprestimo.builder()
                .id(3L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .status(StatusEmprestimo.DEVOLVIDO)
                .dataEmprestimo(LocalDateTime.now().minusWeeks(2))
                .dataPrevistaDevolucao(LocalDateTime.now().minusWeeks(1))
                .dataDevolucao(LocalDateTime.now().minusDays(3))
                .renovacoes(0)
                .build();
    }

    @Test
    @DisplayName("Deve renovar empréstimo atrasado com sucesso")
    void deveRenovarEmprestimoAtrasadoComSucesso() {
        // Given
        LocalDateTime antesRenovacao = LocalDateTime.now();
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoAtrasado));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoAtrasado);

        // When
        Emprestimo resultado = renovarEmprestimo.executar(emprestimoAtrasado);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusEmprestimo.RENOVADO, resultado.getStatus());
        assertEquals(1, resultado.getRenovacoes());
        assertNotNull(resultado.getDataPrevistaDevolucao());
        assertTrue(resultado.getDataPrevistaDevolucao().isAfter(antesRenovacao.plusDays(6)));
        assertTrue(resultado.getDataPrevistaDevolucao().isBefore(antesRenovacao.plusDays(8)));

        verify(emprestimoRepository).findById(1L);
        verify(emprestimoRepository).save(emprestimoAtrasado);
    }

    @Test
    @DisplayName("Deve incrementar número de renovações")
    void deveIncrementarNumeroRenovacoes() {
        // Given
        emprestimoAtrasado.setRenovacoes(2);
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoAtrasado));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoAtrasado);

        // When
        Emprestimo resultado = renovarEmprestimo.executar(emprestimoAtrasado);

        // Then
        assertEquals(3, resultado.getRenovacoes());

        verify(emprestimoRepository).findById(1L);
        verify(emprestimoRepository).save(emprestimoAtrasado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo é nulo")
    void deveLancarExcecaoQuandoEmprestimoNulo() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> renovarEmprestimo.executar(null));

        assertEquals("O empréstimo não pode ser nulo ou sem ID.", exception.getMessage());
        verify(emprestimoRepository, never()).findById(any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do empréstimo é nulo")
    void deveLancarExcecaoQuandoIdEmprestimoNulo() {
        // Given
        Emprestimo emprestimoSemId = Emprestimo.builder().build();

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> renovarEmprestimo.executar(emprestimoSemId));

        assertEquals("O empréstimo não pode ser nulo ou sem ID.", exception.getMessage());
        verify(emprestimoRepository, never()).findById(any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo não é encontrado")
    void deveLancarExcecaoQuandoEmprestimoNaoEncontrado() {
        // Given
        when(emprestimoRepository.findById(999L)).thenReturn(Optional.empty());

        Emprestimo emprestimoInexistente = Emprestimo.builder().id(999L).build();

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> renovarEmprestimo.executar(emprestimoInexistente));

        assertEquals("Empréstimo não encontrado.", exception.getMessage());
        verify(emprestimoRepository).findById(999L);
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo já foi devolvido")
    void deveLancarExcecaoQuandoEmprestimoJaDevolvido() {
        // Given
        when(emprestimoRepository.findById(3L)).thenReturn(Optional.of(emprestimoDevolvido));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> renovarEmprestimo.executar(emprestimoDevolvido));

        assertEquals("O empréstimo já foi devolvido.", exception.getMessage());
        verify(emprestimoRepository).findById(3L);
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo não está atrasado")
    void deveLancarExcecaoQuandoEmprestimoNaoAtrasado() {
        // Given
        when(emprestimoRepository.findById(2L)).thenReturn(Optional.of(emprestimoAtivo));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> renovarEmprestimo.executar(emprestimoAtivo));

        assertEquals("O empréstimo não está atrasado e não pode ser renovado.", exception.getMessage());
        verify(emprestimoRepository).findById(2L);
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo tem data de devolução preenchida")
    void deveLancarExcecaoQuandoEmprestimoTemDataDevolucao() {
        // Given
        Emprestimo emprestimoComDataDevolucao = Emprestimo.builder()
                .id(4L)
                .status(StatusEmprestimo.ATRASADO)
                .dataDevolucao(LocalDateTime.now().minusDays(1))
                .build();

        when(emprestimoRepository.findById(4L)).thenReturn(Optional.of(emprestimoComDataDevolucao));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> renovarEmprestimo.executar(emprestimoComDataDevolucao));

        assertEquals("O empréstimo já foi devolvido.", exception.getMessage());
        verify(emprestimoRepository).findById(4L);
        verify(emprestimoRepository, never()).save(any());
    }
}