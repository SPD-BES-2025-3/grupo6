package br.com.ufg.odm.repository;

import br.com.ufg.odm.model.Livro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends MongoRepository<Livro, String> {
}
