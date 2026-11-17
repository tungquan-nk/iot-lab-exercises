import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;

public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main (String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            SensorData data = new SensorData("Producer-1", 30.0, 60.0);
            Gson jsonData = new Gson();
            String message = jsonData.toJson(data);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sent: " + message);
        }
    }
}
