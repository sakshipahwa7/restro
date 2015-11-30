package com.near.restro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LaunchActivity extends Activity{
	
	public ListView lv_nearest_restro_list;
	Context con;
	@Override
	public void onCreate(Bundle savedBundleInstance){
		super.onCreate(savedBundleInstance);
		
		setContentView(R.layout.activity);
		con = getApplicationContext();
		MyLocation ml = new MyLocation();
		ml.onCreate(con);
		lv_nearest_restro_list = (ListView) findViewById(R.id.nearest_restro_list);
		lv_nearest_restro_list.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
//			    Toast.makeText(getApplicationContext(),
//			      "Click ListItem Number " + position, Toast.LENGTH_LONG)
//			      .show();
			  }
			});
		
		FetchJSON fj = new FetchJSON();
		fj.execute("");
		
	}
	
	
	class FetchJSON extends AsyncTask<String, Void, String>{
		
		@Override
		public void onPreExecute(){
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String data = "";
			 InputStream iStream = null;
	        HttpURLConnection urlConnection = null;
	        try{
	            URL url =  new URL("http://staging.couponapitest.com/task.txt");	
	            urlConnection = (HttpURLConnection) url.openConnection();
	            urlConnection.connect();
	            iStream = urlConnection.getInputStream();
	            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
	            StringBuffer sb  = new StringBuffer();
	            String line = "";
	            while( ( line = br.readLine())  != null){
	                sb.append(line);
	            }
	            data = sb.toString();
	            br.close();
	        }catch(Exception e){
	            Log.d("Exception while downloading url", e.toString());
	        }finally{
	            try {
					iStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            urlConnection.disconnect();
	        }
	        return data;
			
		}
		@Override
		public void onPostExecute(String result){
			super.onPostExecute(result);
			
			JSONParsing jsonParsing = new JSONParsing();
			boolean st = jsonParsing.parseTheJSON(result,con);
			if(st){
				HashMap<String,Double> disInfo = jsonParsing.getDisInfo();
				Map<String, Double> sortedMap = sortMap(disInfo);
				ListModel[] listItem = new ListModel[disInfo.size()];
				int k=0;
				for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
//					System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
					HashMap<String, String> restraunt = jsonParsing.getRestroOb(entry.getKey());
					JSONArray json_catArray;
					String categories = "" ;
					try {
						json_catArray = new JSONArray(restraunt.get("Categories"));
						for(int c=0; c<json_catArray.length();c++){
							categories += "&#8226;  "+json_catArray.getJSONObject(c).getString("Name")+" &nbsp;&nbsp;";
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					listItem[k] = new ListModel(restraunt.get("LogoURL"), restraunt.get("OutletName"), restraunt.get("NumCoupons"),
							categories,restraunt.get("NeighbourhoodName")
							);
					k++;
				}
				lv_nearest_restro_list = (ListView) findViewById(R.id.nearest_restro_list);
			    Listadapter lAdapter = new Listadapter(LaunchActivity.this, R.layout.list, listItem);
				lv_nearest_restro_list.setAdapter(lAdapter);
			}

		}
		private  Map<String, Double> sortMap(Map<String, Double> unsortMap) {

			List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
				public int compare(Map.Entry<String, Double> o1,Map.Entry<String, Double> o2) {
					return (o1.getValue()).compareTo(o2.getValue());
				}
			});
			Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
			for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
				Map.Entry<String, Double> entry = it.next();
				sortedMap.put(entry.getKey(), entry.getValue());
			}
			return sortedMap;
		}
	}
	
}
