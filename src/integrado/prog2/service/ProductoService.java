package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.repository.CategoriaRepository;
import integrado.prog2.repository.ProductoRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public ProductoService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
        this.productoRepository = new ProductoRepository(new CategoriaRepository());
    }

    public Producto crearProducto(String nombre, String descripcion, Double precio, int stock, String imagen, Boolean disponible, Long idCategoria) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto no puede estar vacío.");
        }
        if (precio < 0) throw new ValidacionException("El precio no puede ser menor a 0.");
        if (stock < 0)  throw new ValidacionException("El stock no puede ser menor a 0.");

        Categoria categoria = categoriaService.buscarPorId(idCategoria);

        try {
            Producto nuevo = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
            return productoRepository.guardar(nuevo);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public List<Producto> obtenerProductosActivos() throws ValidacionException {
        try {
            return productoRepository.findAllActivos();
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public Producto buscarPorId(Long id) throws ValidacionException {
        try {
            Producto p = productoRepository.findById(id);
            if (p == null) {
                throw new ValidacionException("No se encontró ningún producto activo con el ID: " + id);
            }
            return p;
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void editarProducto(Long id, String nombre, String descripcion, Double precio, Integer stock, String imagen, Boolean disponible, Long idCategoria) throws ValidacionException {
        Producto producto = buscarPorId(id);

        if (nombre != null && !nombre.trim().isEmpty()) producto.setNombre(nombre);
        if (descripcion != null && !descripcion.trim().isEmpty()) producto.setDescripcion(descripcion);
        if (precio != null) {
            if (precio < 0) throw new ValidacionException("El precio no puede ser menor a 0.");
            producto.setPrecio(precio);
        }
        if (stock != null) {
            if (stock < 0) throw new ValidacionException("El stock no puede ser menor a 0.");
            producto.setStock(stock);
        }
        if (imagen != null && !imagen.trim().isEmpty()) producto.setImagen(imagen);
        if (disponible != null) producto.setDisponible(disponible);
        if (idCategoria != null) {
            Categoria nuevaCat = categoriaService.buscarPorId(idCategoria);
            producto.setCategoria(nuevaCat);
        }

        try {
            productoRepository.actualizar(producto);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void eliminarProducto(Long id) throws ValidacionException {
        buscarPorId(id);
        try {
            productoRepository.eliminar(id);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void actualizarStock(Long id, int nuevoStock) throws ValidacionException {
        try {
            productoRepository.actualizarStock(id, nuevoStock);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }
}