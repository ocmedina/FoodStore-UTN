package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private List<Producto> productos;
    private Long contadorId;

    // Inyectamos el servicio de categorías porque un producto NECESITA una categoría válida
    private CategoriaService categoriaService;

    public ProductoService(CategoriaService categoriaService) {
        this.productos = new ArrayList<>();
        this.contadorId = 1L;
        this.categoriaService = categoriaService;
    }

    // HU-PROD-02: Crear producto
    public Producto crearProducto(String nombre, String descripcion, Double precio, int stock, String imagen, Boolean disponible, Long idCategoria) throws ValidacionException {
        // Validaciones obligatorias de la consigna
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto no puede estar vacío."); // [cite: 299]
        }
        if (precio < 0) {
            throw new ValidacionException("El precio no puede ser menor a 0."); // [cite: 181, 299]
        }
        if (stock < 0) {
            throw new ValidacionException("El stock no puede ser menor a 0."); // [cite: 181, 299]
        }

        // Buscar la categoría. Si no existe o está eliminada, buscarPorId tira error y frena todo
        Categoria categoria = categoriaService.buscarPorId(idCategoria);

        // Si pasamos todas las validaciones, creamos y guardamos
        Producto nuevoProducto = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
        nuevoProducto.setId(contadorId++);
        productos.add(nuevoProducto);

        return nuevoProducto;
    }

    // HU-PROD-01: Listar productos
    public List<Producto> obtenerProductosActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto prod : productos) {
            // Se muestran solo productos no eliminados [cite: 284]
            if (!prod.isEliminado()) {
                activos.add(prod);
            }
        }
        return activos;
    }

    // Método auxiliar interno para buscar por ID
    public Producto buscarPorId(Long id) throws ValidacionException {
        for (Producto prod : productos) {
            if (prod.getId().equals(id) && !prod.isEliminado()) {
                return prod;
            }
        }
        // Si el id no existe o está eliminado, informamos [cite: 310]
        throw new ValidacionException("No se encontró ningún producto activo con el ID: " + id);
    }

    // HU-PROD-03: Editar producto
    public void editarProducto(Long id, String nombre, String descripcion, Double precio, Integer stock, String imagen, Boolean disponible, Long idCategoria) throws ValidacionException {
        Producto producto = buscarPorId(id);

        // Se permite actualizar uno o más campos (manteniendo los anteriores si el usuario no modifica) [cite: 308]
        if (nombre != null && !nombre.trim().isEmpty()) {
            producto.setNombre(nombre);
        }
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            producto.setDescripcion(descripcion);
        }
        if (precio != null) {
            if (precio < 0) throw new ValidacionException("El precio no puede ser menor a 0."); // [cite: 309]
            producto.setPrecio(precio);
        }
        if (stock != null) {
            if (stock < 0) throw new ValidacionException("El stock no puede ser menor a 0."); // [cite: 309]
            producto.setStock(stock);
        }
        if (imagen != null && !imagen.trim().isEmpty()) {
            producto.setImagen(imagen);
        }
        if (disponible != null) {
            producto.setDisponible(disponible);
        }
        if (idCategoria != null) {
            Categoria nuevaCat = categoriaService.buscarPorId(idCategoria);
            producto.setCategoria(nuevaCat);
        }
    }

    // HU-PROD-04: Eliminar producto (Baja lógica)
    public void eliminarProducto(Long id) throws ValidacionException {
        Producto producto = buscarPorId(id);

        // Se marca eliminado = true [cite: 316]
        producto.setEliminado(true);

        // Si el producto está referenciado en detalles de pedidos, no se remueve de la colección para no romper la integridad de los datos [cite: 318, 319]
    }
}