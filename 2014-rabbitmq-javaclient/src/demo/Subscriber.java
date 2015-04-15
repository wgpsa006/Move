package demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Subscriber
{

    private static final String TEST_TOPIC = "demo.EXCHANGE1";

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException
    {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@140.119.163.199");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TEST_TOPIC, "fanout");

        
        
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, TEST_TOPIC, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        
        File fout = new File("/Users/mclab/Documents/MATLAB/MotionAnaUI/data.txt");
    	FileOutputStream fos = new FileOutputStream(fout);
     
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
     
 
    	
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
    		bw.write(message);
    		bw.newLine();
        }
    }  
}
