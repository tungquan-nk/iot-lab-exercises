// package mqttDemo; // Bỏ comment nếu bạn tạo package mqttDemo (src/mqttDemo/MqttExample.java)
import org.eclipse.paho.client.mqttv3.*;

public class MqttExample {
    private static final String BROKER = "tcp://broker.emqx.io:1883";
    private static final String TOPIC = "test/topic";
    private static final String CLIENT_ID = "JavaClientExample";

    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient(BROKER, CLIENT_ID, null);

            // Tạo options
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Ket noi bi mat: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String a = new String(message.getPayload());
                    System.out.println("Nhan tin nhan tu topic '" + topic + "': " + a);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Gui thanh cong!");
                }
            });

            // Kết nối đến broker
            client.connect(options);
            System.out.println("Da ket noi den Broker: " + BROKER);

            // Đăng ký nhận tin từ topic
            client.subscribe(TOPIC);
            System.out.println("Dang ky topic: " + TOPIC);

            // Gửi dữ liệu đến topic
            String content = "Xin chao Java MQTT!";
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1);
            client.publish(TOPIC, message);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
