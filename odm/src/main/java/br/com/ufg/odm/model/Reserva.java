package br.com.ufg.odm.model;

import br.com.ufg.odm.enums.StatusReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reservas")
public class Reserva {

    @Id
    private String id;

    private Long idOrm;
    private String nomeUsuario;
    private String idOrmExemplar;
    private StatusReserva statusReserva;
}
