package demo;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Publisher
{
	 private static final String TEST_TOPIC = "demo.EXCHANGE1";
    //private static final String TEST_TOPIC = "demo.rssi";

    public static void main(String[] argv)
            throws java.io.IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, InterruptedException
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@140.119.163.199");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TEST_TOPIC, "fanout");
        
        Double X = 0.0;
        double Y = 0.0;
        double Z = 0.0;
        String message = null;
        // LeftUpLeg LeftLeg LeftFoot LeftToeBase LeftToes_End RightUpLeg RightLeg RightFoot RightToeBase RightToes_End
        //String message = "{ Head : { x:0.1, y:0, z:0 },Position: { x:0.001, y:0.2, z:0.2 } }";
        int i = 0;
        while (i < 3)
        {
        	//X = -i/10.00;
        	Y=90;
        	//Z= -10;
        	//Z=i/10.00;
        	//message = "{ LeftUpLeg : { x: "+ X +", y: "+ Y  + ", z:"+ Z + ", A:"+ 0.9 + " } }";
        	//message = "{ LeftUpLeg : { x: "+ X +", y: "+ Y  + ", z:"+ Z + " } }";
        	//message = "{ LeftUpLeg : { x: 0"+ X +", y: "+ Y  + ", z:"+ Z + " }, RightUpLeg : {  x: "+ X +", y: "+ Y  + ", z:"+ Z + " }}";
        	//message = "{ Lowpass : { A: 0.8 } }";
        	message = "{ Position : { x: 1.5" + ", y: " + 0 +", z:"+ 0 +" } }";
        	//Position
        	channel.basicPublish(TEST_TOPIC, "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            i++;
            Thread.sleep(10);
        }
        channel.close();
        connection.close();
    }

}
