/*NGUYEN THANH DAT - 202419040 */

public class DuplicateDeviceException extends BorrowingException {
    private final String deviceId;

    public DuplicateDeviceException(String deviceId) {
        super("Thiết bị " + deviceId + " đã có trong phiếu mượn.");
        this.deviceId = deviceId;
    }

    public String getDeviceId() { return deviceId; }
}
