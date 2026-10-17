/*NGUYEN THANH DAT - 202419040 */

public class DeviceUnavailableException extends BorrowingException {
    private final String deviceId;
    private final DeviceStatus currentStatus;

    public DeviceUnavailableException(String deviceId, DeviceStatus currentStatus) {
        super("Thiết bị " + deviceId + " không sẵn sàng, trạng thái hiện tại: " + currentStatus);
        this.deviceId = deviceId;
        this.currentStatus = currentStatus;
    }

    public String getDeviceId() { return deviceId; }
    public DeviceStatus getCurrentStatus() { return currentStatus; }
}
