package com.chaotix.applerefurbwatcher.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.chaotix.applerefurbwatcher.data.ImageButtonData;

public class AppleUrlManager {
	
	private static final String tag = "AppleUrlManager";
	private static final AppleUrlManager instance = new AppleUrlManager();
	
	private AppleUrlManager() {
	}

	public static AppleUrlManager getInstance() {
		return instance;
	}
	
	public List<ImageButtonData> getNavigationData() {
		List<ImageButtonData> buttonData = new ArrayList<ImageButtonData>();
		Document document = fetch(ResourceManager.getInstance().getAllDealsUrl());
		if (document != null) {
			Elements listItems = document.select(".refurb-nav ul li");
			for (Element listItem: listItems) {
				Element image = listItem.select("img").first();
				Element link = listItem.select("a").first();
				Element header = listItem.select(".copy h2").first();
				ImageButtonData data = new ImageButtonData(
						link.attr("href"),
						image.attr("src"),
						Integer.parseInt(image.attr("width")),
						Integer.parseInt(image.attr("height")),
						header.text());
				buttonData.add(data);
			}
			
		}
		return buttonData;
	}
	
	private Document fetch(String url) {
		Document document = null;
		try {
			Log.d(tag, "Retrieving: " + url);
			document = Jsoup.connect(url).get();
			Log.d(tag, "Retrieved: " + url);
		}
		catch (IOException e) {
			Log.e(tag, e.toString());
		}
		return document;
	}
	
}
