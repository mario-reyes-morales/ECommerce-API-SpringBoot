package upm.app.data.models.exceptions;

public class InvalidAttributeException extends RuntimeException {
    private static final String DESCRIPTION = "Invalid Attribute Exception. The attribute value is out of range.";

    public InvalidAttributeException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}