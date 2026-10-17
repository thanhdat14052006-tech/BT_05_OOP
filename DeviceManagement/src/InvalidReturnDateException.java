/*NGUYEN THANH DAT - 202419040 */

import java.time.LocalDate;

public class InvalidReturnDateException extends BorrowingException {
    private final String deviceId;
    private final LocalDate returnedDate;
    private final LocalDate borrowedDate;

    public InvalidReturnDateException(String deviceId, LocalDate returnedDate, LocalDate borrowedDate) {
        super("Ngày trả " + returnedDate + " của thiết bị " + deviceId
                + " không được trước ngày mượn " + borrowedDate);
        this.deviceId = deviceId;
        this.returnedDate = returnedDate;
        this.borrowedDate = borrowedDate;
    }

    public String getDeviceId() { return deviceId; }
    public LocalDate getReturnedDate() { return returnedDate; }
    public LocalDate getBorrowedDate() { return borrowedDate; }
}
