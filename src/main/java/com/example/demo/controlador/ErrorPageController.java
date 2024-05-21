package com.example.demo.controlador;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador para manejar las páginas de error.
 * 
 * <p>Este controlador proporciona el manejo de diferentes códigos de error HTTP,
 * redirigiendo a las páginas de error correspondientes.</p>
 * 
 * @version 1.0
 */
@Controller
public class ErrorPageController implements ErrorController {

    /**
     * Maneja las solicitudes a la URL de error.
     * 
     * <p>Este método determina el código de estado HTTP y redirige a la página
     * de error correspondiente.</p>
     * 
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente
     * @return el nombre de la vista para la página de error correspondiente
     */
    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404"; // Página de error 404
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500"; // Página de error 500
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403"; // Página de error 403
            }
        }
        return "error/default"; // Página de error por defecto
    }
}