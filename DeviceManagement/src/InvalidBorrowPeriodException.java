/*NGUYEN THANH DAT - 202419040 */

public class InvalidBorrowPeriodException extends BorrowingException {
    private final String deviceId;
    private final int requested;
    private final int maximum;

    public InvalidBorrowPeriodException(String message, String deviceId, int requested, int maximum) {
        super(message);
        this.deviceId = deviceId;
        this.requested = requested;
        this.maximum = maximum;
    }

    public String getDeviceId() { return deviceId; }
    public int getRequested() { return requested; }
    public int getMaximum() { return maximum; }
}
