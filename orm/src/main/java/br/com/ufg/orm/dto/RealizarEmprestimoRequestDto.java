package br.com.ufg.orm.dto;

import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.util.AuthUtil;

public record RealizarEmprestimoRequestDto(
        Long idExemplar,
        Long idReserva,
        String observacoes
) {

    public Emprestimo toEmprestimo() {
        Emprestimo emprestimo = new Emprestimo();
        if (idReserva != null) {
            emprestimo.setReserva(Reserva.builder().id(idReserva).build());
        }else if (idExemplar != null) {
            emprestimo.setExemplar(Exemplar.builder().id(idExemplar).build());
        }
        emprestimo.setUsuario(AuthUtil.getCurrentUserEntity());
        emprestimo.setObservacoes(observacoes);
        return emprestimo;
    }
}
