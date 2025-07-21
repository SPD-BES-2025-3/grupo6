package br.com.ufg.orm.useCase.livro;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Livro;
import br.com.ufg.orm.repository.LivroRepository;
import br.com.ufg.orm.useCase.UseCase;
import br.com.ufg.orm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncluirLivro implements UseCase<Livro, Livro> {

    private final LivroRepository livroRepository;

    @Override
    public Livro executar(Livro livro) {
        validar(livro);
        return livroRepository.save(livro);
    }

    @Override
    public void validar(Livro livro) {
        if (livro == null) {
            throw new NegocioException("Livro não pode ser null.");
        }

        if (StringUtil.isNUllOuVazio(livro.getNome())) {
            throw new NegocioException("Nome do livro não pode ser vazio.");
        }

        if (StringUtil.isNUllOuVazio(livro.getAutor())) {
            throw new NegocioException("Autor do livro não pode ser vazio.");
        }

        if (StringUtil.isNUllOuVazio(livro.getEditora())) {
            throw new NegocioException("Editora do livro não pode ser vazia.");
        }

        if (StringUtil.isNUllOuVazio(livro.getAnoLancamento())) {
            throw new NegocioException("Ano de lançamento do livro não pode ser vazio.");
        }

        // Verifica se já existe um livro com mesmo nome e autor
        if (livroRepository.existsByNomeAndAutor(livro.getNome(), livro.getAutor())) {
            throw new NegocioException("Já existe um livro cadastrado com o nome: " +
                                      livro.getNome() + " e autor: " + livro.getAutor());
        }
    }
}
