package br.com.ufg.orm.repository;

import br.com.ufg.orm.model.Exemplar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExemplarRepository extends CrudRepository<Exemplar, Long> {
    boolean existsByCodigoIdentificacao(String codigoIdentificacao);

    Exemplar findByCodigoIdentificacao(String codigoIdentificacao);

    List<Exemplar> findAllByLivroId(Long livroId);
}
