package br.com.ufg.odm.controller;

import br.com.ufg.odm.model.Emprestimo;
import br.com.ufg.odm.repository.EmprestimoRepository;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/emprestimo")
@Tag(name = "Empréstimos", description = "API para gerenciamento de empréstimos")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;

    @GetMapping
    @Operation(summary = "Listar todos os empréstimos", description = "Retorna uma lista com todos os empréstimos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Emprestimo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<Emprestimo>> listarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        return ResponseEntity.ok(emprestimos);
    }
}
