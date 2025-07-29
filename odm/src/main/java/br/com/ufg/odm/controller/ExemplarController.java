package br.com.ufg.odm.controller;

import br.com.ufg.odm.model.Exemplar;
import br.com.ufg.odm.repository.ExemplarRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exemplar")
@Tag(name = "Exemplares", description = "API para gerenciamento de exemplares")
public class ExemplarController {

    private final ExemplarRepository exemplarRepository;

    @GetMapping
    @Operation(summary = "Listar todos os exemplares", description = "Retorna uma lista com todos os exemplares cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exemplares retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exemplar.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<Exemplar>> listarExemplares() {
        List<Exemplar> exemplares = exemplarRepository.findAll();
        return ResponseEntity.ok(exemplares);
    }

    @GetMapping("/livro/{livroId}")
    @Operation(summary = "Listar exemplares por livro", description = "Retorna uma lista com todos os exemplares de um livro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exemplares do livro retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exemplar.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<Exemplar>> listarExemplaresPorLivro(
            @Parameter(description = "ID do livro", required = true) @PathVariable Long livroId) {
        List<Exemplar> exemplares = exemplarRepository.findByIdLivroOrm(livroId);
        return ResponseEntity.ok(exemplares);
    }
}
