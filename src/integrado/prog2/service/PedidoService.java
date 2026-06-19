package integrado.prog2.service;

import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private List<Pedido> pedidos;
    private Long contadorId;

    // Inyectamos las dependencias necesarias
    private UsuarioService usuarioService;
    private ProductoService productoService;

    public PedidoService(UsuarioService usuarioService, ProductoService productoService) {
        this.pedidos = new ArrayList<>();
        this.contadorId = 1L;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    // Método 1: Iniciar un pedido "en borrador"
    public Pedido iniciarPedido(Long idUsuario, FormaPago formaPago) throws ValidacionException {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        // Regla: No permitir crear Pedido sin usuario [cite: 181]
        if (usuario == null) {
            throw new ValidacionException("No se puede crear un pedido sin un usuario válido.");
        }

        Pedido nuevoPedido = new Pedido(Estado.PENDIENTE, formaPago, usuario);
        return nuevoPedido;
    }

    // Método 2: Agregar detalles (productos) a ese pedido borrador
    public void agregarDetalleAPedido(Pedido pedido, Long idProducto, int cantidad) throws ValidacionException {
        // Regla: Al crear DetallePedido, la cantidad debe ser > 0 [cite: 182]
        if (cantidad <= 0) {
            throw new ValidacionException("La cantidad debe ser mayor a 0.");
        }

        Producto producto = productoService.buscarPorId(idProducto);

        // Validar si hay stock suficiente antes de agregar
        if (producto.getStock() < cantidad) {
            throw new ValidacionException("Stock insuficiente para: " + producto.getNombre() + " (Disponible: " + producto.getStock() + ")");
        }

        // Usamos el método obligatorio de la clase Pedido [cite: 379]
        // Le pasamos 0.0 al subtotal porque la clase DetallePedido ya lo calcula automáticamente
        pedido.addDetallePedido(cantidad, 0.0, producto);

        // Actualizamos el stock del producto reduciéndolo
        producto.setStock(producto.getStock() - cantidad);
    }

    // Método 3: Confirmar y guardar el pedido definitivamente en la colección
    public void confirmarYGuardarPedido(Pedido pedido) throws ValidacionException {
        if (pedido.getDetalles().isEmpty()) {
            throw new ValidacionException("No se puede guardar un pedido sin productos.");
        }
        // Recién acá se asigna el ID y se guarda, previniendo datos inconsistentes si fallaba un paso previo
        pedido.setId(contadorId++);
        pedidos.add(pedido);
    }

    // HU-PED-01: Listar pedidos
    public List<Pedido> obtenerPedidosActivos() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public Pedido buscarPorId(Long id) throws ValidacionException {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new ValidacionException("No se encontró ningún pedido con el ID: " + id);
    }

    // HU-PED-03: Actualizar estado y forma de pago
    public void actualizarEstadoYPago(Long id, Estado nuevoEstado, FormaPago nuevaFormaPago) throws ValidacionException {
        Pedido pedido = buscarPorId(id);
        if (nuevoEstado != null) {
            pedido.setEstado(nuevoEstado);
        }
        if (nuevaFormaPago != null) {
            pedido.setFormaPago(nuevaFormaPago);
        }
    }

    // HU-PED-04: Eliminar pedido (baja lógica)
    public void eliminarPedido(Long id) throws ValidacionException {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);
    }
}