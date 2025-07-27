package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.model.Usuario;
import jakarta.validation.constraints.NotNull;

public record RealizarEmprestimoRequestDto(
        Long idExemplar,
        Long idReserva,
        @NotNull Long idUsuario,
        String observacoes
) {

    public Emprestimo toEmprestimo() {
        Emprestimo emprestimo = new Emprestimo();
        if (idReserva != null) {
            emprestimo.setReserva(Reserva.builder().id(idReserva).build());
        }else if (idExemplar != null) {
            emprestimo.setExemplar(Exemplar.builder().id(idExemplar).build());
        }
        emprestimo.setUsuario(Usuario.builder().id(idUsuario).build());
        emprestimo.setObservacoes(observacoes);
        return emprestimo;
    }
}
