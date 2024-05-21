package com.example.demo.configuracion;

import java.io.IOException;

import org.hibernate.annotations.Filter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.entidad.enumerado.RolUsuario;
import org.springframework.security.core.Authentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Filtro de autenticación personalizado para redirigir usuarios autenticados a la URL adecuada según su rol.
 * 
 * <p>Este filtro extiende {@link BasicAuthenticationFilter} y redirige a los usuarios
 * autenticados a las páginas de inicio adecuadas según sus roles (ADMIN o USER).</p>
 * 
 * @version 1.0
 */
@Filter(name = "FiltroDeAutenticacionPersonalizado")
public class FiltroDeAutenticacionPersonalizado extends BasicAuthenticationFilter {

    /**
     * Constructor para inicializar el filtro con el {@link AuthenticationManager}.
     * 
     * @param authenticationManager el gestor de autenticación
     */
    public FiltroDeAutenticacionPersonalizado(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Método que intercepta las solicitudes HTTP y redirige a los usuarios autenticados según su rol.
     * 
     * @param request el objeto {@link HttpServletRequest}
     * @param response el objeto {@link HttpServletResponse}
     * @param chain el objeto {@link FilterChain} para continuar la cadena de filtros
     * @throws IOException si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error de servlet
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            boolean isUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_USER.toString()));
            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_ADMIN.toString()));
            String requestURI = request.getRequestURI();

            try {
                // Redirigir si es un usuario y está intentando acceder a /home o /login
                if (isUser && ("/home".equals(requestURI) || "/login".equals(requestURI))) {
                    response.sendRedirect("/user/home"); // URL para usuarios
                    return;
                } else if (isAdmin && ("/home".equals(requestURI) || "/login".equals(requestURI))) {
                    response.sendRedirect("/admin/home"); // URL para administradores
                    return;
                }
            } catch (IOException e) {
                // Manejar la excepción de redirección
                throw new ServletException("Error al redirigir", e);
            }
        }
        // Continuar la cadena de filtros
        chain.doFilter(request, response);
    }
}