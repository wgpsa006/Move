package Map_unity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Push_text_to_RQ
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
        
        // LeftUpLeg LeftLeg LeftFoot LeftToeBase LeftToes_End RightUpLeg RightLeg RightFoot RightToeBase RightToes_End
        //String message = "{ Head : { x:0.1, y:0, z:0 },Position: { x:0.001, y:0.2, z:0.2 } }";  
        BufferedReader br = new BufferedReader(new FileReader("/Users/teinakayuu/Dropbox/國科會/穿戴計劃/testdata/test.txt"));
        try{														
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String nullString1 = null;
            while ((line = br.readLine()) != null) {
            	
            		sb.append(line);
                //sb.append(System.lineSeparator());
                //line = br.readLine();
                //System.out.println(" [x] Sent '" + line + "'");
 
            		Thread.sleep(20);
            		channel.basicPublish(TEST_TOPIC, "", null, line.getBytes());
        
            }
        }
        finally {
        	   br.close();
        	}
        channel.close();
        connection.close();
    }

}
