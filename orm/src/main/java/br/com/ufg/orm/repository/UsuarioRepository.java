package br.com.ufg.orm.repository;

import br.com.ufg.orm.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByLogin(String login);
}
