package br.com.ufg.orm.exception;

/**
 * Exception para representar erros de regras de negócio da aplicação.
 * Será utilizada para capturar e retornar mensagens claras ao usuário da API.
 */
public class NegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NegocioException(String mensagem) {
        super(mensagem);
    }

    public NegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
