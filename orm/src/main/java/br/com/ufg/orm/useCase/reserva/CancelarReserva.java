package br.com.ufg.orm.useCase.reserva;

import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.enums.StatusReserva;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.repository.ReservaRepository;
import br.com.ufg.orm.useCase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancelarReserva implements UseCase<Reserva, Reserva> {

    private final ExemplarRepository exemplarRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    @Override
    public Reserva executar(Reserva reserva) {
        validar(reserva);

        reserva.setStatusReserva(StatusReserva.CANCELADA);
        reserva.getExemplar().setDisponibilidade(Disponibilidade.DISPONIVEL);
        exemplarRepository.updateDisponibilidade(reserva.getExemplar().getId(), Disponibilidade.DISPONIVEL);
        return reservaRepository.save(reserva);
    }

    @Override
    public void validar(Reserva reserva) {
        if (reserva == null || reserva.getId() == null) {
            throw new NegocioException("Reserva inválida: deve conter um ID válido.");
        }

        if (reserva.getExemplar() == null || reserva.getExemplar().getId() == null) {
            throw new NegocioException("Reserva inválida: deve conter um exemplar válido.");
        }
    }
}
