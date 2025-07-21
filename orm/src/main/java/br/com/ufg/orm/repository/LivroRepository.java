package br.com.ufg.orm.repository;

import br.com.ufg.orm.model.Livro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends CrudRepository<Livro, Long> {
    boolean existsByNomeAndAutor(String nome, String autor);
}
