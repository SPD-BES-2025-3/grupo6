package br.com.ufg.orm.useCase.reserva;

import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.enums.StatusReserva;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.model.Usuario;
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
class ReservarExemplarTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private ReservarExemplar reservarExemplar;

    private Reserva reservaValida;
    private Usuario usuario;
    private Exemplar exemplar;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .build();

        exemplar = Exemplar.builder()
                .id(1L)
                .disponibilidade(Disponibilidade.DISPONIVEL)
                .build();

        reservaValida = Reserva.builder()
                .usuario(usuario)
                .exemplar(exemplar)
                .dataReserva(LocalDateTime.now())
                .dataPrevistaRetirada(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    @DisplayName("Deve realizar reserva com sucesso quando todos os dados são válidos")
    void deveRealizarReservaComSucesso() {
        // Given
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaValida);

        // When
        Reserva resultado = reservarExemplar.executar(reservaValida);

        // Then
        assertNotNull(resultado);
        assertEquals(reservaValida.getUsuario(), resultado.getUsuario());
        assertEquals(reservaValida.getExemplar(), resultado.getExemplar());
        assertNotNull(resultado.getDataReserva());
        assertNotNull(resultado.getDataLimiteRetirada());
        assertEquals(StatusReserva.ATIVA, resultado.getStatusReserva());

        verify(exemplarRepository).findById(exemplar.getId());
        verify(reservaRepository).existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA);
        verify(exemplarRepository).updateDisponibilidade(exemplar.getId(), Disponibilidade.RESERVADO);
        verify(reservaRepository).save(reservaValida);
    }

    @Test
    @DisplayName("Deve definir data limite de retirada uma semana após data prevista")
    void deveDefinirDataLimiteRetiradaUmaSemanaPosDataPrevista() {
        // Given
        LocalDateTime dataPrevista = LocalDateTime.now().plusDays(2);
        reservaValida.setDataPrevistaRetirada(dataPrevista);

        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva reserva = invocation.getArgument(0);
            assertEquals(dataPrevista.plusWeeks(1), reserva.getDataLimiteRetirada());
            return reserva;
        });

        // When
        reservarExemplar.executar(reservaValida);

        // Then
        verify(reservaRepository).save(reservaValida);
        assertEquals(dataPrevista.plusWeeks(1), reservaValida.getDataLimiteRetirada());
    }

    @Test
    @DisplayName("Deve definir status da reserva como ATIVA")
    void deveDefinirStatusReservaComoAtiva() {
        // Given
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva reserva = invocation.getArgument(0);
            assertEquals(StatusReserva.ATIVA, reserva.getStatusReserva());
            return reserva;
        });

        // When
        reservarExemplar.executar(reservaValida);

        // Then
        verify(reservaRepository).save(reservaValida);
        assertEquals(StatusReserva.ATIVA, reservaValida.getStatusReserva());
    }

    @Test
    @DisplayName("Deve alterar disponibilidade do exemplar para RESERVADO")
    void deveAlterarDisponibilidadeExemplarParaReservado() {
        // Given
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaValida);

        // When
        reservarExemplar.executar(reservaValida);

        // Then
        verify(exemplarRepository).updateDisponibilidade(exemplar.getId(), Disponibilidade.RESERVADO);
    }

    @Test
    @DisplayName("Deve lançar exceção quando reserva for null")
    void deveLancarExcecaoQuandoReservaForNull() {
        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(null));

        assertEquals("Reserva não pode ser nula.", exception.getMessage());
        verifyNoInteractions(reservaRepository, exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário for null")
    void deveLancarExcecaoQuandoUsuarioForNull() {
        // Given
        reservaValida.setUsuario(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Usuário da reserva não pode ser nulo ou sem ID.", exception.getMessage());
        verifyNoInteractions(reservaRepository, exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não tiver ID")
    void deveLancarExcecaoQuandoUsuarioNaoTiverID() {
        // Given
        usuario.setId(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Usuário da reserva não pode ser nulo ou sem ID.", exception.getMessage());
        verifyNoInteractions(reservaRepository, exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar for null")
    void deveLancarExcecaoQuandoExemplarForNull() {
        // Given
        reservaValida.setExemplar(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Exemplar da reserva não pode ser nulo ou sem ID.", exception.getMessage());
        verifyNoInteractions(reservaRepository, exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar não tiver ID")
    void deveLancarExcecaoQuandoExemplarNaoTiverID() {
        // Given
        exemplar.setId(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Exemplar da reserva não pode ser nulo ou sem ID.", exception.getMessage());
        verifyNoInteractions(reservaRepository, exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando data da reserva for null")
    void deveLancarExcecaoQuandoDataReservaForNull() {
        // Given
        reservaValida.setDataReserva(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Data da reserva não pode ser nula.", exception.getMessage());
        verifyNoInteractions(reservaRepository, exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando data prevista de retirada for null")
    void deveLancarExcecaoQuandoDataPrevistaRetiradaForNull() {
        // Given
        reservaValida.setDataPrevistaRetirada(null);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Data prevista de retirada não pode ser nula.", exception.getMessage());
        verifyNoInteractions(reservaRepository, exemplarRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar não estiver disponível")
    void deveLancarExcecaoQuandoExemplarNaoEstiverDisponivel() {
        // Given
        exemplar.setDisponibilidade(Disponibilidade.EMPRESTADO);
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Exemplar não está disponível para reservas.", exception.getMessage());
        verify(exemplarRepository).findById(exemplar.getId());
        verify(reservaRepository, never()).save(any());
        verify(exemplarRepository, never()).updateDisponibilidade(anyLong(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando exemplar não for encontrado")
    void deveLancarExcecaoQuandoExemplarNaoForEncontrado() {
        // Given
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.empty());

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Exemplar não está disponível para reservas.", exception.getMessage());
        verify(exemplarRepository).findById(exemplar.getId());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário já possui reserva ativa")
    void deveLancarExcecaoQuandoUsuarioJaPossuiReservaAtiva() {
        // Given
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(true);

        // When & Then
        NegocioException exception = assertThrows(NegocioException.class,
                () -> reservarExemplar.executar(reservaValida));

        assertEquals("Usuário já possui uma reserva ativa.", exception.getMessage());
        verify(reservaRepository).existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA);
        verify(reservaRepository, never()).save(any());
        verify(exemplarRepository, never()).updateDisponibilidade(anyLong(), any());
    }

    @Test
    @DisplayName("Deve aceitar reserva quando exemplar está disponível")
    void deveAceitarReservaQuandoExemplarEstaDisponivel() {
        // Given
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaValida);

        // When & Then
        assertDoesNotThrow(() -> reservarExemplar.executar(reservaValida));
        verify(exemplarRepository).findById(exemplar.getId());
        verify(reservaRepository).existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA);
        verify(exemplarRepository).updateDisponibilidade(exemplar.getId(), Disponibilidade.RESERVADO);
        verify(reservaRepository).save(reservaValida);
    }

    @Test
    @DisplayName("Deve aceitar reserva quando usuário não possui reserva ativa")
    void deveAceitarReservaQuandoUsuarioNaoPossuiReservaAtiva() {
        // Given
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaValida);

        // When & Then
        assertDoesNotThrow(() -> reservarExemplar.executar(reservaValida));
        verify(reservaRepository).existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA);
        verify(reservaRepository).save(reservaValida);
    }

    @Test
    @DisplayName("Deve definir data da reserva como agora ao executar")
    void deveDefinirDataReservaComoAgoraAoExecutar() {
        // Given
        LocalDateTime antes = LocalDateTime.now();
        when(exemplarRepository.findById(exemplar.getId())).thenReturn(Optional.of(exemplar));
        when(reservaRepository.existsByUsuarioIdAndStatusReserva(usuario.getId(), StatusReserva.ATIVA))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva reserva = invocation.getArgument(0);
            LocalDateTime depois = LocalDateTime.now();
            assertTrue(reserva.getDataReserva().isAfter(antes) || reserva.getDataReserva().isEqual(antes));
            assertTrue(reserva.getDataReserva().isBefore(depois) || reserva.getDataReserva().isEqual(depois));
            return reserva;
        });

        // When
        reservarExemplar.executar(reservaValida);

        // Then
        verify(reservaRepository).save(reservaValida);
        assertNotNull(reservaValida.getDataReserva());
    }
}