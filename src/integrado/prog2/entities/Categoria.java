package integrado.prog2.entities;

public class Categoria extends Base {
    private String nombre;
    private String descripcion;

    // Constructor vacío
    public Categoria() {
        super(); // Llama al constructor de la clase Base (asigna eliminado = false y la fecha)
    }

    // Constructor con parámetros
    public Categoria(String nombre, String descripcion) {
        super(); // Siempre llamamos a super() primero
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // El método toString() es clave para que cuando listemos las categorías
    // en la consola se lea bien y no salga el espacio en memoria.
    @Override
    public String toString() {
        return "Categoría [ID=" + getId() + "] - " + nombre + ": " + descripcion;
    }
}