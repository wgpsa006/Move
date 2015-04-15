package demo;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import org.json.*;

public class SP {


    private static final String TEST_TOPIC = "demo.rssi";
    private static final String WISE_TOPIC = "demo.EXCHANGE1";
    
    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException,JSONException 
    {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@140.119.163.199");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        // receive 
        channel.exchangeDeclare(TEST_TOPIC, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, TEST_TOPIC, "");
        
        // sent Channel create 
        Channel channel_PUSH = connection.createChannel();
        channel_PUSH.exchangeDeclare(WISE_TOPIC, "fanout");
        
        
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        
        
        String [] receive_message;
        String oper;

        String receive_messageX;
        String receive_messageY;
        String receive_messageZ;
        Double last_receive_X = 0.0;
        Double A=0.9;
        Double receive_X;
        Double receive_Y;
        Double receive_Z;

        
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            System.out.println(" [x] Received '" + message + "'");

            JSONObject jsonObjectfromRQ = new JSONObject(message);

            try {
            	Object jsonObjectPosition = null ;

            	
		            jsonObjectPosition = jsonObjectfromRQ.get("Position");
		            // get from rssi  
		            receive_messageX = String.valueOf(((JSONObject) jsonObjectPosition).get("x"));
		            receive_messageY = String.valueOf(((JSONObject) jsonObjectPosition).get("y"));
		            receive_messageZ = String.valueOf(((JSONObject) jsonObjectPosition).get("z"));
		            //receive_Z = (String)((JSONObject) jsonObjectPosition).get("z");
		            receive_X = Double.parseDouble(receive_messageX);
		            receive_Y = Double.parseDouble(receive_messageY);
		            receive_Z = Double.parseDouble(receive_messageZ);
		            
		            A = (Double)((JSONObject) jsonObjectPosition).get("A");
		            //receive_X = receive_X +0.001;
		            
		            // LAST POINT + UPDATE 
		            last_receive_X = last_receive_X*A + receive_X*(1-A);
		            //利用map的對照來產生JSONObject --> ASK 
		            //oper = "{ Position: { " + "x:" + receive_X.toString() + ", y:"+receive_Y.toString() + ", z:"+ receive_Z.toString()+ "} }";
		            
		            oper = "{ Position: { " + "x:" + last_receive_X.toString() + ", y:"+receive_Y.toString() + ", z:"+ ((JSONObject) jsonObjectPosition).get("z")+ "} }";
		            System.out.println(" [x] oper '" + oper + "'");
		            //channel.basicPublish(TEST_TOPIC, "", null, oper.getBytes());
		            channel_PUSH.basicPublish(WISE_TOPIC, "", null, oper.getBytes());

            	} catch (JSONException e) { 
            		System.out.println(" Json error'");
                return ;
            }
        }
    }
}
