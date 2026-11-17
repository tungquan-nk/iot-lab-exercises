public class SensorData {
    private String DeviceName;
    private double temperature;
    private double humidity;

    SensorData() {
    }

    public SensorData(String DeviceName, double temperature, double humidity) {
        this.DeviceName = DeviceName;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getDeviceName() {
        return DeviceName;
    };

    public double getTemperature() {
        return temperature;
    };

    public double getHumidity() {
        return humidity;
    };
}
