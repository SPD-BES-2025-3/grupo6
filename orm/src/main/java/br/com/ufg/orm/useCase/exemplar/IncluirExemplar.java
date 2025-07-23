package br.com.ufg.orm.useCase.exemplar;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.useCase.UseCase;
import br.com.ufg.orm.util.CodigoIdentificacaoGenerator;
import br.com.ufg.orm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class IncluirExemplar implements UseCase<Exemplar, Exemplar>{

    private final ExemplarRepository exemplarRepository;
    private final CodigoIdentificacaoGenerator codigoGenerator;

    @Override
    public Exemplar executar(Exemplar exemplar) {
        if (exemplar == null) {
            throw new NegocioException("Exemplar não pode ser null.");
        }

        if (StringUtil.isNUllOuVazio(exemplar.getCodigoIdentificacao())) {
            exemplar.setCodigoIdentificacao(codigoGenerator.gerarCodigo());
        }

        validar(exemplar);

        exemplar.setDataCriacao(LocalDateTime.now());
        return exemplarRepository.save(exemplar);
    }

    @Override
    public void validar(Exemplar exemplar) {
        if (exemplar.getLivro() == null) {
            throw new NegocioException("Livro do exemplar não pode ser null.");
        }

        if (exemplar.getLivro().getId() == null) {
            throw new NegocioException("ID do livro do exemplar não pode ser null.");
        }

        // Se o código foi fornecido, verifica se já existe
        if (!StringUtil.isNUllOuVazio(exemplar.getCodigoIdentificacao()) &&
            exemplarRepository.existsByCodigoIdentificacao(exemplar.getCodigoIdentificacao())) {
            throw new NegocioException("Já existe um exemplar cadastrado com o código de identificação: " +
                                      exemplar.getCodigoIdentificacao());
        }

        if (exemplar.getConservacao() == null) {
            throw new NegocioException("Estado de conservação do exemplar não pode ser null.");
        }

        if (exemplar.getNumeroEdicao() == null) {
            throw new NegocioException("Número da edição do exemplar não pode ser null.");
        }

        if (exemplar.getNumeroEdicao() <= 0) {
            throw new NegocioException("Número da edição deve ser maior que zero.");
        }

        if (exemplar.getDisponibilidade() == null) {
            throw new NegocioException("Disponibilidade do exemplar não pode ser null.");
        }
    }
}
