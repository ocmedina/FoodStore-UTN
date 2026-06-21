package integrado.prog2.entities;

public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido() {
        super();
    }

    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        Double precio = (producto != null && producto.getPrecio() != null) ? producto.getPrecio() : 0.0;
        this.subtotal = cantidad * precio;
    }

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        if (this.producto != null) {
            Double precio = (this.producto.getPrecio() != null) ? this.producto.getPrecio() : 0.0;
            this.subtotal = cantidad * precio;
        }
    }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Producto getProducto() { return producto; }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (this.producto != null) {
            Double precio = (this.producto.getPrecio() != null) ? this.producto.getPrecio() : 0.0;
            this.subtotal = this.cantidad * precio;
        }
    }

    @Override
    public String toString() {
        String nombreProd = (producto != null) ? producto.getNombre() : "Producto Desconocido";
        Double st = (subtotal != null) ? subtotal : 0.0;
        return cantidad + "x " + nombreProd + " | Subtotal: $" + st;
    }
}