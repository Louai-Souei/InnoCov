package covoiturage.project.InnoCov.exceptions;

public class UserException extends Exception {
    public UserException() { super("An error has occured, please try again or contact your Administrator");}
    public UserException(String message) { super(message);}
}
