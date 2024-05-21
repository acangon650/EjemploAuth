package com.example.demo.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.servicio.usuario.UsuarioPersonalizadoDetailsService;

/**
 * Configuración de seguridad web para la aplicación.
 * 
 * <p>Esta clase configura la seguridad de la aplicación utilizando Spring Security,
 * incluyendo la gestión de autenticación y autorización.</p>
 * 
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UsuarioPersonalizadoDetailsService userDetailsService;

    /**
     * Configura la cadena de filtros de seguridad.
     * 
     * @param http la configuración de seguridad HTTP
     * @param authenticationConfiguration la configuración de autenticación
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManager authentication = authenticationConfiguration.getAuthenticationManager();
        if (authentication != null) {
            http.addFilterBefore(new FiltroDeAutenticacionPersonalizado(authentication), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/admin/**").hasRole("ADMIN")  // Rutas bajo "/admin/" requieren el rol ADMIN
                    .requestMatchers("/user/**").hasRole("USER")    // Rutas bajo "/user/" requieren el rol USER
                    .requestMatchers("/login", "/", "/home", "/crear", "/public/**").permitAll() // Permite el acceso a estas rutas sin autenticación
                    .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
                )
                .userDetailsService(userDetailsService)
                // Configuración para el proceso de inicio de sesión
                .formLogin(formLogin -> formLogin
                    .loginPage("/login")  // URL personalizada de inicio de sesión
                    .successHandler(new CustomAuthenticationSuccessHandler())  // Usa el manejador personalizado                        
                    .permitAll()
                )
                // Configuración para el proceso de cierre de sesión
                .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)       // Invalida la sesión actual
                    .clearAuthentication(true)        // Limpia la autenticación
                    .deleteCookies("JSESSIONID")      // Borra la cookie de sesión (opcional)
                    .permitAll()
                );
        }
        return http.build();
    }

    /**
     * Bean para el gestor de autenticación.
     * 
     * @param authenticationConfiguration la configuración de autenticación
     * @return el gestor de autenticación
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean para el codificador de contraseñas.
     * 
     * @return el codificador de contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}