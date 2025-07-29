package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.LivroDTO;
import br.com.ufg.odm.model.Livro;
import br.com.ufg.odm.repository.LivroRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/livro")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "API para gerenciamento de livros")
public class LivroController {

    private final LivroRepository livroRepository;

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista com todos os livros cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<LivroDTO>> listarLivros() {
        List<Livro> livros = livroRepository.findAll();
//        List<Livro> livros = new ArrayList<>();

        List<LivroDTO> livrosDTO = livros.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(livrosDTO);
    }

    private LivroDTO convertToDTO(Livro livro) {
        LivroDTO dto = new LivroDTO();
        dto.setId(livro.getId());
        dto.setIdOrm(livro.getIdOrm());
        dto.setNome(livro.getNome());
        dto.setAnoLancamento(livro.getAnoLancamento());
        dto.setAutor(livro.getAutor());
        dto.setEditora(livro.getEditora());
        return dto;
    }
}
