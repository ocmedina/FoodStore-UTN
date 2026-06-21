package integrado.prog2.repository;

import integrado.prog2.config.DatabaseConnection;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {

    private final CategoriaRepository categoriaRepository;

    public ProductoRepository(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Producto guardar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, imagen, disponible, categoria_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.getDisponible());
            ps.setLong(7, producto.getCategoria().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                producto.setId(rs.getLong(1));
            }
        }
        return producto;
    }

    public List<Producto> findAllActivos() throws SQLException {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen, disponible, eliminado, created_at, categoria_id " +
                     "FROM productos WHERE eliminado = false ORDER BY id";
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Producto findById(Long id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen, disponible, eliminado, created_at, categoria_id " +
                     "FROM productos WHERE id = ? AND eliminado = false";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, imagen=?, disponible=?, categoria_id=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.getDisponible());
            ps.setLong(7, producto.getCategoria().getId());
            ps.setLong(8, producto.getId());
            ps.executeUpdate();
        }
    }

    public void actualizarStock(Long id, int nuevoStock) throws SQLException {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void descontarStock(Long id, int cantidad) throws SQLException {
        Producto prod = findById(id);
        if (prod != null) {
            int nuevoStock = prod.getStock() - cantidad;
            actualizarStock(id, nuevoStock);
        }
    }

    public void eliminar(Long id) throws SQLException {
        String sql = "UPDATE productos SET eliminado = true WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagen(rs.getString("imagen"));
        p.setDisponible(rs.getBoolean("disponible"));
        p.setEliminado(rs.getBoolean("eliminado"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        long catId = rs.getLong("categoria_id");
        if (!rs.wasNull()) {
            Categoria cat = categoriaRepository.findById(catId);
            p.setCategoria(cat);
        }
        return p;
    }
}
