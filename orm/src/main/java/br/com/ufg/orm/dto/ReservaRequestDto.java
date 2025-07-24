package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.util.AuthUtil;

import java.time.LocalDateTime;

public record ReservaRequestDto(
        Long idExemplar,
        LocalDateTime dataPrevistaRetirada,
        String observacoes
) {
    public Reserva toReserva() {
        return Reserva.builder()
                .exemplar(Exemplar.builder().id(idExemplar).build())
                .dataPrevistaRetirada(dataPrevistaRetirada)
                .observacoes(observacoes)
                .usuario(AuthUtil.getCurrentUserEntity())
                .build();
    }
}
