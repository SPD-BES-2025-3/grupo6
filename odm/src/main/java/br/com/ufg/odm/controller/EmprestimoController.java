package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.EmprestimoDTO;
import br.com.ufg.odm.model.Emprestimo;
import br.com.ufg.odm.repository.EmprestimoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emprestimo")
@Tag(name = "Empréstimos", description = "API para gerenciamento de empréstimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @GetMapping
    @Operation(summary = "Listar todos os empréstimos", description = "Retorna uma lista com todos os empréstimos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<EmprestimoDTO>> listarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();

        List<EmprestimoDTO> emprestimosDTO = emprestimos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(emprestimosDTO);
    }

    private EmprestimoDTO convertToDTO(Emprestimo emprestimo) {
        EmprestimoDTO dto = new EmprestimoDTO();
        dto.setId(emprestimo.getId());
        dto.setIdOrm(emprestimo.getIdOrm());
        dto.setNomeUsuario(emprestimo.getNomeUsuario());
        dto.setIdOrmExemplar(emprestimo.getIdOrmExemplar());
        dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
        dto.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
        dto.setDataDevolucao(emprestimo.getDataDevolucao());
        dto.setStatus(emprestimo.getStatus());
        dto.setRenovacoes(emprestimo.getRenovacoes());
        return dto;
    }
}
