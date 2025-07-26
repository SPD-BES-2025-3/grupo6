package br.com.ufg.orm.useCase.emprestimo;

import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.repository.EmprestimoRepository;
import br.com.ufg.orm.useCase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RenovarEmprestimo implements UseCase<Emprestimo, Emprestimo> {

    private final EmprestimoRepository emprestimoRepository;

    @Override
    public Emprestimo executar(Emprestimo emprestimo) {
        validar(emprestimo);
        emprestimo.setStatus(StatusEmprestimo.RENOVADO);
        emprestimo.setRenovacoes(emprestimo.getRenovacoes() + 1);
        emprestimo.setDataPrevistaDevolucao(LocalDateTime.now().plusWeeks(1));
        return emprestimoRepository.save(emprestimo);
    }

    @Override
    public void validar(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getId() == null) {
            throw new NegocioException("O empréstimo não pode ser nulo ou sem ID.");
        }

        emprestimo = emprestimoRepository.findById(emprestimo.getId())
                .orElseThrow(() -> new NegocioException("Empréstimo não encontrado."));

        if (emprestimo.getStatus().equals(StatusEmprestimo.DEVOLVIDO) || emprestimo.getDataDevolucao() != null) {
            throw new NegocioException("O empréstimo já foi devolvido.");
        }

        if (!emprestimo.getStatus().equals(StatusEmprestimo.ATRASADO)) {
            throw new NegocioException("O empréstimo não está atrasado e não pode ser renovado.");
        }
    }
}
