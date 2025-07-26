package br.com.ufg.orm.useCase.emprestimo;

import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.enums.StatusReserva;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.EmprestimoRepository;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.repository.ReservaRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealizarEmprestimoTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private ExemplarRepository exemplarRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private RealizarEmprestimo realizarEmprestimo;

    private Usuario usuarioValido;
    private Exemplar exemplarValido;
    private Emprestimo emprestimoValido;
    private Reserva reservaValida;

    @BeforeEach
    void setUp() {
        usuarioValido = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .build();

        exemplarValido = Exemplar.builder()
                .id(1L)
                .disponibilidade(Disponibilidade.DISPONIVEL)
                .build();

        emprestimoValido = Emprestimo.builder()
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .build();

        reservaValida = Reserva.builder()
                .id(1L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .statusReserva(StatusReserva.ATIVA)
                .build();
    }

    @Test
    @DisplayName("Deve realizar empréstimo com sucesso quando todos os dados são válidos")
    void deveRealizarEmprestimoComSucesso() {
        // Given
        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(exemplarRepository.findById(anyLong())).thenReturn(Optional.of(exemplarValido));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoValido);

        // When
        Emprestimo resultado = realizarEmprestimo.executar(emprestimoValido);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusEmprestimo.ATIVO, emprestimoValido.getStatus());
        assertNotNull(emprestimoValido.getDataEmprestimo());
        assertNotNull(emprestimoValido.getDataPrevistaDevolucao());
        assertEquals(0, emprestimoValido.getRenovacoes());

        verify(emprestimoRepository).existsByUsuarioIdAndStatus(usuarioValido.getId(), StatusEmprestimo.ATIVO);
        verify(exemplarRepository).findById(exemplarValido.getId());
        verify(exemplarRepository).updateDisponibilidade(exemplarValido.getId(), Disponibilidade.EMPRESTADO);
        verify(emprestimoRepository).save(emprestimoValido);
    }

    @Test
    @DisplayName("Deve realizar empréstimo por reserva com sucesso")
    void deveRealizarEmprestimoPorReservaComSucesso() {
        // Given
        emprestimoValido.setReserva(reservaValida);

        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaValida));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoValido);

        // When
        Emprestimo resultado = realizarEmprestimo.executar(emprestimoValido);

        // Then
        assertNotNull(resultado);
        assertEquals(StatusEmprestimo.ATIVO, emprestimoValido.getStatus());
        assertEquals(exemplarValido, emprestimoValido.getExemplar());
        assertNotNull(emprestimoValido.getDataEmprestimo());
        assertNotNull(emprestimoValido.getDataPrevistaDevolucao());
        assertEquals(0, emprestimoValido.getRenovacoes());

        verify(reservaRepository).findById(reservaValida.getId());
        verify(reservaRepository).alterarStatus(reservaValida.getId(), StatusReserva.RETIRADA);
        verify(exemplarRepository).updateDisponibilidade(exemplarValido.getId(), Disponibilidade.EMPRESTADO);
        verify(emprestimoRepository).save(emprestimoValido);
    }

    @Test
    @DisplayName("Deve definir data de empréstimo e previsão de devolução automaticamente")
    void deveDefinirDatasAutomaticamente() {
        // Given
        LocalDateTime antes = LocalDateTime.now();

        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(exemplarRepository.findById(anyLong())).thenReturn(Optional.of(exemplarValido));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoValido);

        // When
        realizarEmprestimo.executar(emprestimoValido);

        // Then
        LocalDateTime depois = LocalDateTime.now();

        assertNotNull(emprestimoValido.getDataEmprestimo());
        assertNotNull(emprestimoValido.getDataPrevistaDevolucao());

        assertTrue(emprestimoValido.getDataEmprestimo().isAfter(antes.minusSeconds(1)));
        assertTrue(emprestimoValido.getDataEmprestimo().isBefore(depois.plusSeconds(1)));

        // Verificar se a data prevista é uma semana após o empréstimo
        assertTrue(emprestimoValido.getDataPrevistaDevolucao()
                .isAfter(emprestimoValido.getDataEmprestimo().plusDays(6)));
        assertTrue(emprestimoValido.getDataPrevistaDevolucao()
                .isBefore(emprestimoValido.getDataEmprestimo().plusDays(8)));
    }

    @Test
    @DisplayName("Deve lançar exceção quando empréstimo for null")
    void deveLancarExcecaoQuandoEmprestimoForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(null));

        assertEquals("O empréstimo não pode ser nulo.", exception.getMessage());
        verifyNoInteractions(emprestimoRepository);
        verifyNoInteractions(exemplarRepository);
        verifyNoInteractions(reservaRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário for null")
    void deveLancarExcecaoQuandoUsuarioForNull() {
        // Given
        emprestimoValido.setUsuario(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("O usuário do empréstimo não pode ser nulo.", exception.getMessage());
        verifyNoInteractions(emprestimoRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do usuário for null")
    void deveLancarExcecaoQuandoIdUsuarioForNull() {
        // Given
        usuarioValido.setId(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("O usuário do empréstimo não pode ser nulo.", exception.getMessage());
        verifyNoInteractions(emprestimoRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar for null")
    void deveLancarExcecaoQuandoExemplarForNull() {
        // Given
        emprestimoValido.setExemplar(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("O exemplar do empréstimo não pode ser nulo.", exception.getMessage());
        verify(emprestimoRepository, never()).save(any());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do exemplar for null")
    void deveLancarExcecaoQuandoIdExemplarForNull() {
        // Given
        exemplarValido.setId(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("O exemplar do empréstimo não pode ser nulo.", exception.getMessage());
        verify(emprestimoRepository, never()).save(any());
        verify(exemplarRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário já possui empréstimos ativos")
    void deveLancarExcecaoQuandoUsuarioJaPossuiEmprestimosAtivos() {
        // Given
        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("O usuário já possui empréstimos ativos.", exception.getMessage());

        verify(emprestimoRepository).existsByUsuarioIdAndStatus(usuarioValido.getId(), StatusEmprestimo.ATIVO);
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar não estiver disponível")
    void deveLancarExcecaoQuandoExemplarNaoEstiverDisponivel() {
        // Given
        Exemplar exemplarIndisponivel = Exemplar.builder()
                .id(1L)
                .disponibilidade(Disponibilidade.EMPRESTADO)
                .build();

        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(exemplarRepository.findById(anyLong())).thenReturn(Optional.of(exemplarIndisponivel));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("O exemplar não está disponível para empréstimo.", exception.getMessage());

        verify(exemplarRepository).findById(exemplarValido.getId());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar não for encontrado")
    void deveLancarExcecaoQuandoExemplarNaoForEncontrado() {
        // Given
        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(exemplarRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("O exemplar não está disponível para empréstimo.", exception.getMessage());

        verify(exemplarRepository).findById(exemplarValido.getId());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando reserva não for encontrada")
    void deveLancarExcecaoQuandoReservaNaoForEncontrada() {
        // Given
        emprestimoValido.setReserva(reservaValida);

        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("Reserva não encontrada.", exception.getMessage());

        verify(reservaRepository).findById(reservaValida.getId());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando reserva não pertencer ao usuário")
    void deveLancarExcecaoQuandoReservaNaoPertencerAoUsuario() {
        // Given
        Usuario outroUsuario = Usuario.builder()
                .id(2L)
                .nome("Maria Silva")
                .build();

        Reserva reservaDeOutroUsuario = Reserva.builder()
                .id(1L)
                .usuario(outroUsuario)
                .exemplar(exemplarValido)
                .statusReserva(StatusReserva.ATIVA)
                .build();

        emprestimoValido.setReserva(reservaValida);

        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaDeOutroUsuario));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("A reserva não é válida para o empréstimo pois não pertence ao usuário ou não está ativa.",
                exception.getMessage());

        verify(reservaRepository).findById(reservaValida.getId());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando reserva não estiver ativa")
    void deveLancarExcecaoQuandoReservaNaoEstiverAtiva() {
        // Given
        Reserva reservaInativa = Reserva.builder()
                .id(1L)
                .usuario(usuarioValido)
                .exemplar(exemplarValido)
                .statusReserva(StatusReserva.CANCELADA)
                .build();

        emprestimoValido.setReserva(reservaValida);

        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaInativa));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> realizarEmprestimo.executar(emprestimoValido));

        assertEquals("A reserva não é válida para o empréstimo pois não pertence ao usuário ou não está ativa.",
                exception.getMessage());

        verify(reservaRepository).findById(reservaValida.getId());
        verify(emprestimoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve definir exemplar da reserva no empréstimo quando for empréstimo por reserva")
    void deveDefinirExemplarDaReservaNoEmprestimo() {
        // Given
        Exemplar exemplarDaReserva = Exemplar.builder()
                .id(2L)
                .disponibilidade(Disponibilidade.RESERVADO)
                .build();

        reservaValida.setExemplar(exemplarDaReserva);
        emprestimoValido.setReserva(reservaValida);

        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reservaValida));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoValido);

        // When
        realizarEmprestimo.executar(emprestimoValido);

        // Then
        assertEquals(exemplarDaReserva, emprestimoValido.getExemplar());
        verify(reservaRepository).alterarStatus(reservaValida.getId(), StatusReserva.RETIRADA);
        verify(exemplarRepository).updateDisponibilidade(exemplarDaReserva.getId(), Disponibilidade.EMPRESTADO);
    }

    @Test
    @DisplayName("Deve inicializar campos obrigatórios do empréstimo")
    void deveInicializarCamposObrigatoriosDoEmprestimo() {
        // Given
        when(emprestimoRepository.existsByUsuarioIdAndStatus(anyLong(), any(StatusEmprestimo.class)))
                .thenReturn(false);
        when(exemplarRepository.findById(anyLong())).thenReturn(Optional.of(exemplarValido));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoValido);

        // When
        realizarEmprestimo.executar(emprestimoValido);

        // Then
        assertEquals(StatusEmprestimo.ATIVO, emprestimoValido.getStatus());
        assertNotNull(emprestimoValido.getDataEmprestimo());
        assertNotNull(emprestimoValido.getDataPrevistaDevolucao());
        assertEquals(Integer.valueOf(0), emprestimoValido.getRenovacoes());
    }
}