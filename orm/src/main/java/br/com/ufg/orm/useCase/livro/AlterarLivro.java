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
        if (livro == null || livro.getId() == null) {
            throw new NegocioException("Livro ou ID do livro não podem ser nulos.");
        }

        Livro livroExistente = livroRepository.findById(livro.getId())
                .orElseThrow(() -> new NegocioException("Livro não encontrado com o ID: " + livro.getId()));

        livroExistente.setNome(livro.getNome());
        livroExistente.setAutor(livro.getAutor());
        livroExistente.setEditora(livro.getEditora());
        livroExistente.setAnoLancamento(livro.getAnoLancamento());

        validar(livro);

        return livroRepository.save(livroExistente);
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
