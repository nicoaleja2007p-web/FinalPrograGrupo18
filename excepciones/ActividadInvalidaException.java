package excepciones;

public class ActividadInvalidaException extends Exception {

    public ActividadInvalidaException(String mensaje) {
        super(mensaje);
    }
}