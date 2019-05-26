package il.co.rudakov.pollingservice.error_hadler;

public class Exception409 extends RuntimeException {
    private String message;

    public Exception409() {    }

    public Exception409(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
