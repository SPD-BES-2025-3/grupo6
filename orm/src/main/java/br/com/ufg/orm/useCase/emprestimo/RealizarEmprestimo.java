package br.com.ufg.orm.useCase.emprestimo;

import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.enums.StatusReserva;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.model.Reserva;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.EmprestimoRepository;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.repository.ReservaRepository;
import br.com.ufg.orm.useCase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RealizarEmprestimo implements UseCase<Emprestimo, Emprestimo> {

    private final EmprestimoRepository emprestimoRepository;
    private final ExemplarRepository exemplarRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    @Override
    public Emprestimo executar(Emprestimo emprestimo) {
        validar(emprestimo);

        emprestimo.setStatus(StatusEmprestimo.ATIVO);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataPrevistaDevolucao(LocalDateTime.now().plusWeeks(1));
        emprestimo.setRenovacoes(0);
        if (emprestimo.getReserva() != null) {
            emprestimo.setExemplar(emprestimo.getReserva().getExemplar());
            reservaRepository.alterarStatus(emprestimo.getReserva().getId(), StatusReserva.RETIRADA);
        }

        exemplarRepository.updateDisponibilidade(
                emprestimo.getExemplar().getId(),
                Disponibilidade.EMPRESTADO
        );
       return emprestimoRepository.save(emprestimo);
    }

    @Override
    public void validar(Emprestimo emprestimo) {
        if (emprestimo == null) {
            throw new NegocioException("O empréstimo não pode ser nulo.");
        }

        if (emprestimo.getUsuario() == null || emprestimo.getUsuario().getId() == null) {
            throw new NegocioException("O usuário do empréstimo não pode ser nulo.");
        }

        if (usuarioPossuiEmprestimosAtivos(emprestimo.getUsuario())){
            throw new NegocioException("O usuário já possui empréstimos ativos.");
        }


        if (isEmprestimoPorReserva(emprestimo)){
            if (!isReservaValidaParaEmprestimo(emprestimo.getReserva(), emprestimo.getUsuario())){
                throw new NegocioException("A reserva não é válida para o empréstimo pois não pertence ao usuário informado ou não está ativa.");
            }
        }else {
            if (emprestimo.getExemplar() == null || emprestimo.getExemplar().getId() == null) {
                throw new NegocioException("O exemplar do empréstimo não pode ser nulo.");
            }

            if (!isExemplarDisponivelParaEmprestimo(emprestimo.getExemplar().getId())) {
                throw new NegocioException("O exemplar não está disponível para empréstimo.");
            }
        }

    }

    private boolean isReservaValidaParaEmprestimo(Reserva reserva, Usuario usuario) {
        reserva = reservaRepository.findById(reserva.getId())
                .orElseThrow(() -> new NegocioException("Reserva não encontrada."));

        return reserva.getUsuario().getId().equals(usuario.getId()) &&
                reserva.getStatusReserva() == StatusReserva.ATIVA;
    }

    private boolean isEmprestimoPorReserva(Emprestimo emprestimo) {
        return emprestimo.getReserva() != null && emprestimo.getReserva().getId() != null;
    }

    private boolean isExemplarDisponivelParaEmprestimo(Long id) {
        return exemplarRepository.findById(id).map(exemplar -> exemplar.getDisponibilidade().equals(Disponibilidade.DISPONIVEL)).orElse(false);
    }

    private boolean usuarioPossuiEmprestimosAtivos(Usuario usuario) {
        return emprestimoRepository.existsByUsuarioIdAndStatus(
                usuario.getId(),
                StatusEmprestimo.ATIVO
        );
    }
}
