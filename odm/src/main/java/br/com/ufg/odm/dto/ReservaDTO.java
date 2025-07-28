package br.com.ufg.odm.dto;

import br.com.ufg.odm.enums.StatusReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private String id;
    private Long idOrm;
    private String nomeUsuario;
    private String idOrmExemplar;
    private StatusReserva statusReserva;
}
