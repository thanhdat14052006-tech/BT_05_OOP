/*NGUYEN THANH DAT - 202419040 */

public class DeviceAlreadyReturnedException extends BorrowingException {
    private final String deviceId;

    public DeviceAlreadyReturnedException(String deviceId) {
        super("Thiết bị " + deviceId + " đã được trả trước đó.");
        this.deviceId = deviceId;
    }

    public String getDeviceId() { return deviceId; }
}
