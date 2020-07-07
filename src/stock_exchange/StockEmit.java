package stock_exchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class StockEmit extends Thread {

    private String host;
    private String topic;
    private String message;

    public StockEmit(String host, String topic, String message) {
        this.host = host;
        this.topic = topic;
        this.message = message;
    }

    @Override
    public void run() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare("BOLSADEVALORES", BuiltinExchangeType.TOPIC);

            channel.basicPublish("BOLSADEVALORES", topic, null, message.getBytes("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}