package br.com.ufg.orm.repository;

import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.model.Emprestimo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends CrudRepository<Emprestimo, Long> {
    boolean existsByUsuarioIdAndStatus(Long usuarioId, StatusEmprestimo status);

    List<Emprestimo> findByUsuarioId(Long usuarioId);

    List<Emprestimo> findByStatus(StatusEmprestimo status);
}
