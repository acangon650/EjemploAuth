package com.example.demo.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entidad.PerfilUsuario;
import com.example.demo.servicio.usuario.UsuarioServicio;
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
	
	@Autowired
	private UsuarioServicio usuarioServicio;
	
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
    public String admin(Model model, Authentication authentication) {
    	logger.info("@ INFO :: ### Acceso a Página Admin . ###");
    	String usernameAuth = authentication.getName();
        model.addAttribute("username", usernameAuth);
    	try {
            PerfilUsuario perfilUsuario = usuarioServicio.obtenerPorUsername(usernameAuth).getPerfilusuario();
            model.addAttribute("perfilUsuario", perfilUsuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "auth/admin/home"; // Muestra la página específica del administrador (admin.html)
    }
    
}