package br.com.ufg.orm.repository;

import br.com.ufg.orm.model.Exemplar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemplarRepository extends CrudRepository<Exemplar, Long> {
    boolean existsByCodigoIdentificacao(String codigoIdentificacao);
}
