package com.example.demo.configuracion;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.entidad.enumerado.RolUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

/**
 * Manejador de éxito de autenticación personalizado.
 * Redirige a los usuarios autenticados a la URL adecuada según su rol.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * Maneja el evento de autenticación exitosa.
     * Redirige al usuario a la URL determinada basada en su rol.
     * 
     * @param request el HttpServletRequest
     * @param response el HttpServletResponse
     * @param authentication el objeto Authentication
     * @throws IOException si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error de servlet
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * Determina la URL de redirección basada en el rol del usuario autenticado.
     * 
     * @param authentication el objeto Authentication
     * @return la URL de redirección
     * @throws IllegalStateException si el usuario no tiene un rol adecuado
     */
    protected String determineTargetUrl(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_ADMIN.toString()));
        boolean isUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_USER.toString()));

        if (isAdmin) {
            return "/admin/home"; // URL para administradores
        } else if (isUser) {
            return "/user/home"; // URL para usuarios
        } else {
            throw new IllegalStateException("El usuario no tiene un rol adecuado.");
        }
    }
}
