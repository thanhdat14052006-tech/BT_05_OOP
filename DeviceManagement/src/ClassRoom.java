/*NGUYEN THANH DAT - 202419040 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassRoom {
    private final String roomId;
    private String roomName;
    private int capacity;
    private final List<Device> devices = new ArrayList<>();

    public ClassRoom(String roomId, String roomName, int capacity) {
        if (roomId == null || roomId.isBlank()) throw new IllegalArgumentException("Mã phòng không được rỗng.");
        if (roomName == null || roomName.isBlank()) throw new IllegalArgumentException("Tên phòng không được rỗng.");
        if (capacity <= 0) throw new IllegalArgumentException("Sức chứa phải lớn hơn 0.");
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public String getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public int getCapacity() { return capacity; }
    public List<Device> getDevices() { return List.copyOf(devices); }

    public void addDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Thiết bị không được null.");
        }
        if (findDevice(device.getDeviceId()).isPresent()) {
            throw new IllegalArgumentException("Phòng đã có thiết bị mã " + device.getDeviceId());
        }
        devices.add(device);
    }

    public boolean removeDevice(String deviceId) {
        return devices.removeIf(d -> d.getDeviceId().equals(deviceId));
    }

    public Optional<Device> findDevice(String deviceId) {
        return devices.stream().filter(d -> d.getDeviceId().equals(deviceId)).findFirst();
    }

    public BigDecimal calculateAnnualMaintenanceCost() {
        BigDecimal total = BigDecimal.ZERO;
        for (Device d : devices) {
            total = total.add(d.calculateAnnualMaintenanceCost());
        }
        return total;
    }

    public List<Device> getDevicesRequiringMaintenance() {
        List<Device> result = new ArrayList<>();
        for (Device d : devices) {
            if (d.getStatus() == DeviceStatus.UNDER_MAINTENANCE || d.getAge() > 5) {
                result.add(d);
            }
        }
        return result;
    }
}
