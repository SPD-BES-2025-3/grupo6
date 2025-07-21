package br.com.ufg.orm.useCase.livro;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Livro;
import br.com.ufg.orm.repository.LivroRepository;
import br.com.ufg.orm.useCase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlterarLivro implements UseCase<Livro, Livro> {

    private final LivroRepository livroRepository;

    @Override
    public Livro executar(Livro livro) {
        validar(livro);
        return livroRepository.save(livro);
    }

    @Override
    public void validar(Livro livro) {
        LivroValidator.validar(livro);

        if (livroRepository.existsAnotherByNomeAndAutor(livro.getId(), livro.getNome(), livro.getAutor())) {
            throw new NegocioException("Já existe um livro cadastrado com o nome: " +
                                       livro.getNome() + " e autor: " + livro.getAutor());
        }
    }
}
