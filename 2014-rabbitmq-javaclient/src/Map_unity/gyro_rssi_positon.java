package Map_unity;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.view.ViewDebug.FlagToString;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class gyro_rssi_positon {

//    private static final String TEST_TOPIC = "demo.rssi2";
    private static final String WISE_TOPIC = "demo.EXCHANGE1";
    
    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException,JSONException 
    {
		try {
	        ConnectionFactory factory = new ConnectionFactory();
	        //ConnectionFactory factory_rssi = new ConnectionFactory();
	        factory.setUri("amqp://admin:admin@140.119.163.199");
	        Connection connection = factory.newConnection();
	        Connection connection_rssi = factory.newConnection();
//	       
	        Channel channel = connection.createChannel();
	        Channel channel_rssi = connection_rssi.createChannel();
	        
	        // receive  from two Channel one from Gryo  the other from rssi
	        channel.exchangeDeclare(WISE_TOPIC, "fanout");
//	        channel_rssi.exchangeDeclare(TEST_TOPIC, "fanout");
	       
	        String queueName = channel.queueDeclare().getQueue();
	        String queueName_rssi = channel_rssi.queueDeclare().getQueue();
	        
	        channel.queueBind(queueName, WISE_TOPIC, "");
//	        channel_rssi.queueBind(queueName_rssi, TEST_TOPIC, "");

	        Channel channel_PUSH = connection.createChannel();
	        channel_PUSH.exchangeDeclare(WISE_TOPIC, "fanout");

	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	        // queueing_wise
	        QueueingConsumer consumer = new QueueingConsumer(channel);
	        channel.basicConsume(queueName, true, consumer);
	        
	        //System.out.println(" [*] Waiting for rssi   messages. To exit press CTRL+C");
	        // queueing_rssi	        
//	        QueueingConsumer consumer_rssi = new QueueingConsumer(channel_rssi);
//	        channel_rssi.basicConsume(queueName_rssi, true, consumer_rssi);
//	        
	        String receive_LeftUpLegX = null;
	        String receive_LeftUpLegY = null;
	        String receive_LeftUpLegZ = null;
	        
	        String receive_RightUplegX = null;
	        String receive_RightUplegY = null;
	        String receive_RightUplegZ = null;
	        String receive_PositionX = null;
	        String receive_rssiX = null;
	        
	        Double LeftUpLegX = 0.0;
	        Double LeftUpLegY= 0.0;
	        Double LeftUpLegZ= 0.0;
	        
	        Double RightUplegX= 0.0;
	        Double RightUplegY= 0.0;
	        Double RightUplegZ= 0.0;
	        int count = 0;
			
	        Double LeftUpLegX_move=0.0;
	        Double RightUpLegX_move=0.0;
	        Double LeftUpLegY_move=0.0;
	        Double RightUpLegY_move=0.0;
	        Double LeftUpLegZ_move=0.0;
	        Double RightUpLegZ_move=0.0;
	        Double maxRightUpLeg_move=0.0;
	        Double maxLeftUpLeg_move=0.0;
	        
	        int norm=10;
	        int receive_rssicheck = 0;
			double L = 1.0; 

			Double Position_X = -1.5;
			Double rssi_X=-1.5;
			Double Gyroposition = 0.0;				
			// gyro 
			Double distance=0.0;
			double rssi_w1=0.0;
			double rssi_w0=0.0;
			double Unity_range = 1.5;		// give Unity_range
			double max_step = 60;
			double Steprange = Math.abs(Math.sqrt(2*(1 - Math.cos(0*2*Math.PI/360))) - Math.sqrt(2*(1 - Math.cos(max_step*2*Math.PI/360))));			// give Steprange
			double numberofstep = 15;		// give numberofstep
			double rssi_wiegh = 0.5;		// test rssi_wiegh from rssi 準確度
			double over_rssi=0.0;			// test over_rssi if gyroposition over rssi
			double low_rssi=0.0;			// test low_rssi if gyroposition less rssi
			Double normalize_number=0.0;
			Double normalize_distance=0.0; 
			
			 // rssi
			//double degree =7;
			double degree =7.0;
			double threshold_rssi = 0.4 ;  // -1.5 ~ 1.5
			double threshold_gyro = 1.0 ;  // -180 ~ 180

			//RMQ
			JSONObject jsonObjectfromRQ;
			JSONObject jsonObjectfromRQ_rssi;
			Object jsonObjectLeftUpLeg = null ;
		    Object jsonObjectRightUpLeg = null ;
		    Object jsonObjectPosition = null ;
		    Object jsonObjectPosition_rssi = null ;
	        
	        while (true) {
	        		
	        		// use consumer to get value 
	            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	            String message = new String(delivery.getBody());
	            System.out.println(" [x] Received '" + message + "'");
	            jsonObjectfromRQ = new BBjsonObjectfromRQ(message);

				// write file
//				File file = new File("/Users/teinakayuu/Desktop/gyro_move/test.txt");
//				FileWriter fw = new FileWriter(file,true);    // true is for append
//				BufferedWriter bw = new BufferedWriter(fw);
				
	            
	            // 計算執行幾次
	            count = count +1;
	            receive_rssicheck = 0;
	            System.out.println(" [x] count '" + count + "'");
	            try {
		            	// 抓取wise 資料 
		            	jsonObjectLeftUpLeg = jsonObjectfromRQ.get("LeftUpLeg");
		            	jsonObjectRightUpLeg = jsonObjectfromRQ.get("RightUpLeg");
		            // 抓取 rssi 資料
		            	jsonObjectPosition = jsonObjectfromRQ.get("Position");
		            	jsonObjectPosition_rssi = jsonObjectfromRQ.get("RSSI");
		            	
		            	// LeftUpLeg
		            	if (jsonObjectLeftUpLeg==null){
		            	} else {
			            		receive_LeftUpLegX = String.valueOf(((JSONObject) jsonObjectLeftUpLeg).get("x"));
			            		receive_LeftUpLegY = String.valueOf(((JSONObject) jsonObjectLeftUpLeg).get("y"));
			            		receive_LeftUpLegZ = String.valueOf(((JSONObject) jsonObjectLeftUpLeg).get("z"));
			            		if(count>1)				            //  left move  前一筆資料與現在資料筆  
			            		{
						            LeftUpLegX_move = Double.parseDouble(receive_LeftUpLegX) - LeftUpLegX; 
			            				LeftUpLegY_move = Double.parseDouble(receive_LeftUpLegY) - LeftUpLegY;
			            				LeftUpLegZ_move = Double.parseDouble(receive_LeftUpLegZ) - LeftUpLegZ;
						            
			            				maxLeftUpLeg_move =  Math.max(Math.abs(LeftUpLegX_move), Math.abs(LeftUpLegZ_move));
			            				//System.out.println(" [x] receive_LeftUpLegX  '" + receive_LeftUpLegX  + "LeftUpLegX : " + LeftUpLegX+ "LeftUpLegX_move: " + LeftUpLegX_move);
				            	}
		            	}
		            	//  RightUpLeg
		            	if (jsonObjectRightUpLeg==null){
		            	} else {
				            receive_RightUplegX = String.valueOf(((JSONObject) jsonObjectRightUpLeg).get("x"));
				            receive_RightUplegY = String.valueOf(((JSONObject) jsonObjectRightUpLeg).get("y"));
				            receive_RightUplegZ = String.valueOf(((JSONObject) jsonObjectRightUpLeg).get("z"));
				            if(count>1)			//  right move  前一筆資料與現在資料筆
			            		{
				            			RightUpLegX_move = Double.parseDouble(receive_RightUplegX) - RightUplegX; 
				            			RightUpLegY_move = Double.parseDouble(receive_RightUplegY) - RightUplegY;
							        RightUpLegZ_move = Double.parseDouble(receive_RightUplegZ) - RightUplegZ;
							        maxRightUpLeg_move =  Math.max(Math.abs(RightUpLegZ_move),Math.max(Math.abs(RightUpLegX_move), Math.abs(RightUpLegY_move)));
							        //System.out.println(" [x] receive_RightUplegX  '" + receive_RightUplegX  + "RightUplegX : " + RightUplegX+ "maxRightUpLeg_move: " + maxRightUpLeg_move);
				            	}
		            	}
		            	
		            	// get current Position 
		            	if (jsonObjectPosition==null){
		            	} else {
		            			receive_PositionX = String.valueOf(((JSONObject) jsonObjectPosition).get("x"));
		            	}
		            	
		            	// get rssi Position 
		            	if (jsonObjectPosition_rssi==null){
		            	} else {
		            		receive_rssicheck = 1;
		            		receive_rssiX = String.valueOf(((JSONObject) jsonObjectPosition_rssi).get("x"));
		            	}
		            	
		            //  left move  前一筆資料與現在資料筆  
//		            	if (receive_LeftUpLegX==null){ 	            	//LeftUpLegX_move 
//		            	} else if(count>1){
//				            LeftUpLegX_move = Double.parseDouble(receive_LeftUpLegX) - LeftUpLegX; 
//				            System.out.println(" [x] receive_LeftUpLegX  '" + receive_LeftUpLegX  + "LeftUpLegX : " + LeftUpLegX+ "LeftUpLegX_move: " + LeftUpLegX_move);
//		            	}
//		            	if (receive_LeftUpLegY==null){ 	            	//LeftUpLegX_move 
//		            	} else if(count>1) {
//				            LeftUpLegY_move = Double.parseDouble(receive_LeftUpLegY) - LeftUpLegY; 
//		            	}
//		            	if (receive_LeftUpLegZ==null){ 	            	//LeftUpLegX_move 
//		            	} else if(count>1){
//				            LeftUpLegZ_move = Double.parseDouble(receive_LeftUpLegZ) - LeftUpLegZ; 
//		            	}
//		            	
		              	
//		            	if (receive_RightUplegX==null){ 				//RightUpLegX_move
//		            	} else if(count>1){
//				            RightUpLegX_move = Double.parseDouble(receive_RightUplegX) - RightUplegX;
//				            System.out.println(" [x] receive_RightUplegX  '" + receive_RightUplegX  + "RightUplegX" + RightUplegX + "RightUpLegX_move" + RightUpLegX_move);
//		            	}
//		            	if (receive_RightUplegY==null){ 				//RightUpLegY_move
//		            	} else if(count>1){
//				            RightUpLegY_move = Double.parseDouble(receive_RightUplegY) - RightUplegY; 
//		            	}
//		            	if (receive_RightUplegZ==null){ 				//RightUpLegZ_move
//		            	} else if(count>1){
//				            RightUpLegZ_move = Double.parseDouble(receive_RightUplegZ) - RightUplegZ; 
//		            	}
		            	//maxLeftUpLeg_move =  Math.max(Math.abs(LeftUpLegZ_move),Math.max(Math.abs(LeftUpLegX_move), Math.abs(LeftUpLegY_move)));
						
		            	// 轉成double
		            	if (receive_LeftUpLegX==null){
		            	} else {
				            LeftUpLegX = Double.parseDouble(receive_LeftUpLegX);
		            	}
		            	if (receive_LeftUpLegY==null){
		            	} else {
				            LeftUpLegY = Double.parseDouble(receive_LeftUpLegY);
		            	}
		            	if (receive_LeftUpLegZ==null){
		            	} else {
				            LeftUpLegZ = Double.parseDouble(receive_LeftUpLegZ);
		            	}
		            	
		            	if (receive_RightUplegX==null){
		            	} else {
				            RightUplegX = Double.parseDouble(receive_RightUplegX);
		            	}
		            	if (receive_RightUplegY==null){
		            	} else {
				            RightUplegY = Double.parseDouble(receive_RightUplegY);
		            	}
		            	if (receive_RightUplegZ==null){
		            	} else {
				            RightUplegZ = Double.parseDouble(receive_RightUplegZ);
		            	}
	
		            	if (receive_PositionX==null){
		            	} else {
				            Position_X = Double.parseDouble(receive_PositionX);
				            System.out.println(" [x] receive_PositionX------------------ '" + Position_X + "'");
		            	}
		            	
		            	if (receive_rssiX==null){
		            	} else {
		            		rssi_X = Double.parseDouble(receive_rssiX);
		            	}

					// way depends on y
		            // pattern
		            //turn left  RightUplegY = 180~360  LeftUpLegY = 0~180
		            if ( 180 < ((RightUplegY%360)+360)%360 && ((RightUplegY%360)+360)%360 < 360 && 0 < ((LeftUpLegY%360)+360)%360 && ((LeftUpLegY%360)+360)%360 < 180)
					{
			            	// 跟前一次資料比 1筆增加 另1比減少 表示往前 
			            	//RightUplegX 往前 累加  LeftUplegX 往前遞減  
			            	//RightUpLegX_move 往前  LeftUplegX 往後
			            	if(RightUpLegX_move > 0 && LeftUpLegX_move > 0)
			            	{
			            		System.out.println("------------------------------------turn left");
			            		//bw.append("------------------------------------turn left");
			            		distance =  L * Math.sqrt(2*(1 - Math.cos(RightUpLegX_move*Math.PI/360)));
			            		//distance = distance / 2;
			            		
			    				// ******************************* test Steprange = default cos60 = 0.5 
			            		// give Unity_range = default -1.5~1.5 = 3
			            		normalize_number = 2*Unity_range/(L*Steprange); 
			            		
			            		// normalize_distance adjust to range
			            		normalize_distance = distance * normalize_number;
			            		normalize_distance = normalize_distance / numberofstep;

			    				// Gyroposition 
			    				Gyroposition = Position_X + normalize_distance;
//			    				System.out.println("------------------------------------turn Gyroposition'" + Gyroposition + "'");
//			    				System.out.println("------------------------------------turn Position_X'" + Position_X + "'");
//			    				System.out.println("------------------------------------turn normalize_distance'" + normalize_distance + "'");
//			    				
			    				//rssi 的Position 跟目前算出的 Position_X.toString() 相互比較
			    				//  gryo 有動下 由rssi判斷有無動作
			    				//  RightUpLegX_move 當成權重
			    				// 從目前的Position_X +  （RightUpLegX_move 的 Gyroposition 與 rssi 絕對相差）
			    				//   
//			    				if(Gyroposition - rssi_X > 0)
//			    				{
//			    					over_rssi = 0.2;
//			    					Position_X = Position_X + normalize_distance*over_rssi;
//			    					L = L *0.5;
//			    				}
//			    				else 
//			    				{
//			    					low_rssi = 0.8;
//			    					Position_X = Position_X + normalize_distance*low_rssi;	
//			    					L = L *1.5;
//			    				}
			    				
			    				// position = last position + distance;
			    				Position_X = Gyroposition;
				            	 if(Position_X > -1.5 && Position_X < 1.5)
				    				{
				            		 System.out.println("------------------------------------turn Position_X'" + Position_X + "'");
				    					message = "{ Position : { x:"+ Position_X.toString() +", y:" + "0" +", z:"+ "0" +" } }";
										channel.basicPublish(WISE_TOPIC, "", null, message.getBytes());
				    				}//Thread.sleep(10);
				            	 	else if (Position_X > 1.6)
				    				{
				    					Position_X = 1.5;
				    				}
				            	 	else if (Position_X < -1.6)
				    				{
				    					Position_X = -1.5;
				    				}
			            	}
			            	
			            	//RightUpLegX_move 往後  LeftUplegX 往前
			            	else if (RightUpLegX_move < 0 && LeftUpLegX_move < 0)
			            	{
			            		System.out.println("------------------------------------turn left2");
			            		//bw.append("------------------------------------turn left2");
			            		distance =  L * Math.sqrt(2*(1 - Math.cos(LeftUpLegX_move*Math.PI/360)));
			            		//distance = distance / 2;
			    				//normalize_distance = (distance *normalize_number)/(Math.sqrt(2)*L);
			            		// ******************************* test Steprange
			            		
			            		normalize_number = 2*Unity_range/(L*Steprange); 
			            		
			            		// normalize_distance adjust to range
			            		// give Unity_range
			            		normalize_distance = distance * normalize_number;
			            		normalize_distance = normalize_distance / numberofstep;
			    				// position = last position + distance;
			    				Gyroposition = Position_X + normalize_distance;
			    				
//			    				System.out.println("------------------------------------turn Gyroposition'" + Gyroposition + "'");
//			    				System.out.println("------------------------------------turn Position_X'" + Position_X + "'");
//			    				System.out.println("------------------------------------turn normalize_distance'" + normalize_distance + "'");
//			    				// 上傳WISE
//			    				if(Gyroposition - rssi_X > 0)
//			    				{
//			    					over_rssi = 0.2;
//			    					Position_X = Position_X + normalize_distance*over_rssi;
//			    					L = L *0.5;
//			    				}
//			    				else 
//			    				{
//			    					low_rssi = 0.8;
//			    					Position_X = Position_X + normalize_distance*low_rssi;	
//			    					L = L *1.5;
//			    				}
			    				Position_X = Gyroposition;
			    				if(Position_X > -1.5 && Position_X < 1.5)
			    				{
			    					System.out.println("------------------------------------turn Position_X'" + Position_X + "'");
			    					message = "{ Position : { x:"+ Position_X.toString() +", y:" + "0" +", z:"+ "0" +" } }";
									channel.basicPublish(WISE_TOPIC, "", null, message.getBytes());
			    				}
			    				else if (Position_X > 1.6)  
			    				{
			    					Position_X = 1.5;
			    				}
			    				else if (Position_X < -1.6)
			    				{
			    					Position_X = -1.5;
			    				}
			            	}
					} 
					//turn right RightUplegY = 0~180  LeftUpLegY = 180~360
					if ( 0 < ((RightUplegY%360)+360)%360 && ((RightUplegY%360)+360)%360 < 180 && 180 < ((LeftUpLegY%360)+360)%360 && ((LeftUpLegY%360)+360)%360 < 360)
					{
							// 跟前一次資料比 1筆增加 另1比減少 表示往前 
			            	//RightUplegX 往前 累加  LeftUplegX 往前遞減  
			            	//RightUpLegX_move 往前  LeftUplegX 往後
			            	if(RightUpLegX_move > 0 && LeftUpLegX_move > 0)
			            	{
			            		System.out.println("------------------------------------turn right");
			            		//bw.append("------------------------------------turn right");
			            		distance =  L * Math.sqrt(2*(1 - Math.cos(RightUpLegX_move*Math.PI/360)));
			            		//  distance = distance / 2;  one step to end
			            		//  (2*range)/(2*L);  -1.5~ 1.5 = 2*1.5
			            		//  Steprange = maxStep.cos - minStep.cos  default maxStep.cos(60) ~ maxStep.cos0 0.5~0
			            		
			            		// ******************************* test Steprange
			            		normalize_number = 2*Unity_range/(L*Steprange); 
			            		
			            		// normalize_distance adjust to range
			            		// give Unity_range
			            		normalize_distance = distance * normalize_number;
			            		normalize_distance = normalize_distance / numberofstep;
			    				// position = last position - distance;
			            		
			    				Gyroposition = Position_X - normalize_distance;
			    				
			    				// ******************************* step too over
//			    				if(Gyroposition - rssi_X < 0)
//			    				{
//			    					over_rssi = 0.2;
//			    					//Position_X = Position_X - normalize_distance*over_rssi;
//			    					L = L *over_rssi;
//			    					
//			    					 // calculate user position  = Gyro + w (rssi - gyro) 
//			    					// ******************************* test rssi_wiegh
//			    					
//			    				}
//			    				else 
//			    				{
//			    					low_rssi = 0.8;
//			    					Position_X = Position_X - normalize_distance*low_rssi;	
//			    					L = L *low_rssi;
//			    				}
			    				
//			    				Position_X = Gyroposition + rssi_wiegh*(rssi_X - Gyroposition);
			    				Position_X = Gyroposition;
//			    				System.out.println("------------------------------------turn Gyroposition'" + Gyroposition + "'");
//			    				System.out.println("------------------------------------turn Position_X'" + Position_X + "'");
//			    				System.out.println("------------------------------------turn normalize_distance'" + normalize_distance + "'");
//			    				
			    				
			    				if(Position_X > -1.5 && Position_X < 1.5)
			    				{
			    					message = "{ Position : { x:"+ Position_X.toString() +", y:" + "0" +", z:"+ "0" +" } }";
									channel.basicPublish(WISE_TOPIC, "", null, message.getBytes());
			    				}
			    				else if (Position_X > 1.6)
			    				{
			    					Position_X = 1.5;
			    				}
			    				else if (Position_X < -1.6)
			    				{
			    					Position_X = -1.5;
			    				}
			    				
			            		//System.out.println("-----------------------------------------------------move");
			            		//message = "-----------------------------------------------------move";
			            	}
			            	
			            	//RightUpLegX_move 往後  LeftUplegX 往前
			            	else if (RightUpLegX_move < 0 && LeftUpLegX_move < 0)
			            	{
			            		System.out.println("------------------------------------turn right2");
			            		//bw.append("------------------------------------turn right2");
			            		distance =  L * Math.sqrt(2*(1 - Math.cos(LeftUpLegX_move*Math.PI/360)));
			            	//  distance = distance / 2;  one step to end
			            		//  (2*range)/(2*L);  -1.5~ 1.5 = 2*1.5
			            		//  Steprange = maxStep.cos - minStep.cos  default maxStep.cos(60) ~ maxStep.cos0 0.5~0
			            		
			            		// ******************************* test Steprange
			            		normalize_number = 2*Unity_range/(L*Steprange); 
			            		
			            		// normalize_distance adjust to range
			            		// give Unity_range
			            		normalize_distance = distance * normalize_number;
			            		//normalize_distance = normalize_distance / numberofstep;
			    				// position = last position - distance;
			            		
			    				Gyroposition = Position_X - normalize_distance;
			    				
			    				// ******************************* step too over
//			    				if(Gyroposition - rssi_X < 0)
//			    				{
//			    					over_rssi = 0.2;
//			    					//Position_X = Position_X - normalize_distance*over_rssi;
//			    					L = L *over_rssi;
//			    					
//			    					 // calculate user position  = Gyro + w (rssi - gyro) 
//			    					// ******************************* test rssi_wiegh
//			    					
//			    				}
//			    				else 
//			    				{
//			    					low_rssi = 0.8;
//			    					Position_X = Position_X - normalize_distance*low_rssi;	
//			    					L = L *low_rssi;
//			    				}
			    				
//			    				Position_X = Gyroposition + rssi_wiegh*(rssi_X - Gyroposition);
			    				Position_X = Gyroposition;
//			    				System.out.println("------------------------------------turn Gyroposition'" + Gyroposition + "'");
//			    				System.out.println("------------------------------------turn Position_X'" + Position_X + "'");
//			    				System.out.println("------------------------------------turn normalize_distance'" + normalize_distance + "'");
//			    				
			    				if(Position_X > -1.5 && Position_X < 1.5)
			    				{
			    					message = "{ Position : { x:"+ Position_X.toString() +", y:" + "0" +", z:"+ "0" +" } }";
									channel.basicPublish(WISE_TOPIC, "", null, message.getBytes());
			    				}
			    				else if (Position_X > 1.6)
			    				{
			    					Position_X = 1.5;
			    				}
			    				else if (Position_X < -1.6)
			    				{
			    					Position_X = -1.5;
			    				}
			            	}
					}
					else  if( receive_rssicheck == 1) // rssi 
					{
	//		            	if(Math.abs(rssi_X - Position_X) < threshold_rssi && maxLeftUpLeg_move < 10)
	//		            	{
	//		            		 // not move
	//		            	} 
			            	// Final positon = w1 new(x,y,z) + w0 old(x,y,z)
							// w1 : w0 =  max(pitch,roll,yaw)/180~0
							// if(new - old() < threshold && max(pitxh,roll,yaw)< threshold) not move
							// else  find w1` 
							//
						
						//w1 = Math.max(maxLeftUpLeg_move ,maxRightUpLeg_move)/degree;
			            	//w0 = 1 - w1
			            	
		            		// Get value can't in same point
					 	if( Math.abs(rssi_X - Position_X) < threshold_rssi && Math.max(maxLeftUpLeg_move ,maxRightUpLeg_move) < threshold_gyro)
			            	{
			            		 // not move
			            	} 
		            		
					 	// ******************************* test rssi_w1
					 	//rssi_w1 = Math.max(maxLeftUpLeg_move ,maxRightUpLeg_move)/degree;
			            	//rssi_w1 = maxLeftUpLeg_move/degree;
			            	rssi_w1 =Math.max(maxLeftUpLeg_move ,maxRightUpLeg_move)/degree;
			            	rssi_w1 = rssi_w1 + 0.5;
		    	            if(rssi_w1 > 1)
		    	            {
		    	            		rssi_w1 = 1 ;
		    	            }
		    	            
			            	rssi_w0 = Math.abs(1 - rssi_w1);
		            		
			            	System.out.println(" [x] rssi_X------------------ '" + rssi_X + "'");
		    	            System.out.println(" [x] rssi_w1------------------ '" + rssi_w1 + "'");
		    	            System.out.println(" [x] rssi_w0------------------ '" + rssi_w0 + "'");
		    	            
		            		// Final positon = w1 new(x,y,z) + w0 old(x,y,z)
		            		Position_X = (rssi_w1*rssi_X) + (rssi_w0*Position_X);
		            		System.out.println(" [x] final_Position------------------ '" + Position_X + "'");
		            		
		            		
		            		if(Position_X > -1.5 && Position_X < 1.5)
		    				{
		    					message = "{ Position : { x:"+ Position_X.toString() +", y:" + "0" +", z:"+ "0" +" } }";
								channel.basicPublish(WISE_TOPIC, "", null, message.getBytes());
		    				}
		    				else if (Position_X > 1.6)
		    				{
		    					Position_X = 1.5;
		    				}
		    				else if (Position_X < -1.6)
		    				{
		    					Position_X = -1.5;
		    				}
		            		
		            		//Position_X = rssi_X;
					}
//					// write file
//					File file = new File("/Users/mclab/Desktop/wearable/mapping/position/test.txt");
//					FileWriter fw = new FileWriter(file,true);    // true is for append
					//BufferedWriter bw = new BufferedWriter(fw);
					//message = "{ LeftUpLeg : { x: "+ LeftUpLegX +", y: "+ LeftUpLegY + ", z:"+ LeftUpLegZ + " }" }";
					//Gyroposition Position_X normalize_distance
//					 bw.newLine();
//					 bw.append("Gyroposition : " + Gyroposition.toString() + " Position_X: " + Position_X.toString() + " normalize_distance: " + normalize_distance.toString() + " distance" + distance.toString() + " RightUpLegX_move" + RightUpLegX_move.toString()+ " LeftUpLegX_move" + LeftUpLegX_move.toString());
//					 bw.newLine();
//					 bw.flush();
//					 bw.close();
	            	} catch (JSONException e) { 
	            		e.printStackTrace();
	            		System.out.println(" Json error");
	            }
	        } 
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}