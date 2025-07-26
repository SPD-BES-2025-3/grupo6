package br.com.ufg.orm.useCase.emprestimo;

import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.EmprestimoRepository;
import br.com.ufg.orm.repository.ExemplarRepository;
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
class DevolverEmprestimoTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private DevolverEmprestimo devolverEmprestimo;

    private Usuario usuarioValido;
    private Exemplar exemplarValido;
    private Emprestimo emprestimoAtivo;
    private Emprestimo emprestimoAtrasado;
    private Emprestimo emprestimoRenovado;
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
                .disponibilidade(Disponibilidade.EMPRESTADO)
                .build();

        emprestimoAtivo = Emprestimo.builder()
                .id(1L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .status(StatusEmprestimo.ATIVO)
                .dataEmprestimo(LocalDateTime.now().minusWeeks(1))
                .dataPrevistaDevolucao(LocalDateTime.now().plusWeeks(1))
                .renovacoes(0)
                .build();

        emprestimoAtrasado = Emprestimo.builder()
                .id(2L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .status(StatusEmprestimo.ATRASADO)
                .dataEmprestimo(LocalDateTime.now().minusWeeks(3))
                .dataPrevistaDevolucao(LocalDateTime.now().minusWeeks(1))
                .renovacoes(0)
                .build();

        emprestimoRenovado = Emprestimo.builder()
                .id(3L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .status(StatusEmprestimo.RENOVADO)
                .dataEmprestimo(LocalDateTime.now().minusWeeks(2))
                .dataPrevistaDevolucao(LocalDateTime.now().plusDays(3))
                .renovacoes(1)
                .build();

        emprestimoDevolvido = Emprestimo.builder()
                .id(4L)
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
    @DisplayName("Deve devolver empréstimo ativo com sucesso")
    void deveDevolverEmprestimoAtivoComSucesso() {
        // Given
        LocalDateTime antesDevolucao = LocalDateTime.now();
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoAtivo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoAtivo);

        // When
        Emprestimo resultado = devolverEmprestimo.executar(emprestimoAtivo);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusEmprestimo.DEVOLVIDO, resultado.getStatus());
        assertNotNull(resultado.getDataDevolucao());
        assertTrue(resultado.getDataDevolucao().isAfter(antesDevolucao.minusSeconds(1)));
        assertTrue(resultado.getDataDevolucao().isBefore(LocalDateTime.now().plusSeconds(1)));

        verify(emprestimoRepository).findById(1L);
        verify(exemplarRepository).updateDisponibilidade(exemplarValido.getId(), Disponibilidade.DISPONIVEL);
        verify(emprestimoRepository).save(emprestimoAtivo);
    }

    @Test
    @DisplayName("Deve devolver empréstimo atrasado com sucesso")
    void deveDevolverEmprestimoAtrasadoComSucesso() {
        // Given
        LocalDateTime antesDevolucao = LocalDateTime.now();
        when(emprestimoRepository.findById(2L)).thenReturn(Optional.of(emprestimoAtrasado));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoAtrasado);

        // When
        Emprestimo resultado = devolverEmprestimo.executar(emprestimoAtrasado);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusEmprestimo.DEVOLVIDO, resultado.getStatus());
        assertNotNull(resultado.getDataDevolucao());
        assertTrue(resultado.getDataDevolucao().isAfter(antesDevolucao.minusSeconds(1)));

        verify(emprestimoRepository).findById(2L);
        verify(exemplarRepository).updateDisponibilidade(exemplarValido.getId(), Disponibilidade.DISPONIVEL);
        verify(emprestimoRepository).save(emprestimoAtrasado);
    }

    @Test
    @DisplayName("Deve devolver empréstimo renovado com sucesso")
    void deveDevolverEmprestimoRenovadoComSucesso() {
        // Given
        when(emprestimoRepository.findById(3L)).thenReturn(Optional.of(emprestimoRenovado));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoRenovado);

        // When
        Emprestimo resultado = devolverEmprestimo.executar(emprestimoRenovado);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusEmprestimo.DEVOLVIDO, resultado.getStatus());
        assertNotNull(resultado.getDataDevolucao());

        verify(emprestimoRepository).findById(3L);
        verify(exemplarRepository).updateDisponibilidade(exemplarValido.getId(), Disponibilidade.DISPONIVEL);
        verify(emprestimoRepository).save(emprestimoRenovado);
    }

    @Test
    @DisplayName("Deve atualizar disponibilidade do exemplar para disponível")
    void deveAtualizarDisponibilidadeExemplar() {
        // Given
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoAtivo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoAtivo);

        // When
        devolverEmprestimo.executar(emprestimoAtivo);

        // Then
        verify(exemplarRepository).updateDisponibilidade(exemplarValido.getId(), Disponibilidade.DISPONIVEL);
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo é nulo")
    void deveLancarExcecaoQuandoEmprestimoNulo() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> devolverEmprestimo.executar(null));

        assertEquals("O empréstimo não pode ser nulo ou sem ID.", exception.getMessage());
        verify(emprestimoRepository, never()).findById(any());
        verify(exemplarRepository, never()).updateDisponibilidade(any(), any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do empréstimo é nulo")
    void deveLancarExcecaoQuandoIdEmprestimoNulo() {
        // Given
        Emprestimo emprestimoSemId = Emprestimo.builder().build();

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> devolverEmprestimo.executar(emprestimoSemId));

        assertEquals("O empréstimo não pode ser nulo ou sem ID.", exception.getMessage());
        verify(emprestimoRepository, never()).findById(any());
        verify(exemplarRepository, never()).updateDisponibilidade(any(), any());
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
                () -> devolverEmprestimo.executar(emprestimoInexistente));

        assertEquals("Empréstimo não encontrado.", exception.getMessage());
        verify(emprestimoRepository).findById(999L);
        verify(exemplarRepository, never()).updateDisponibilidade(any(), any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo já foi devolvido")
    void deveLancarExcecaoQuandoEmprestimoJaDevolvido() {
        // Given
        when(emprestimoRepository.findById(4L)).thenReturn(Optional.of(emprestimoDevolvido));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> devolverEmprestimo.executar(emprestimoDevolvido));

        assertEquals("O empréstimo já foi devolvido.", exception.getMessage());
        verify(emprestimoRepository).findById(4L);
        verify(exemplarRepository, never()).updateDisponibilidade(any(), any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo tem data de devolução preenchida")
    void deveLancarExcecaoQuandoEmprestimoTemDataDevolucao() {
        // Given
        Emprestimo emprestimoComDataDevolucao = Emprestimo.builder()
                .id(5L)
                .status(StatusEmprestimo.ATIVO)
                .dataDevolucao(LocalDateTime.now().minusDays(1))
                .build();

        when(emprestimoRepository.findById(5L)).thenReturn(Optional.of(emprestimoComDataDevolucao));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> devolverEmprestimo.executar(emprestimoComDataDevolucao));

        assertEquals("O empréstimo já foi devolvido.", exception.getMessage());
        verify(emprestimoRepository).findById(5L);
        verify(exemplarRepository, never()).updateDisponibilidade(any(), any());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve preservar dados originais do empréstimo durante devolução")
    void devePreservarDadosOriginaisEmprestimo() {
        // Given
        LocalDateTime dataEmprestimoOriginal = emprestimoAtivo.getDataEmprestimo();
        LocalDateTime dataPrevistaOriginal = emprestimoAtivo.getDataPrevistaDevolucao();
        int renovacoesOriginais = emprestimoAtivo.getRenovacoes();

        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimoAtivo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoAtivo);

        // When
        Emprestimo resultado = devolverEmprestimo.executar(emprestimoAtivo);

        // Then
        assertEquals(dataEmprestimoOriginal, resultado.getDataEmprestimo());
        assertEquals(dataPrevistaOriginal, resultado.getDataPrevistaDevolucao());
        assertEquals(renovacoesOriginais, resultado.getRenovacoes());
        assertEquals(usuarioValido, resultado.getUsuario());
        assertEquals(exemplarValido, resultado.getExemplar());
    }
}