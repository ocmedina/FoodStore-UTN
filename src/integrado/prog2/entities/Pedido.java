package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;

    // Relación con Usuario (el dueño del pedido)
    private Usuario usuario;

    // Composición 1:N con DetallePedido (Uso de Colecciones en memoria)
    private List<DetallePedido> detalles;

    // Constructor vacío
    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    // Constructor principal
    public Pedido(Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        this.fecha = LocalDate.now();
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    // --- MÉTODOS PROPIOS EXIGIDOS POR EL UML ---

    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        DetallePedido nuevoDetalle = new DetallePedido(cantidad, producto);
        // Podemos ignorar el parámetro 'subtotal' que viene en la firma del UML
        // porque la clase DetallePedido ya lo calcula de forma segura en su constructor,
        // pero respetamos la firma del método que pide el documento.
        nuevoDetalle.setSubtotal(subtotal);
        this.detalles.add(nuevoDetalle);
        calcularTotal(); // Recalculamos el total del pedido cada vez que se agrega algo
    }

    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().getId().equals(producto.getId())) {
                return detalle;
            }
        }
        return null; // Si no lo encuentra, retorna null
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalleAEliminar = findeDetallePedidoByProducto(producto);
        if (detalleAEliminar != null) {
            detalles.remove(detalleAEliminar);
            calcularTotal(); // Recalculamos el total al eliminar un detalle
        }
    }

    // --- IMPLEMENTACIÓN DE LA INTERFAZ CALCULABLE ---

    @Override
    public void calcularTotal() {
        double sumaTotal = 0.0;
        for (DetallePedido detalle : detalles) {
            sumaTotal += detalle.getSubtotal();
        }
        this.total = sumaTotal;
    }

    // --- GETTERS Y SETTERS ---

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        String nombreUsr = (usuario != null) ? usuario.getNombre() : "Sin usuario";
        return "Pedido [ID=" + getId() + "] - " + fecha + " | Usuario: " + nombreUsr +
                " | Estado: " + estado + " | Pago: " + formaPago + " | Total: $" + total;
    }
}