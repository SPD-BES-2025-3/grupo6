package br.com.ufg.orm.repository;

import br.com.ufg.orm.enums.Disponibilidade;
import br.com.ufg.orm.model.Exemplar;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExemplarRepository extends CrudRepository<Exemplar, Long> {
    boolean existsByCodigoIdentificacao(String codigoIdentificacao);

    Exemplar findByCodigoIdentificacao(String codigoIdentificacao);

    List<Exemplar> findAllByLivroId(Long livroId);

    List<Exemplar> findAllByLivroIdAndDisponibilidade(Long livroId, Disponibilidade disponibilidade);

    @Modifying
    @Query("UPDATE Exemplar e SET e.disponibilidade = ?2 WHERE e.id = ?1")
    void updateDisponibilidade(Long id, Disponibilidade disponibilidade);

}
