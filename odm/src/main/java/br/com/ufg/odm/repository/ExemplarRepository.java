package br.com.ufg.odm.repository;

import br.com.ufg.odm.model.Exemplar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemplarRepository extends MongoRepository<Exemplar, String> {
}
