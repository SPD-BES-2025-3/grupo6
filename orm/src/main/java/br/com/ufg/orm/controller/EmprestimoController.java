package br.com.ufg.orm.controller;

import br.com.ufg.orm.dto.EmprestimoResponseDto;
import br.com.ufg.orm.dto.RealizarEmprestimoRequestDto;
import br.com.ufg.orm.enums.StatusEmprestimo;
import br.com.ufg.orm.model.Emprestimo;
import br.com.ufg.orm.repository.EmprestimoRepository;
import br.com.ufg.orm.useCase.emprestimo.DevolverEmprestimo;
import br.com.ufg.orm.useCase.emprestimo.RealizarEmprestimo;
import br.com.ufg.orm.useCase.emprestimo.RenovarEmprestimo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emprestimo")
@Tag(name = "Empréstimos", description = "Operações relacionadas ao gerenciamento de empréstimos de livros")
public class EmprestimoController {

    private final RealizarEmprestimo realizarEmprestimo;
    private final DevolverEmprestimo devolverEmprestimo;
    private final RenovarEmprestimo renovarEmprestimo;
    private final EmprestimoRepository emprestimoRepository;

    @PreAuthorize("principal.podeManipularEmprestimos()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Realizar empréstimo", description = "Realiza um novo empréstimo de livro para um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Empréstimo realizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuário ou livro não encontrado", content = @Content),
        @ApiResponse(responseCode = "409", description = "Livro não disponível para empréstimo", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<EmprestimoResponseDto> realizarEmprestimo(
            @Parameter(description = "Dados do empréstimo a ser realizado", required = true)
            @RequestBody RealizarEmprestimoRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(EmprestimoResponseDto.from(
                realizarEmprestimo.executar(requestDto.toEmprestimo())
        ));
    }

    @PreAuthorize("principal.podeManipularEmprestimos()")
    @PutMapping("/{id}")
    @Operation(summary = "Devolver empréstimo", description = "Realiza a devolução de um empréstimo ativo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empréstimo devolvido com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Empréstimo já foi devolvido", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<EmprestimoResponseDto> devolverEmprestimo(
            @Parameter(description = "ID do empréstimo a ser devolvido", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(EmprestimoResponseDto.from(
                devolverEmprestimo.executar(Emprestimo.builder().id(id).build())
        ));
    }

    @PreAuthorize("principal.podeManipularEmprestimos()")
    @PutMapping("/{id}/renovar")
    @Operation(summary = "Renovar empréstimo", description = "Renova um empréstimo ativo estendendo seu prazo de devolução")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empréstimo renovado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Empréstimo não pode ser renovado", content = @Content),
        @ApiResponse(responseCode = "409", description = "Empréstimo já foi devolvido ou vencido", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<EmprestimoResponseDto> renovarEmprestimo(
            @Parameter(description = "ID do empréstimo a ser renovado", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(EmprestimoResponseDto.from(
                renovarEmprestimo.executar(Emprestimo.builder().id(id).build())
        ));
    }

    @GetMapping
    @Operation(summary = "Listar empréstimos", description = "Lista todos os empréstimos registrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<EmprestimoResponseDto>> listarEmprestimos() {
        List<Emprestimo> emprestimos = (List<Emprestimo>) emprestimoRepository.findAll();
        List<EmprestimoResponseDto> responseDtos = emprestimos.stream()
                .map(EmprestimoResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar empréstimos por usuário", description = "Lista todos os empréstimos de um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empréstimos do usuário retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<EmprestimoResponseDto>> listarEmprestimosPorUsuario(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long usuarioId) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuarioId(usuarioId);
        List<EmprestimoResponseDto> responseDtos = emprestimos.stream()
                .map(EmprestimoResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar empréstimos por status", description = "Lista todos os empréstimos com um status específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empréstimos com o status especificado retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Status inválido", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<EmprestimoResponseDto>> listarEmprestimosPorStatus(
            @Parameter(description = "Status do empréstimo (ATIVO, ATRASADO, DEVOLVIDO, RENOVADO)", required = true)
            @PathVariable StatusEmprestimo status) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByStatus(status);
        List<EmprestimoResponseDto> responseDtos = emprestimos.stream()
                .map(EmprestimoResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
}
