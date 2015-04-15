package Readfile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter; 


public class ReadCVS {
 
  public static void main(String[] args) {
 
	ReadCVS obj = new ReadCVS();
	obj.run();

  }
 
  public void run() {
	  //C:\Users\mclab\Desktop\wearable\4-1\LeftUpLeg
	  String csvFile = "/Users/mclab/Desktop/wearable/location/4_4_1.csv";
	//String csvFile = "/Users/mclab/Documents/MATLAB/MotionAnaUI/LeftUpLeg.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	int count=0;
	try {
		br = new BufferedReader(new FileReader(csvFile));
		
		String outputFile = "/Users/mclab/Desktop/wearable/location/users.csv";
		String outputFile2 = "/Users/mclab/Desktop/wearable/location/users_2.csv";
		String outputFile3 = "/Users/mclab/Desktop/wearable/location/users_3.csv";
		String outputFile4 = "/Users/mclab/Desktop/wearable/location/users_4.csv";
		
		
		while ((line = br.readLine()) != null) {
			String[] country = line.split(cvsSplitBy);
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			CsvWriter csvOutput2 = new CsvWriter(new FileWriter(outputFile2, true), ',');
			CsvWriter csvOutput3 = new CsvWriter(new FileWriter(outputFile3, true), ',');
			CsvWriter csvOutput4 = new CsvWriter(new FileWriter(outputFile4, true), ',');
			
			count = count + 1;
			
			if(count == 0)
			{
				String[] column = null;
				column =  country;
				csvOutput.write(column[1]);
				
			}
			else if( (count-2)%5 > 0)
			{
			        // use comma as separator
				
				String[] rssi = null;

				if((count-2)%5 == 1)
				{
					rssi =	country[2].split(" ");
					csvOutput.write(country[1]);
					csvOutput.write(rssi[2]);
					
					rssi =	country[6].split(" ");
					csvOutput.write(rssi[2]);

					rssi =	country[10].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[14].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[18].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[22].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[26].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[30].split(" ");
					csvOutput.write(rssi[2]);

					rssi =	country[34].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[38].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[42].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[46].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[50].split(" ");
					csvOutput.write(rssi[2]);
					
					rssi =	country[54].split(" ");
					csvOutput.write(rssi[2]);

				}
				else if((count-2)%5 == 2)
				{
					rssi =	country[2].split(" ");
					csvOutput2.write(country[1]);
					csvOutput2.write(rssi[2]);
					
					
					rssi =	country[6].split(" ");
					csvOutput2.write(rssi[2]);

					rssi =	country[10].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[14].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[18].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[22].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[26].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[30].split(" ");
					csvOutput2.write(rssi[2]);

					rssi =	country[34].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[38].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[42].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[46].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[50].split(" ");
					csvOutput2.write(rssi[2]);
					
					rssi =	country[54].split(" ");
					csvOutput2.write(rssi[2]);
				}
					
				else if((count-2)%5 == 3)
				{
					rssi =	country[2].split(" ");
					csvOutput3.write(country[1]);
					csvOutput3.write(rssi[2]);
					
					rssi =	country[6].split(" ");
					csvOutput3.write(rssi[2]);

					rssi =	country[10].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[14].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[18].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[22].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[26].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[30].split(" ");
					csvOutput3.write(rssi[2]);

					rssi =	country[34].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[38].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[42].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[46].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[50].split(" ");
					csvOutput3.write(rssi[2]);
					
					rssi =	country[54].split(" ");
					csvOutput3.write(rssi[2]);
				}
				else if((count-2)%5 == 4)
				{
					rssi =	country[2].split(" ");
					csvOutput4.write(country[1]);
					csvOutput4.write(rssi[2]);
					
					rssi =	country[6].split(" ");
					csvOutput4.write(rssi[2]);

					rssi =	country[10].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[14].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[18].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[22].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[26].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[30].split(" ");
					csvOutput4.write(rssi[2]);

					rssi =	country[34].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[38].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[42].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[46].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[50].split(" ");
					csvOutput4.write(rssi[2]);
					
					rssi =	country[54].split(" ");
					csvOutput4.write(rssi[2]);
					
				}
				
				
				
				csvOutput.endRecord();
				csvOutput2.endRecord();
				csvOutput3.endRecord();
				csvOutput4.endRecord();
			}
			csvOutput.close();
			csvOutput2.close();
			csvOutput3.close();
			csvOutput4.close();
			
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