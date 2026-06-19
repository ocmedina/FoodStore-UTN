package integrado.prog2.exception;

// Heredamos de Exception para obligar al menú a usar Try-Catch
public class ValidacionException extends Exception {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}