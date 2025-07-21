package br.com.ufg.orm.repository;

import br.com.ufg.orm.model.Exemplar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemplarRepository extends CrudRepository<Exemplar, Long> {
    boolean existsByCodigoIdentificacao(String codigoIdentificacao);

    Exemplar findByCodigoIdentificacao(String codigoIdentificacao);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Exemplar e WHERE e.id != :id AND e.codigoIdentificacao = :codigoIdentificacao")
    boolean existsAnotherByCodigoIdentificacao(@Param("id") Long id, @Param("codigoIdentificacao") String codigoIdentificacao);
}
