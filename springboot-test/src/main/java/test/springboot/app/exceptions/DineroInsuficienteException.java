package test.springboot.app.exceptions;

public class DineroInsuficienteException extends RuntimeException {

    public DineroInsuficienteException(String s) {
        super(s);
    }
}
