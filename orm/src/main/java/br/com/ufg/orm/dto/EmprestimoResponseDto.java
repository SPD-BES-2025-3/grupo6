package br.com.ufg.orm.dto;

import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmprestimoResponseDto {
    private Long id;
    private Usuario usuario;
    private Exemplar exemplar;
    private Reserva reserva;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataPrevistaDevolucao;
    private LocalDateTime dataDevolucao;
    private StatusEmprestimo status;
    private Integer renovacoes;
    private String observacoes;

    public static EmprestimoResponseDto from(Emprestimo emprestimo) {
        EmprestimoResponseDto response = new EmprestimoResponseDto();
        response.setId(emprestimo.getId());
        if (emprestimo.getUsuario() != null) {
            response.setUsuario(Usuario.builder().id(emprestimo.getUsuario().getId()).nome(emprestimo.getUsuario().getNome()).build());
        }

        if (emprestimo.getExemplar() != null) {
            response.setExemplar(Exemplar.builder().id(emprestimo.getExemplar().getId()).build());
        }

        if (emprestimo.getReserva() != null) {
            response.setReserva(Reserva.builder().id(emprestimo.getReserva().getId()).build());
        }

        response.setDataEmprestimo(emprestimo.getDataEmprestimo());
        response.setDataPrevistaDevolucao(emprestimo.getDataPrevistaDevolucao());
        response.setDataDevolucao(emprestimo.getDataDevolucao());
        response.setStatus(emprestimo.getStatus());
        response.setRenovacoes(emprestimo.getRenovacoes());
        response.setObservacoes(emprestimo.getObservacoes());

        return response;
    }
}
