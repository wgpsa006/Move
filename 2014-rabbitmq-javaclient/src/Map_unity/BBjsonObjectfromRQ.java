package Map_unity;

import org.json.JSONObject;

public class BBjsonObjectfromRQ extends JSONObject {
	private String msg;
	
	public BBjsonObjectfromRQ(String msg) throws Exception {
		super(msg);
	}
	
	public Object get(String str){
		Object bb = null;
		try {
			bb = super.get(str);
		} catch (Exception e) {
//			e.printStackTrace();
    		System.out.println(" Json error GOT");
		}
		return bb;
	}
}
