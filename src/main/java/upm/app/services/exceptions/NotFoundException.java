package upm.app.services.exceptions;

public class NotFoundException extends RuntimeException {
    private static final String DESCRIPTION = "Objeto no encontrado";

    public NotFoundException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}