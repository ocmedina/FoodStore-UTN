package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    // Nuestra "Base de Datos" en memoria
    private List<Categoria> categorias;
    // Un contador manual para simular el auto-incremento de la base de datos
    private Long contadorId;

    public CategoriaService() {
        this.categorias = new ArrayList<>();
        this.contadorId = 1L;
    }

    // HU-CAT-02: Crear categoría
    public Categoria crearCategoria(String nombre, String descripcion) throws ValidacionException {
        // 1. Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre de la categoría no puede estar vacío.");
        }

        // 2. Validar que el nombre sea único
        for (Categoria cat : categorias) {
            if (!cat.isEliminado() && cat.getNombre().equalsIgnoreCase(nombre)) {
                throw new ValidacionException("Ya existe una categoría activa con el nombre: " + nombre);
            }
        }

        // 3. Crear el objeto, asignarle el ID y guardarlo en la colección
        Categoria nuevaCategoria = new Categoria(nombre, descripcion);
        nuevaCategoria.setId(contadorId++); // Asigna el ID actual y luego le suma 1 al contador
        categorias.add(nuevaCategoria);

        return nuevaCategoria;
    }

    // HU-CAT-01: Listar categorías
    public List<Categoria> obtenerCategoriasActivas() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria cat : categorias) {
            // Solo devolvemos las que NO están eliminadas (Soft Delete)
            if (!cat.isEliminado()) {
                activas.add(cat);
            }
        }
        return activas;
    }

    // Método auxiliar interno para buscar por ID
    public Categoria buscarPorId(Long id) throws ValidacionException {
        for (Categoria cat : categorias) {
            if (cat.getId().equals(id) && !cat.isEliminado()) {
                return cat;
            }
        }
        throw new ValidacionException("No se encontró ninguna categoría activa con el ID: " + id);
    }

    // HU-CAT-03: Editar categoría
    public void editarCategoria(Long id, String nuevoNombre, String nuevaDescripcion) throws ValidacionException {
        // Buscamos la categoría (si no existe, el método buscarPorId lanza la excepción y corta la ejecución acá)
        Categoria categoria = buscarPorId(id);

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            // Validar unicidad si le estamos cambiando el nombre por otro distinto
            if (!categoria.getNombre().equalsIgnoreCase(nuevoNombre)) {
                for (Categoria cat : categorias) {
                    if (!cat.isEliminado() && cat.getNombre().equalsIgnoreCase(nuevoNombre)) {
                        throw new ValidacionException("Ya existe otra categoría con el nombre: " + nuevoNombre);
                    }
                }
            }
            categoria.setNombre(nuevoNombre);
        }

        if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
            categoria.setDescripcion(nuevaDescripcion);
        }
    }

    // HU-CAT-04: Eliminar categoría (Baja Lógica)
    public void eliminarCategoria(Long id) throws ValidacionException {
        Categoria categoria = buscarPorId(id);

        // Regla de negocio: Baja lógica (Soft delete)
        categoria.setEliminado(true);

        // Nota: En un sistema completo, acá también validaríamos si tiene productos asociados
        // antes de dejarla eliminar. Lo podemos agregar más adelante en ProductoService.
    }
}