package integrado.prog2.repository;

import integrado.prog2.config.DatabaseConnection;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {

    private final UsuarioRepository usuarioRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoRepository(UsuarioRepository usuarioRepository, DetallePedidoRepository detallePedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    public Pedido guardar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (fecha, estado, total, forma_pago, usuario_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(pedido.getFecha()));
            ps.setString(2, pedido.getEstado().name());
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getFormaPago().name());
            ps.setLong(5, pedido.getUsuario().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId(rs.getLong(1));
            }
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            detallePedidoRepository.guardar(detalle, pedido.getId());
        }

        return pedido;
    }

    public List<Pedido> findAllActivos() throws SQLException {
        String sql = "SELECT id, fecha, estado, total, forma_pago, usuario_id, eliminado, created_at " +
                     "FROM pedidos WHERE eliminado = false ORDER BY id";
        List<Pedido> lista = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Pedido findById(Long id) throws SQLException {
        String sql = "SELECT id, fecha, estado, total, forma_pago, usuario_id, eliminado, created_at " +
                     "FROM pedidos WHERE id = ? AND eliminado = false";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public void actualizarEstado(Long id, Estado estado, FormaPago formaPago) throws SQLException {
        String sql = "UPDATE pedidos SET estado = ?, forma_pago = COALESCE(?, forma_pago) WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setString(2, formaPago != null ? formaPago.name() : null);
            ps.setLong(3, id);
            ps.executeUpdate();
        }
    }

    public void eliminar(Long id) throws SQLException {
        String sql = "UPDATE pedidos SET eliminado = true WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getLong("id"));
        p.setFecha(rs.getDate("fecha").toLocalDate());
        p.setEstado(Estado.valueOf(rs.getString("estado")));
        p.setTotal(rs.getDouble("total"));
        p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
        p.setEliminado(rs.getBoolean("eliminado"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        long userId = rs.getLong("usuario_id");
        if (!rs.wasNull()) {
            Usuario user = usuarioRepository.findById(userId);
            p.setUsuario(user);
        }

        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(p.getId());
        p.setDetalles(detalles);

        return p;
    }
}
