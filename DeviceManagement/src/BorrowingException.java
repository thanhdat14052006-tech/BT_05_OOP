public class BorrowingException extends Exception {
    public BorrowingException(String message) {
        super(message);
    }

    public BorrowingException(String message, Exception innerException) {
        super(message, innerException);
    }
}
