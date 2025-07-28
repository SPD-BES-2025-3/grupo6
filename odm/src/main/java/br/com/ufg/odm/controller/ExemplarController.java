package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.ExemplarDTO;
import br.com.ufg.odm.model.Exemplar;
import br.com.ufg.odm.repository.ExemplarRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exemplar")
@Tag(name = "Exemplares", description = "API para gerenciamento de exemplares")
public class ExemplarController {

    @Autowired
    private ExemplarRepository exemplarRepository;

    @GetMapping
    @Operation(summary = "Listar todos os exemplares", description = "Retorna uma lista com todos os exemplares cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exemplares retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<ExemplarDTO>> listarExemplares() {
//        List<Exemplar> exemplares = exemplarRepository.findAll();
        List<Exemplar> exemplares = new ArrayList<>();

        List<ExemplarDTO> exemplaresDTO = exemplares.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(exemplaresDTO);
    }

    @GetMapping("/livro/{livroId}")
    @Operation(summary = "Listar exemplares por livro", description = "Retorna uma lista com todos os exemplares de um livro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exemplares do livro retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarDTO.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<ExemplarDTO>> listarExemplaresPorLivro(
            @Parameter(description = "ID do livro", required = true) @PathVariable String livroId) {
//        List<Exemplar> exemplares = exemplarRepository.findByLivroId(livroId);
        List<Exemplar> exemplares = new ArrayList<>();

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
