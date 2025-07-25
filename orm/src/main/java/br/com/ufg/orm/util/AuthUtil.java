package br.com.ufg.orm.util;

import br.com.ufg.orm.config.CustomUserPrincipal;
import br.com.ufg.orm.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    /**
     * Retorna o usuário atualmente autenticado
     */
    public static CustomUserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            return (CustomUserPrincipal) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    /**
     * Retorna o ID do usuário atualmente autenticado
     */
    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Retorna o nome do usuário atualmente autenticado
     */
    public static String getCurrentUserName() {
        return getCurrentUser().getNome();
    }

    /**
     * Retorna o login do usuário atualmente autenticado
     */
    public static String getCurrentUserLogin() {
        return getCurrentUser().getUsername();
    }

    /**
     * Retorna o objeto Usuario completo do usuário autenticado
     */
    public static Usuario getCurrentUserEntity() {
        return getCurrentUser().getUsuario();
    }

    /**
     * Verifica se há um usuário autenticado
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal;
    }
}
