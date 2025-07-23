package br.com.ufg.orm.useCase.livro;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.repository.LivroRepository;
import br.com.ufg.orm.useCase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExcluirLivro implements UseCase<Long, Void>{

    private final LivroRepository livroRepository;

    @Override
    public Void executar(Long idLivro) {
        validar(idLivro);
        livroRepository.deleteById(idLivro);
        return null;
    }

    @Override
    public void validar(Long idLivro) {
        if (idLivro == null) {
            throw new NegocioException("ID do livro não pode ser null.");
        }

        if (!livroRepository.existsById(idLivro)) {
            throw new NegocioException("Não existe um livro cadastrado com o ID: " + idLivro);
        }
    }
}
