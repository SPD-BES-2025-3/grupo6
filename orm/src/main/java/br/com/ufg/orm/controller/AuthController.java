package br.com.ufg.orm.controller;

import br.com.ufg.orm.config.CustomUserPrincipal;
import br.com.ufg.orm.config.JwtUtil;
import br.com.ufg.orm.dto.LoginRequestDto;
import br.com.ufg.orm.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna token JWT")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getSenha())
            );

            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userPrincipal);

            // Salvar na sessão do Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            session.setAttribute("JWT_TOKEN", token);
            session.setAttribute("USER_ID", userPrincipal.getId());

            LoginResponseDto response = LoginResponseDto.builder()
                .token(token)
                .nome(userPrincipal.getNome())
                .login(userPrincipal.getUsername())
                .permissoes(userPrincipal.getPermissoes())
                .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro no login: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Realizar logout", description = "Invalida a sessão do usuário")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @GetMapping("/me")
    @Operation(summary = "Obter usuário logado", description = "Retorna informações do usuário autenticado")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

            LoginResponseDto response = LoginResponseDto.builder()
                .nome(userPrincipal.getNome())
                .login(userPrincipal.getUsername())
                .permissoes(userPrincipal.getPermissoes())
                .build();

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body("Usuário não autenticado");
    }
}
