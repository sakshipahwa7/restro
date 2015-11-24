package com.near.restro;


import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.location.Location;
import android.widget.Toast;

class JSONParsing {

	HashMap<String,Double> disInfo;
	Context con; 
	HashMap<String, HashMap<String, String>> restroHM = new HashMap<String, HashMap<String, String>>();
	public boolean parseTheJSON(String json,Context con){

		try {
			this.con = con;
			double myLongitude, myLatitude; 
			Location myLocation = MyLocation.returnMyLocation();
			if(myLocation != null){
				 myLongitude = myLocation.getLongitude();
				 myLatitude = myLocation.getLatitude();
				
			}else{
				Toast.makeText(con, "Sorry, we are not able to loacte your location!",Toast.LENGTH_SHORT).show();
				myLongitude = 28.444044;
				myLatitude = 77.313681;
			}
			disInfo = new HashMap<String,Double>();
			JSONObject json_Object = new JSONObject(json);
			JSONObject json_status = json_Object.getJSONObject("status");
			int int_status = Integer.parseInt(json_status.getString("rcode"));
			String msg = json_status.getString("message");
			if(int_status == 200 && msg.equalsIgnoreCase("OK")){
				JSONArray json_Array = json_Object.getJSONArray("data");
				for(int i=0; i<json_Array.length();i++){
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("OutletID",json_Array.getJSONObject(i).getString("OutletID"));
					hm.put("OutletName",json_Array.getJSONObject(i).getString("OutletName"));
					hm.put("BrandID",json_Array.getJSONObject(i).getString("BrandID"));
					hm.put("Address",json_Array.getJSONObject(i).getString("Address"));
					hm.put("Latitude",json_Array.getJSONObject(i).getString("Latitude"));
					hm.put("Longitude",json_Array.getJSONObject(i).getString("Longitude"));
					double longitude = Double.parseDouble(json_Array.getJSONObject(i).getString("Longitude"));
					double latitude = Double.parseDouble(json_Array.getJSONObject(i).getString("Latitude"));
					double dis = Distance.distance(myLatitude, myLongitude, latitude, longitude, "M");
					disInfo.put(json_Array.getJSONObject(i).getString("OutletID"),dis);
					hm.put("OutletURL",json_Array.getJSONObject(i).getString("OutletURL"));
					hm.put("NumCoupons",json_Array.getJSONObject(i).getString("NumCoupons"));
					hm.put("NeighbourhoodName",json_Array.getJSONObject(i).getString("NeighbourhoodName"));
					hm.put("Categories", json_Array.getJSONObject(i).getJSONArray("Categories").toString());
					hm.put("LogoURL",json_Array.getJSONObject(i).getString("LogoURL"));
					hm.put("CoverURL",json_Array.getJSONObject(i).getString("CoverURL"));
					saveRestroOb(hm);
				}
				return true;
				
				
			}else{
				Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
				return false;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	public HashMap<String,Double> getDisInfo(){
		return disInfo;
	}
	public void saveRestroOb(HashMap<String, String> hm){
		restroHM.put(hm.get("OutletID"),hm);
	}
	public HashMap<String, String> getRestroOb(String outletid){
		HashMap<String, String> resMap = new HashMap<String, String>();
		Iterator<String> iter = restroHM.keySet().iterator();
		while (iter.hasNext()) {
            Object key = iter.next();
            if (key.equals(outletid))
            {
            	resMap = restroHM.get(key);
            	break;
            }
		}
		return resMap;
	}
}