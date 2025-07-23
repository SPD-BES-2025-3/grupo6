package br.com.ufg.orm.controller;

import br.com.ufg.orm.dto.IncluirUsuarioRequestDto;
import br.com.ufg.orm.dto.UsuarioResponseDto;
import br.com.ufg.orm.exception.NegocioException;
import br.com.ufg.orm.model.Usuario;
import br.com.ufg.orm.repository.UsuarioRepository;
import br.com.ufg.orm.useCase.usuario.IncluirUsuario;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
@Tag(name = "Usuários", description = "Operações relacionadas ao gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final IncluirUsuario incluirUsuario;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico baseado no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<UsuarioResponseDto> getUsuarioById(
            @Parameter(description = "ID do usuário a ser buscado", required = true)
            @PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado com o ID: " + id));
        return ResponseEntity.ok(UsuarioResponseDto.from(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Iterable<UsuarioResponseDto>> getAllUsuarios() {
        Iterable<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(UsuarioResponseDto.from(usuarios));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<UsuarioResponseDto> createUsuario(
            @Parameter(description = "Dados do usuário a ser criado", required = true)
            @RequestBody IncluirUsuarioRequestDto requestDto) {
        Usuario usuario = requestDto.toUsuario();
        return ResponseEntity.ok(UsuarioResponseDto.from(incluirUsuario.executar(usuario)));
    }
}
