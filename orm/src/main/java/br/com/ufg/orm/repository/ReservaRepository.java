package br.com.ufg.orm.repository;

import br.com.ufg.orm.enums.StatusReserva;
import br.com.ufg.orm.model.Reserva;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends CrudRepository<Reserva, Long> {
    boolean existsByUsuarioIdAndStatusReserva(Long idUsuario, StatusReserva statusReserva);

    List<Reserva> findByUsuarioId(Long usuarioId);
}
