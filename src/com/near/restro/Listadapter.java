package com.near.restro;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class Listadapter extends ArrayAdapter<ListModel> {
	Context mContext;
	int layoutResourceId;
	ListModel data[] = null;
	public Listadapter(Context mContext, int layoutResourceId, ListModel[] data) {
		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	        if(convertView==null){
	        	LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
	        	convertView = inflater.inflate(layoutResourceId, parent, false);
	        }
	        ListModel listItem = data[position];
	        TextView outletname = (TextView) convertView.findViewById(R.id.outletname);
	        TextView outletcoupons = (TextView) convertView.findViewById(R.id.outletcoupons);
	        TextView outlet_category = (TextView) convertView.findViewById(R.id.outletcategory);
	        TextView outlet_location = (TextView) convertView.findViewById(R.id.outletlocation);
	        ImageView logo = (ImageView) convertView.findViewById(R.id.logo);	        
	        Picasso.with((Activity) mContext).load(listItem.logoUrl).into(logo);
	        outletname.setText(listItem.outletName);
	        if(Integer.parseInt(listItem.outletCoupons) <= 1){
	        	outletcoupons.setText(listItem.outletCoupons+" Offer");
	        }else{
	        	outletcoupons.setText(listItem.outletCoupons+" Offers");
	        }
	        outlet_category.setText(Html.fromHtml(listItem.outletCategory));
	        outlet_location.setText(" "+listItem.outletLocation);
	        return convertView;

	    }
}
