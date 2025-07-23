package br.com.ufg.orm.controller;

import br.com.ufg.orm.dto.IncluirLIvroRequestDto;
import br.com.ufg.orm.model.Livro;
import br.com.ufg.orm.repository.LivroRepository;
import br.com.ufg.orm.useCase.livro.AlterarLivro;
import br.com.ufg.orm.useCase.livro.ExcluirLivro;
import br.com.ufg.orm.useCase.livro.IncluirLivro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/livro")
@Tag(name = "Livros", description = "Operações relacionadas ao gerenciamento de livros")
public class LivroController {

    private final IncluirLivro incluirLivro;
    private final LivroRepository livroRepository;
    private final AlterarLivro alterarLivro;
    private final ExcluirLivro excluirLivro;

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista com todos os livros cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Iterable<Livro>> getAllLivros() {
        return ResponseEntity.ok(livroRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna um livro específico baseado no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Livro> getLivroById(
            @Parameter(description = "ID do livro a ser buscado", required = true)
            @PathVariable Long id) {
        return livroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo livro", description = "Cria um novo livro no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Livro> incluirLivro(
            @Parameter(description = "Dados do livro a ser criado", required = true)
            @RequestBody IncluirLIvroRequestDto requestDto){
        return ResponseEntity.ok(incluirLivro.executar(requestDto.toLivro()));
    }

    @PutMapping
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Livro> atualizarLivro(
            @Parameter(description = "Dados do livro a ser atualizado", required = true)
            @RequestBody IncluirLIvroRequestDto requestDto) {
        if (!livroRepository.existsById(requestDto.id())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(alterarLivro.executar(requestDto.toLivro()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro", description = "Remove um livro do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Void> excluirLivro(
            @Parameter(description = "ID do livro a ser excluído", required = true)
            @PathVariable Long id) {
        if (!livroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        excluirLivro.executar(id);
        return ResponseEntity.noContent().build();
    }
}
