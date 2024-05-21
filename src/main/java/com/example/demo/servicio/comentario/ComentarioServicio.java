package com.example.demo.servicio.comentario;


import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import com.example.demo.entidad.Comentario;

	/**
	 * Servicio para la gestión de comentarios.
	 * 
	 * <p>Proporciona métodos para listar, guardar, eliminar y buscar comentarios
	 * en la base de datos, así como para obtener usuarios que han hecho comentarios.</p>
	 * 
	 * @version 1.0
	 */
	public interface ComentarioServicio {

	    /**
	     * Recupera una página de comentarios.
	     * 
	     * @param pageable Parámetro que contiene la información de paginación y ordenación.
	     * @return Una página de comentarios según los parámetros de paginación.
	     */
	    Page<Comentario> listarTodos(Pageable pageable);

	    /**
	     * Guarda un comentario en la base de datos.
	     * 
	     * @param comentario El comentario a guardar.
	     * @return El comentario guardado con su ID generado.
	     */
	    Comentario guardar(Comentario comentario);

	    /**
	     * Elimina un comentario de la base de datos.
	     * 
	     * @param id El ID del comentario a eliminar.
	     */
	    void eliminar(Long id);

	    /**
	     * Busca un comentario por su ID.
	     * 
	     * @param id El ID del comentario a buscar.
	     * @return El comentario encontrado o null si no se encuentra.
	     */
	    Comentario obtenerPorId(Long id);

	    /**
	     * Recupera una porción (slice) de comentarios.
	     * 
	     * <p>Este método es útil cuando se necesita paginación pero no se requiere
	     * información sobre el número total de páginas o elementos. Ideal para
	     * situaciones de carga de más datos o scroll infinito.</p>
	     * 
	     * @param pageable Parámetro que contiene la información de paginación.
	     * @return Una porción de comentarios según los parámetros de paginación.
	     */
	    Slice<Comentario> listarTodosComoSlice(Pageable pageable);

	    /**
	     * Lista comentarios filtrados por usuario.
	     * 
	     * @param pageable Parámetro que contiene la información de paginación y ordenación.
	     * @param username El nombre de usuario para filtrar los comentarios.
	     * @return Una página de comentarios filtrados por el nombre de usuario.
	     */
	    Page<Comentario> listarFiltroPorUsuario(Pageable pageable, String username);

	    /**
	     * Obtiene una lista de nombres de usuarios que han hecho comentarios.
	     * 
	     * @return Una lista de nombres de usuarios que han hecho comentarios.
	     */
	    List<String> obtenerUsuariosConComentarios();

	    /**
	     * Busca comentarios por una palabra clave en su contenido.
	     * 
	     * @param pageable Parámetro que contiene la información de paginación y ordenación.
	     * @param palabraClave La palabra clave para buscar en los comentarios.
	     * @return Una página de comentarios que contienen la palabra clave.
	     */
	    Page<Comentario> buscarPorPalabraClave(Pageable pageable, String palabraClave);
	

}
