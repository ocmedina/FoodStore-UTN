package integrado.prog2.entities;

public interface Calculable {
    // Las interfaces solo definen "qué" se debe hacer, no "cómo".
    // La lógica matemática la vamos a escribir adentro de Pedido.
    void calcularTotal();
}