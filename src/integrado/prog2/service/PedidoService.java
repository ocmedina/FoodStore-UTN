package integrado.prog2.service;

import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.repository.DetallePedidoRepository;
import integrado.prog2.repository.PedidoRepository;
import integrado.prog2.repository.ProductoRepository;
import integrado.prog2.repository.UsuarioRepository;
import integrado.prog2.repository.CategoriaRepository;

import java.sql.SQLException;
import java.util.List;

public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoService(UsuarioService usuarioService, ProductoService productoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;

        UsuarioRepository usuarioRepo   = new UsuarioRepository();
        CategoriaRepository catRepo     = new CategoriaRepository();
        ProductoRepository productoRepo = new ProductoRepository(catRepo);
        DetallePedidoRepository detalleRepo = new DetallePedidoRepository(productoRepo);
        this.pedidoRepository = new PedidoRepository(usuarioRepo, detalleRepo);
    }

    public Pedido iniciarPedido(Long idUsuario, FormaPago formaPago) throws ValidacionException {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        return new Pedido(Estado.PENDIENTE, formaPago, usuario);
    }

    public void agregarDetalleAPedido(Pedido pedido, Long idProducto, int cantidad) throws ValidacionException {
        Producto producto = productoService.buscarPorId(idProducto);
        
        int cantidadTotalEnPedido = cantidad;
        DetallePedido detalleExistente = pedido.findeDetallePedidoByProducto(producto);
        if (detalleExistente != null) {
            cantidadTotalEnPedido += detalleExistente.getCantidad();
        }
        
        if (producto.getStock() < cantidadTotalEnPedido) {
            throw new ValidacionException("Stock insuficiente para: " + producto.getNombre());
        }
        
        if (detalleExistente != null) {
            detalleExistente.setCantidad(cantidadTotalEnPedido);
            pedido.calcularTotal();
        } else {
            pedido.addDetallePedido(cantidad, 0.0, producto);
        }
    }

    public void confirmarYGuardarPedido(Pedido pedido) throws ValidacionException {
        if (pedido.getDetalles().isEmpty()) {
            throw new ValidacionException("No se puede guardar un pedido sin productos.");
        }
        try {
            pedidoRepository.guardar(pedido);

            for (DetallePedido detalle : pedido.getDetalles()) {
                try {
                    productoService.descontarStock(
                        detalle.getProducto().getId(),
                        detalle.getCantidad()
                    );
                } catch (ValidacionException e) {
                    System.err.println("No se pudo descontar stock: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new ValidacionException("Error al guardar el pedido: " + e.getMessage());
        }
    }

    public List<Pedido> obtenerPedidosActivos() throws ValidacionException {
        try {
            return pedidoRepository.findAllActivos();
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public Pedido buscarPorId(Long id) throws ValidacionException {
        try {
            Pedido p = pedidoRepository.findById(id);
            if (p == null) throw new ValidacionException("No se encontró ningún pedido con el ID: " + id);
            return p;
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void actualizarEstadoYPago(Long id, Estado nuevoEstado, FormaPago nuevaFormaPago) throws ValidacionException {
        buscarPorId(id);
        try {
            pedidoRepository.actualizarEstado(id, nuevoEstado, nuevaFormaPago);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void eliminarPedido(Long id) throws ValidacionException {
        buscarPorId(id);
        try {
            pedidoRepository.eliminar(id);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }
}