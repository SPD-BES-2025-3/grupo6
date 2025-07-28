package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.ReservaDTO;
import br.com.ufg.odm.model.Reserva;
import br.com.ufg.odm.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<Reserva> reservas = reservaRepository.findAll();

        List<ReservaDTO> reservasDTO = reservas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservasDTO);
    }

    private ReservaDTO convertToDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setIdOrm(reserva.getIdOrm());
        dto.setNomeUsuario(reserva.getNomeUsuario());
        dto.setIdOrmExemplar(reserva.getIdOrmExemplar());
        dto.setStatusReserva(reserva.getStatusReserva());
        return dto;
    }
}
