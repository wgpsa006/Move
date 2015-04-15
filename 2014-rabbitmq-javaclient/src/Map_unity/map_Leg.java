package Map_unity;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import Readfile.ReadCVS;
 
public class map_Leg {
	private static final String TEST_TOPIC = "demo.EXCHANGE1";
	//private static final String TEST_TOPIC = "demo.rssi";
	
    public static void main(String[] argv)
            throws java.io.IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@140.119.163.199");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TEST_TOPIC, "fanout");
        //C:\Users\mclab\Desktop\wearable\4-1\LeftUpLeg
        
		String csvFile = "/Users/mclab/Desktop/wearable/4-1/RightUpLeg/RightUpLeg_2.csv";
		//String csvFile = "/Users/mclab/Documents/MATLAB/MotionAnaUI/LeftUpLeg.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int count=0;
		String message = null;
		
		double receive_X;
        double receive_Y;
		//Integer receive_Y=0;
        double receive_Z;
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				count = count + 1;
				if(count % 2 == 1 && count< 1034 && count> 2)
				if(count>1)
				{
				      // use comma as separator
					String[] data = line.split(cvsSplitBy);
					try{
						System.out.println("Country [x= " + count+ " , y=" + data[15]  + " , z=" + data[17] + "]");
					receive_X =  Double.parseDouble(data[15]);
					//receive_Y =  Double.parseDouble(data[16]);
					receive_Z =  Double.parseDouble(data[17]);
					
					receive_X = (receive_X +180)*-1;
					//receive_Y = receive_Y +20;
					receive_Z = receive_Z -20;
					
					data[0] =  String.valueOf(receive_X);
					//data[1] =  String.valueOf(receive_Y);
					data[2] =  String.valueOf(receive_Z);
					
					message = "{ Position : { x:"+ receive_X +", y: 0" +", z: " + receive_Z + " } }";				
					//message = "{ LeftUpLeg : { x:"+ data[6] +", y:" + data[7] +", z:"+ data[8] +" } }";
					//message = "{ RightUpLeg : { x:"+ data[0] +", y:" + data[1] +", z:"+ data[2] +" } }";
					//message = "{ RightUpLeg : { x:"+ data[0] +", y:" + data[1] +", z:"+ data[2] +" },RightLeg : { x:"+ data[17] +", y:" + data[16] +", z:"+ data[15] +" },RightFoot : { x:"+ data[20] +", y:" + data[19] +", z:"+ data[18] +" },LeftUpLeg : { x:"+ data[4] +", y:" + data[3] +", z:"+ data[2] +" },LeftLeg : { x:"+ data[7] +", y:" + data[6] +", z:"+ data[5] +" },LeftFoot : { x:"+ data[10] +", y:" + data[9] +", z:"+ data[8] +" } }";
    				channel.basicPublish(TEST_TOPIC, "", null, message.getBytes());
    				//message = "{ LeftUpLeg : { x:"+ data[4] +", y:" + data[3] +", z:"+ data[2] +" },LeftLeg : { x:"+ data[7] +", y:" + data[6] +", z:-"+ data[5] +" },LeftFoot : { x:-"+ data[10] +", y:" + data[9] +", z:"+ data[8] +" } }";
					} catch (Exception  e) {
						e.printStackTrace();
					}
    				
    				try {
    					//channel.basicPublish(TEST_TOPIC, "", null, message.getBytes());
						Thread.sleep(60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				  
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
 
	//System.out.println("Done");
  }
 
}