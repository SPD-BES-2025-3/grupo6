package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.ExemplarDTO;
import br.com.ufg.odm.model.Exemplar;
import br.com.ufg.odm.repository.ExemplarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exemplar")
public class ExemplarController {

    @Autowired
    private ExemplarRepository exemplarRepository;

    @GetMapping
    public ResponseEntity<List<ExemplarDTO>> listarExemplares() {
        List<Exemplar> exemplares = exemplarRepository.findAll();

        List<ExemplarDTO> exemplaresDTO = exemplares.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(exemplaresDTO);
    }

    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<ExemplarDTO>> listarExemplaresPorLivro(@PathVariable String livroId) {
        List<Exemplar> exemplares = exemplarRepository.findByLivroId(livroId);

        List<ExemplarDTO> exemplaresDTO = exemplares.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(exemplaresDTO);
    }

    private ExemplarDTO convertToDTO(Exemplar exemplar) {
        ExemplarDTO dto = new ExemplarDTO();
        dto.setId(exemplar.getId());
        dto.setIdOrm(exemplar.getIdOrm());
        dto.setCodigoIdentificacao(exemplar.getCodigoIdentificacao());
        dto.setConservacao(exemplar.getConservacao());
        dto.setNumeroEdicao(exemplar.getNumeroEdicao());
        dto.setDisponibilidade(exemplar.getDisponibilidade());
        dto.setDataCriacao(exemplar.getDataCriacao());

        // Informações básicas do livro (evitando referência circular)
        if (exemplar.getLivro() != null) {
            dto.setLivroId(exemplar.getLivro().getId());
            dto.setLivroNome(exemplar.getLivro().getNome());
            dto.setLivroAutor(exemplar.getLivro().getAutor());
        }

        return dto;
    }
}
