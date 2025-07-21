package br.com.ufg.orm.useCase.livro;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Livro;
import br.com.ufg.orm.util.StringUtil;

public class LivroValidator {

    public static void validar(Livro livro) {
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
    }
}
