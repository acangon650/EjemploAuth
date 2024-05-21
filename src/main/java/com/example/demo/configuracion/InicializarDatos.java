package com.example.demo.configuracion;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entidad.Comentario;
import com.example.demo.entidad.PerfilUsuario;
import com.example.demo.entidad.Usuario;
import com.example.demo.entidad.enumerado.RolUsuario;
import com.example.demo.repositorio.ComentarioRepository;
import com.example.demo.repositorio.UsuarioRepository;

import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

/**
 * Clase para inicializar datos en la base de datos al inicio de la aplicación.
 * Implementa {@link CommandLineRunner} para ejecutar el método run al iniciar la aplicación.
 * 
 * @version 1.0
 */
@Component
public class InicializarDatos implements CommandLineRunner {

	 @Autowired
	 private UsuarioRepository usuarioRepository;

	 @Autowired
	 private ComentarioRepository comentarioRepository;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;

	 Faker faker = new Faker();
	 /**
	     * Método ejecutado al inicio de la aplicación para inicializar datos.
	     * 
	     * @param args argumentos de línea de comandos
	     * @throws Exception si ocurre un error durante la inicialización
	     */
	 @Override
	 public void run(String... args) throws Exception {
		   // Asegúrate de que los roles existan

		    // Crear o buscar el usuario 'user1' y asignarle el rol 'ROLE_USER'
		    try {
			    crearOBuscarUsuario("user1", "user1", RolUsuario.ROLE_USER);

		    }catch(Exception e) {
		    	System.out.println(e.toString());
		    }
	 try {
		 crearOBuscarUsuario("user2", "user2", RolUsuario.ROLE_USER);
		    }catch(Exception e) {
		    	System.out.println(e.toString());
		    }
	 try {
		    crearOBuscarUsuario("admin", "admin", RolUsuario.ROLE_ADMIN);

	 }catch(Exception e) {
	 	System.out.println(e.toString());
	 }
		    
	 		comentarioRepository.deleteAll();
		    // Crear comentarios
		    crearComentarioUsuario("user1");
		    crearComentarioUsuario("user2");
	 }

	 /**
	     * Crea o busca un usuario en la base de datos y le asigna un rol.
	     * 
	     * @param username el nombre de usuario
	     * @param password la contraseña del usuario
	     * @param rol el rol a asignar al usuario
	     * @return el usuario creado o encontrado
	     */
	 @Transactional
	 private Usuario crearOBuscarUsuario(String username, String password, RolUsuario rol) {
	     return usuarioRepository.findByUsername(username).orElseGet(() -> {
	         Usuario nuevoUsuario = new Usuario();
	         PerfilUsuario perfil = new PerfilUsuario();
	         
	         perfil.setNombre(faker.name().firstName());
	         perfil.setApellido(faker.name().lastName());
	         perfil.setEmail(faker.internet().emailAddress());
	         
	         nuevoUsuario.setUsername(username);
	         nuevoUsuario.setPassword(passwordEncoder.encode(password));
	         nuevoUsuario.getRoles().add(rol);
	         
	         nuevoUsuario.setPerfilusuario(perfil);
	         perfil.setUsuario(nuevoUsuario);
	         return usuarioRepository.save(nuevoUsuario);
	     });
	 }
	    /**
	     * Crea comentarios para un usuario específico.
	     * 
	     * @param usuario el nombre de usuario
	     */
	 @Transactional
	 private void crearComentarioUsuario(String usuario) {
	     Usuario user = usuarioRepository.findByUsername(usuario).orElse(null);
	     for (int i = 0; i < 5; i++) {
	         Comentario comentario = new Comentario();
	         try {
	             String contenido = faker.lorem().paragraph();
	             if (contenido.length() > 255) {
	                 contenido = contenido.substring(0, 255);
	             }
	             comentario.setContenido(contenido);
	         } catch(Exception e) {
	             // Manejo de excepciones
	         }
	         comentario.setUsuario(user);
	         comentario.setFechaCreacion(LocalDateTime.now());
	         comentarioRepository.save(comentario);
	     }
	 }

	}