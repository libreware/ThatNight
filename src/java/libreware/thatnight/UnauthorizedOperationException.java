package libreware.thatnight;

/**
 * Created by Ben on 15/05/14.
 */
public class UnauthorizedOperationException extends Exception{

    public UnauthorizedOperationException(String message) {
        super(message);
    }

    //TODO translate to user message using a newly created method
}
