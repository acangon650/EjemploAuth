package com.example.demo.servicio.usuario;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.dto.UsuarioDTO;

import org.springframework.stereotype.Service;

import com.example.demo.entidad.Usuario;
import com.example.demo.repositorio.UsuarioRepository;

import jakarta.transaction.Transactional;

/**
 * Servicio para la gestión de usuarios.
 * 
 * <p>Proporciona métodos para guardar, obtener y verificar usuarios en la base de datos.</p>
 * 
 * @version 1.0
 */
@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Guarda un usuario en la base de datos.
     * 
     * <p>Este método codifica la contraseña del usuario antes de guardarlo y establece
     * la relación bidireccional con el perfil de usuario.</p>
     * 
     * @param usuario El usuario a guardar.
     * @return El usuario guardado con su ID generado.
     */
    public Usuario guardar(Usuario usuario) {
        // Codificar la contraseña antes de guardar el usuario
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Establecer la relación bidireccional
        if (usuario.getPerfilusuario() != null) {
            usuario.getPerfilusuario().setUsuario(usuario);
        }

        return usuarioRepositorio.save(usuario);
    }

    /**
     * Busca un usuario por su ID.
     * 
     * @param id El ID del usuario a buscar.
     * @return El usuario encontrado o null si no se encuentra.
     */
    public Usuario obtenerPorId(Long id) {
        Optional<Usuario> resultado = usuarioRepositorio.findById(id);
        return resultado.orElse(null);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * <p>Este método está anotado con @Transactional para garantizar la consistencia de la transacción.</p>
     * 
     * @param username El nombre de usuario a buscar.
     * @return El usuario encontrado.
     */
    @Transactional
    public Usuario obtenerPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username).orElse(null);
    }

    /**
     * Convierte un Usuario en un UsuarioDTO.
     * 
     * <p>Este método convierte una entidad Usuario en un DTO para su uso en la capa de presentación.</p>
     * 
     * @param usuario El usuario a convertir.
     * @return El UsuarioDTO correspondiente.
     */
    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        Set<String> roles = usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        return new UsuarioDTO(usuario.getId(), usuario.getUsername(), roles);
    }

    /**
     * Obtiene un UsuarioDTO por nombre de usuario.
     * 
     * <p>Este método está anotado con @Transactional para garantizar la consistencia de la transacción.</p>
     * 
     * @param username El nombre de usuario a buscar.
     * @return El UsuarioDTO correspondiente.
     */
    @Transactional
    public UsuarioDTO obtenerUsuarioDTO(String username) {
        Usuario usuario = obtenerPorUsername(username);
        return convertirAUsuarioDTO(usuario);
    }

    /**
     * Verifica si un usuario existe por su nombre de usuario.
     * 
     * @param username El nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existe(String username) {
        return usuarioRepositorio.existsByUsername(username);
    }
}