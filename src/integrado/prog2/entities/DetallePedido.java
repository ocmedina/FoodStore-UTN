package integrado.prog2.entities;

public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;

    // Relación N:1 con Producto
    private Producto producto;

    // Constructor vacío
    public DetallePedido() {
        super();
    }

    // Constructor con parámetros
    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        // El subtotal se calcula automáticamente multiplicando la cantidad por el precio
        this.subtotal = cantidad * producto.getPrecio();
    }

    // Getters y Setters
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        // Si cambia la cantidad, actualizamos el subtotal
        if (this.producto != null) {
            this.subtotal = cantidad * this.producto.getPrecio();
        }
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        String nombreProd = (producto != null) ? producto.getNombre() : "Producto Desconocido";
        return cantidad + "x " + nombreProd + " | Subtotal: $" + subtotal;
    }
}