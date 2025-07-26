package br.com.ufg.orm.controller;

import br.com.ufg.orm.dto.AlterarExemplarRequestDto;
import br.com.ufg.orm.dto.ExemplarResponseDto;
import br.com.ufg.orm.dto.IncluirExemplarRequestDto;
import br.com.ufg.orm.model.Exemplar;
import br.com.ufg.orm.repository.ExemplarRepository;
import br.com.ufg.orm.useCase.exemplar.AlterarExemplar;
import br.com.ufg.orm.useCase.exemplar.ExcluirExemplar;
import br.com.ufg.orm.useCase.exemplar.IncluirExemplar;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exemplar")
@Tag(name = "Exemplares", description = "Operações relacionadas ao gerenciamento de exemplares")
public class ExemplarController {

    private final IncluirExemplar incluirExemplar;
    private final ExemplarRepository exemplarRepository;
    private final AlterarExemplar alterarExemplar;
    private final ExcluirExemplar excluirExemplar;

    @Transactional(readOnly = true)
    @GetMapping
    @Operation(summary = "Listar todos os exemplares", description = "Retorna uma lista com todos os exemplares cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de exemplares retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<ExemplarResponseDto>> getAllExemplares() {
        List<Exemplar> exemplares = (List<Exemplar>) exemplarRepository.findAll();
        return ResponseEntity.ok(ExemplarResponseDto.from(exemplares));
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @Operation(summary = "Buscar exemplar por ID", description = "Retorna um exemplar específico baseado no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exemplar encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Exemplar não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<ExemplarResponseDto> getExemplarById(
            @Parameter(description = "ID do exemplar a ser buscado", required = true)
            @PathVariable Long id) {
        return exemplarRepository.findById(id)
                .map(exemplar -> ResponseEntity.ok(ExemplarResponseDto.from(exemplar)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/livro/{idLivro}")
    @Operation(summary = "Listar exemplares por livro", description = "Retorna todos os exemplares de um livro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de exemplares retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<ExemplarResponseDto>> getExemplaresByLivroId(
            @Parameter(description = "ID do livro para buscar exemplares", required = true)
            @PathVariable Long idLivro) {
        List<Exemplar> exemplares = exemplarRepository.findAllByLivroId(idLivro);
        return ResponseEntity.ok(ExemplarResponseDto.from(exemplares));
    }

    @PostMapping
    @Operation(summary = "Criar novo exemplar", description = "Cria um novo exemplar no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exemplar criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<ExemplarResponseDto> incluirExemplar(
            @Parameter(description = "Dados do exemplar a ser criado", required = true)
            @RequestBody IncluirExemplarRequestDto requestDto) {
        Exemplar exemplarSalvo = incluirExemplar.executar(requestDto.toExemplar());
        return ResponseEntity.ok(ExemplarResponseDto.from(exemplarSalvo));
    }

    @PutMapping
    @Operation(summary = "Atualizar exemplar", description = "Atualiza os dados de um exemplar existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exemplar atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Exemplar não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<ExemplarResponseDto> atualizarExemplar(
            @Parameter(description = "Dados do exemplar a ser atualizado", required = true)
            @RequestBody AlterarExemplarRequestDto requestDto) {
        if (!exemplarRepository.existsById(requestDto.id())) {
            return ResponseEntity.notFound().build();
        }
        Exemplar exemplarAtualizado = alterarExemplar.executar(requestDto.toExemplar());
        return ResponseEntity.ok(ExemplarResponseDto.from(exemplarAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir exemplar", description = "Remove um exemplar do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Exemplar excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Exemplar não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Void> excluirExemplar(
            @Parameter(description = "ID do exemplar a ser excluído", required = true)
            @PathVariable Long id) {
        if (!exemplarRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        excluirExemplar.executar(id);
        return ResponseEntity.noContent().build();
    }
}
