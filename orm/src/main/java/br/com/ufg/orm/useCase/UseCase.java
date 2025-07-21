package br.com.ufg.orm.useCase;

public interface UseCase <INPUT, OUTPUT>{
    OUTPUT executar(INPUT input);
    void validar(INPUT input);
}
