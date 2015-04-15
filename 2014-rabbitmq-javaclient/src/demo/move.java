package demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import Readfile.ReadCVS;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class move
{

    private static final String TEST_TOPIC = "demo.EXCHANGE1";

    public static void main(String[] argv)
            throws java.io.IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@140.119.163.199");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TEST_TOPIC, "fanout");
        ReadCVS obj = new ReadCVS();
        String csvFile = "/Users/mclab/Documents/MATLAB/MotionAnaUI/angle_move.csv";
    	BufferedReader br = null;
    	String line = "";
    	String cvsSplitBy = ",";
    	int count=0;
    	String message = null;
    	try {
    		
    		message = "{ Position : { x:"+ "-1.5" +", y:" + "0" +", z:"+ "0" +" } }";
    		channel.basicPublish(TEST_TOPIC, "", null, message.getBytes());
    		
    		br = new BufferedReader(new FileReader(csvFile));
    		while ((line = br.readLine()) != null) {
    			count = count + 1;
    			if(count > 3)
    			{
    			        // use comma as separator
    				String[] data = line.split(cvsSplitBy);
    				//test angle
    				//P=p+D
    				
    				// default -1.5
    				message = "{ RightUpLeg : { x:"+ data[14] +", y:" + data[13] +", z:"+ data[12] +" },RightLeg : { x:"+ data[17] +", y:" + data[16] +", z:"+ data[15] +" },RightFoot : { x:"+ data[20] +", y:" + data[19] +", z:"+ data[18] +" },LeftUpLeg : { x:"+ data[4] +", y:" + data[3] +", z:"+ data[2] +" },LeftLeg : { x:"+ data[7] +", y:" + data[6] +", z:"+ data[5] +" },LeftFoot : { x:"+ data[10] +", y:" + data[9] +", z:"+ data[8] +" } }";
    				channel.basicPublish(TEST_TOPIC, "", null, message.getBytes());
    				//message = "{ LeftUpLeg : { x:"+ data[4] +", y:" + data[3] +", z:"+ data[2] +" },LeftLeg : { x:"+ data[7] +", y:" + data[6] +", z:-"+ data[5] +" },LeftFoot : { x:-"+ data[10] +", y:" + data[9] +", z:"+ data[8] +" } }";
    				message = "{ Position : { x:"+ data[41] +", y:" + "0" +", z:"+ "0" +" } }";
    				
    				try {
    					channel.basicPublish(TEST_TOPIC, "", null, message.getBytes());
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    				
    				System.out.println("Country [x= " + data[2] 
    	                                 + " , y=" + data[3]  + " , z=" + data[4] + "]");
    			}
    		}
     
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (br != null) {
    			try {
    				br.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}

        channel.close();
        connection.close();
    }

}