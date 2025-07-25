package br.com.ufg.orm.useCase.exemplar;

import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.useCase.UseCase;
import br.com.ufg.orm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AlterarExemplar implements UseCase<Exemplar, Exemplar> {

    private final ExemplarRepository exemplarRepository;

    @Override
    public Exemplar executar(Exemplar exemplar) {
        if (exemplar == null) {
            throw new NegocioException("Exemplar não pode ser null.");
        }

        Exemplar existingExemplar = exemplarRepository.findById(exemplar.getId())
            .orElseThrow(() -> new NegocioException("Exemplar não encontrado com o ID: " + exemplar.getId()));

        existingExemplar.setDisponibilidade(exemplar.getDisponibilidade());
        existingExemplar.setConservacao(exemplar.getConservacao());
        existingExemplar.setNumeroEdicao(exemplar.getNumeroEdicao());

        validar(existingExemplar);

        return exemplarRepository.save(existingExemplar);
    }

    @Override
    public void validar(Exemplar exemplar) {
        if (exemplar.getId() == null) {
            throw new NegocioException("ID do exemplar não pode ser null.");
        }

        Exemplar exemplarExistente = exemplarRepository.findById(exemplar.getId())
            .orElseThrow(() -> new NegocioException("Exemplar não encontrado com o ID: " + exemplar.getId()));

        if (!Objects.equals(exemplarExistente.getCodigoIdentificacao(), exemplar.getCodigoIdentificacao())) {
            throw new NegocioException("Código de identificação não pode ser alterado.");
        }

        if (StringUtil.isNUllOuVazio(exemplar.getCodigoIdentificacao())) {
            throw new NegocioException("Código de identificação do exemplar não pode ser vazio.");
        }

        if (exemplar.getLivro() == null) {
            throw new NegocioException("Livro do exemplar não pode ser null.");
        }

        if (exemplar.getLivro().getId() == null) {
            throw new NegocioException("ID do livro do exemplar não pode ser null.");
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

        if (exemplar.getDataCriacao() == null){
            throw new NegocioException("Data de criação do exemplar não pode ser null.");
        }
    }
}
