package Readfile;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;



public class math_test {
	 public static void main(String[] argv)
	            throws java.io.IOException,
	            java.lang.InterruptedException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException,JSONException 
	    {
		 	int number = -15;
		 	number = (((number) % 360) +360)%360;
		 	System.out.println(number);
	    }
}
