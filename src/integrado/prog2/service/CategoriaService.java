package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.repository.CategoriaRepository;

import java.sql.SQLException;
import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService() {
        this.categoriaRepository = new CategoriaRepository();
    }

    public Categoria crearCategoria(String nombre, String descripcion) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre de la categoría no puede estar vacío.");
        }
        try {
            Categoria existente = categoriaRepository.findByNombreCualquiera(nombre);
            if (existente != null) {
                if (!existente.isEliminado()) {
                    throw new ValidacionException("Ya existe una categoría activa con el nombre: " + nombre);
                } else {
                    categoriaRepository.reactivar(existente.getId(), descripcion);
                    existente.setEliminado(false);
                    existente.setDescripcion(descripcion);
                    return existente;
                }
            }
            Categoria nueva = new Categoria(nombre, descripcion);
            return categoriaRepository.guardar(nueva);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public List<Categoria> obtenerCategoriasActivas() throws ValidacionException {
        try {
            return categoriaRepository.findAllActivas();
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public Categoria buscarPorId(Long id) throws ValidacionException {
        try {
            Categoria cat = categoriaRepository.findById(id);
            if (cat == null) {
                throw new ValidacionException("No se encontró ninguna categoría activa con el ID: " + id);
            }
            return cat;
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void editarCategoria(Long id, String nuevoNombre, String nuevaDescripcion) throws ValidacionException {
        Categoria categoria = buscarPorId(id);
        try {
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                if (!categoria.getNombre().equalsIgnoreCase(nuevoNombre)) {
                    if (categoriaRepository.existeNombreExcluyendoId(nuevoNombre, id)) {
                        throw new ValidacionException("Ya existe otra categoría con el nombre: " + nuevoNombre);
                    }
                }
                categoria.setNombre(nuevoNombre);
            }
            if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
                categoria.setDescripcion(nuevaDescripcion);
            }
            categoriaRepository.actualizar(categoria);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void eliminarCategoria(Long id) throws ValidacionException {
        buscarPorId(id);
        try {
            categoriaRepository.eliminar(id);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }
}