package com.example.demo.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Controlador para las funcionalidades de administración.
 * 
 * <p>Este controlador maneja las solicitudes relacionadas con la administración,
 * proporcionando acceso a la página de inicio del administrador.</p>
 * 
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    /**
     * Maneja las solicitudes GET para la página de inicio del administrador.
     * 
     * <p>Este método requiere que el usuario tenga el rol de ADMIN para acceder a la página.
     * Redirige al usuario a la página de inicio del administrador.</p>
     * 
     * @return el nombre de la vista para la página de inicio del administrador
     */
    @GetMapping("/home")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
    	logger.info("@ INFO :: ### Acceso a Página Admin . ###");
        return "auth/admin/home"; // Muestra la página específica del administrador (admin.html)
    }
}