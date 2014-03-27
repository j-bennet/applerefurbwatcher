package com.chaotix.applerefurbwatcher.data;

public class ImageButtonData {
	public String Url;
	public String ImageUrl;
	public int Width;
	public int Height;
	public String Text;
	
	public ImageButtonData(String url, String imageUrl, int width, int height, String text) {
		Url = url;
		ImageUrl = imageUrl;
		Width = width;
		Height = height;
		Text = text;
	}
}
