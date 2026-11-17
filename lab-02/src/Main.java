import org.eclipse.paho.client.mqttv3.*;
import com.google.gson.Gson;
import java.util.Scanner;

public class Main {
    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private static final String TOPIC = "tungquan/topic";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    public static class SensorData {
        private String DeviceName;
        private double temperature;
        private double humidity;
        SensorData() {}

        public SensorData(String DeviceName, double temperature, double humidity) {
            this.DeviceName = DeviceName;
            this.temperature = temperature;
            this.humidity = humidity;
        }

        public String getDeviceName() {return DeviceName;};
        public double getTemperature() {return temperature;};
        public double getHumidity() {return humidity;};
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        try {
            MqttClient client = new MqttClient(BROKER, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Ket noi bi mat: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Nhan tin nhan tu topic " + topic + ":" + "\n");
                    String payload = new String(message.getPayload());
                    try {
                        SensorData data = gson.fromJson(payload, SensorData.class);
                        System.out.println("DeviceName: " + data.getDeviceName());
                        System.out.println("Temperature: " + data.getTemperature());
                        System.out.println("Humidity: " + data.getHumidity());
                    } catch (Exception e) {
                        System.out.println("Loi parse JSON: " + e.getMessage());
                    }
                    
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Gui thanh cong!");
                }
            });

            client.connect(options);
            System.out.println("Da ket noi den Broker: " + BROKER);

            client.subscribe(TOPIC);
            System.out.println("Dang ky topic: " + TOPIC);

            SensorData data = new SensorData("my-client-id", 30.0, 60.0);
            Gson jsonData = new Gson();
            String json = jsonData.toJson(data);
            MqttMessage message = new MqttMessage(json.getBytes());
            message.setQos(1);
            client.publish(TOPIC, message);
            System.out.println("Da publish JSON: " + json);
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            System.out.print("Gui tin nhan tu MQTTBox...");

            client.disconnect();
            client.close();
            System.out.print("Da dong ket noi.");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
