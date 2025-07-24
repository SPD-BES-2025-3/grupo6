package br.com.ufg.orm.useCase.reserva;

import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.enums.StatusReserva;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.repository.ReservaRepository;
import br.com.ufg.orm.useCase.UseCase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class ReservarExemplar implements UseCase<Reserva, Reserva> {

    private final ReservaRepository reservaRepository;
    private final ExemplarRepository exemplarRepository;

    public ReservarExemplar(ReservaRepository reservaRepository, ExemplarRepository exemplarRepository) {
        this.reservaRepository = reservaRepository;
        this.exemplarRepository = exemplarRepository;
    }

    @Transactional
    @Override
    public Reserva executar(Reserva reserva) {
        validar(reserva);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setDataLimiteRetirada(reserva.getDataPrevistaRetirada().plusWeeks(1));
        reserva.setStatusReserva(StatusReserva.ATIVA);

        exemplarRepository.updateDisponibilidade(reserva.getExemplar().getId(), Disponibilidade.RESERVADO);
        return reservaRepository.save(reserva);
    }

    @Override
    public void validar(Reserva reserva) {
        if (reserva == null) {
            throw new NegocioException("Reserva não pode ser nula.");
        }

        if (reserva.getUsuario() == null || reserva.getUsuario().getId() == null) {
            throw new NegocioException("Usuário da reserva não pode ser nulo ou sem ID.");
        }

        if (reserva.getExemplar() == null || reserva.getExemplar().getId() == null) {
            throw new NegocioException("Exemplar da reserva não pode ser nulo ou sem ID.");
        }

        if (reserva.getDataReserva() == null) {
            throw new NegocioException("Data da reserva não pode ser nula.");
        }

        if (reserva.getDataPrevistaRetirada() == null) {
            throw new NegocioException("Data prevista de retirada não pode ser nula.");
        }

        if (!isExemplarDisponivel(reserva.getExemplar().getId())) {
            throw new NegocioException("Exemplar não está disponível para reservas.");
        }

        if (isUsuarioJaPossuiReservaAtiva(reserva.getUsuario().getId())) {
            throw new NegocioException("Usuário já possui uma reserva ativa.");
        }
    }

    private boolean isUsuarioJaPossuiReservaAtiva(Long idUsuario) {
        return reservaRepository.existsByUsuarioIdAndStatusReserva(idUsuario, StatusReserva.ATIVA);
    }

    private boolean isExemplarDisponivel(Long idExemplar) {
        return exemplarRepository.findById(idExemplar)
                .map(exemplar -> exemplar.getDisponibilidade() == Disponibilidade.DISPONIVEL)
                .orElse(false);
    }
}
