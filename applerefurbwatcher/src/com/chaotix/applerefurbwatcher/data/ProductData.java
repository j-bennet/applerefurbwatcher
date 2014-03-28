package com.chaotix.applerefurbwatcher.data;

public class ProductData {
	public String ImageUrl;
	public int ImageWidth;
	public int ImageHeight;
	public String Header;
	public String Specs;
	public String Price;
	public String Savings;
	
	public ProductData(String imageUrl, int imageWidth, int imageHeight, String header, String specs, String price, String savings) {
		super();
		ImageUrl = imageUrl;
		ImageWidth = imageWidth;
		ImageHeight = imageHeight;
		Header = header;
		Specs = specs;
		Price = price;
		Savings = savings;
	}
	
}
