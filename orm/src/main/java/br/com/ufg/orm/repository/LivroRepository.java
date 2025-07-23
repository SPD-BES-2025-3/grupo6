package br.com.ufg.orm.repository;

import br.com.ufg.orm.model.Livro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends CrudRepository<Livro, Long> {
    boolean existsByNomeAndAutor(String nome, String autor);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Livro l " +
            "WHERE l.id <> ?1 AND l.nome = ?2 AND l.autor = ?3")
    boolean existsAnotherByNomeAndAutor(Long id, String nome, String autor);
}
