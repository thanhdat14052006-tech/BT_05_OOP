/*NGUYEN THANH DAT - 202419040 */

public class DeviceNotInSlipException extends BorrowingException {
    private final String deviceId;

    public DeviceNotInSlipException(String deviceId) {
        super("Thiết bị " + deviceId + " không thuộc phiếu mượn này.");
        this.deviceId = deviceId;
    }

    public String getDeviceId() { return deviceId; }
}
