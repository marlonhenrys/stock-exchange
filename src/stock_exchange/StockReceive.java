package stock_exchange;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import com.rabbitmq.client.*;

import storage.OfferBook;

public class StockReceive extends Thread {

    private String host;

    public StockReceive(String host) {
        this.host = host;
    }

    @Override
    public void run() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare("BROKER", BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "BROKER", "#");

            JLabel openMsgL = new JLabel("Negociações abertas...");
            openMsgL.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            StockGUI.actionsP.add(openMsgL);
            StockGUI.actionsP.revalidate();
            StockGUI.actionsP.repaint();

            DeliverCallback deliveryCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                String topic = delivery.getEnvelope().getRoutingKey();

                String[] splitTopic = topic.split("\\.");
                String[] splitMessage = message.split(">|;|<");

                String messageFormat;

                if (splitMessage.length > 5) {
                    messageFormat = splitTopic[0] + " " + splitTopic[1].toUpperCase() + " | data: " + splitMessage[1]
                            + ", crr_vd: " + splitMessage[2] + ", crr_cp: " + splitMessage[3] + ", qtde: "
                            + splitMessage[4] + ", val: " + splitMessage[5];
                } else {
                    messageFormat = splitTopic[0] + " " + splitTopic[1].toUpperCase() + " | qtde: " + splitMessage[1]
                            + ", val: " + splitMessage[2] + ", crr: " + splitMessage[3];
                }

                JLabel messageL = new JLabel(messageFormat);
                messageL.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                StockGUI.actionsP.add(messageL);
                StockGUI.actionsP.revalidate();
                StockGUI.actionsP.repaint();

                StockEmit sEmit = new StockEmit(host, topic, message);
                sEmit.start();

                OfferBook.store(host, topic, message);
            };

            channel.basicConsume(queueName, true, deliveryCallback, consumerTag -> { });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}