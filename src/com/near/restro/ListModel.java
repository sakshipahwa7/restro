package com.near.restro;


public class ListModel {
    public String logoUrl;
    public String outletName;
    public String outletCoupons;
    public String outletCategory;
    public String outletLocation;
    
	public ListModel(String logoUrl, String outletName, String outletCoupons, String outletCategory, String outletLocation) {
			this.logoUrl = logoUrl;
	        this.outletName = outletName;
	        this.outletCoupons = outletCoupons;
	        this.outletCategory = outletCategory;
	        this.outletLocation = outletLocation;

	    }

	}