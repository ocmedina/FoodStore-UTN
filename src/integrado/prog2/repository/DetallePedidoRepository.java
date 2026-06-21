package integrado.prog2.repository;

import integrado.prog2.config.DatabaseConnection;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoRepository {

    private final ProductoRepository productoRepository;

    public DetallePedidoRepository(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public DetallePedido guardar(DetallePedido detalle, Long pedidoId) throws SQLException {
        String sql = "INSERT INTO detalles_pedido (cantidad, subtotal, pedido_id, producto_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getSubtotal());
            ps.setLong(3, pedidoId);
            ps.setLong(4, detalle.getProducto().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                detalle.setId(rs.getLong(1));
            }
        }
        return detalle;
    }

    public List<DetallePedido> findByPedidoId(Long pedidoId) throws SQLException {
        String sql = "SELECT id, cantidad, subtotal, producto_id, eliminado, created_at " +
                     "FROM detalles_pedido WHERE pedido_id = ? AND eliminado = false";
        List<DetallePedido> lista = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, pedidoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private DetallePedido mapear(ResultSet rs) throws SQLException {
        DetallePedido d = new DetallePedido();
        d.setId(rs.getLong("id"));
        d.setEliminado(rs.getBoolean("eliminado"));
        d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        d.setCantidad(rs.getInt("cantidad"));

        long prodId = rs.getLong("producto_id");
        if (!rs.wasNull()) {
            Producto prod = productoRepository.findById(prodId);
            if (prod != null) d.setProducto(prod);
        }

        d.setSubtotal(rs.getDouble("subtotal"));
        return d;
    }
}
