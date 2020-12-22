package exceptions;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/*
caught when we have entity attributes validation annotations that can be violated when we persist or
update entities:
 */

public class MyConstraintViolationException extends Exception {
    public MyConstraintViolationException(ConstraintViolationException e) {
        super(getConstraintViolationMessages(e));
    }

    public MyConstraintViolationException(String s) {
    }

    private static String
    getConstraintViolationMessages(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();
        StringBuilder errorMessages = new StringBuilder();
        for (ConstraintViolation<?> cv : cvs) {
            errorMessages.append(cv.getMessage());
            errorMessages.append("; ");
        }
        return errorMessages.toString();
    }
}