package br.com.ufg.orm.useCase.exemplar;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.useCase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExcluirExemplar implements UseCase<Long, Void> {

    private final ExemplarRepository exemplarRepository;

    @Override
    public Void executar(Long idExemplar) {
        validar(idExemplar);
        exemplarRepository.deleteById(idExemplar);
        return null;
    }

    @Override
    public void validar(Long idExemplar) {
        if (idExemplar == null) {
            throw new NegocioException("ID do exemplar não pode ser null.");
        }

        if (!exemplarRepository.existsById(idExemplar)) {
            throw new NegocioException("Não existe um exemplar cadastrado com o ID: " + idExemplar);
        }
    }
}
