package com.near.restro;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocation  implements LocationListener
{
	static Location myLocation;
	public void onCreate(Context con){

		
	    
	final LocationManager lm = (LocationManager)con.getSystemService(Context.LOCATION_SERVICE); 
	boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

    boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    if (!isGPSEnabled && !isNetworkEnabled) {
    	Toast.makeText(con, "Either there is no network provider or no GPS!",Toast.LENGTH_SHORT).show();
    } else {
    	if (isNetworkEnabled) {
    		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if (lm != null) {
            	myLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
       	 	  	Toast.makeText(con,"Location; "+myLocation, Toast.LENGTH_LONG).show();
            }
        }
        if (isGPSEnabled) {
            if (myLocation == null) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
                if (lm != null) {
                	myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               	}
            }
        }
    }
   
    
	
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
			myLocation = location;
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	public static Location returnMyLocation(){
		return myLocation;
	}
}
