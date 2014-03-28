package com.chaotix.applerefurbwatcher.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.net.Uri;
import android.util.Log;

import com.chaotix.applerefurbwatcher.data.ImageButtonData;
import com.chaotix.applerefurbwatcher.data.ProductData;

public class AppleUrlManager {
	
	private static final String tag = "AppleUrlManager";
	private static final AppleUrlManager instance = new AppleUrlManager();
	private static String baseUrl = null;
	
	private AppleUrlManager() {
	}

	public static AppleUrlManager getInstance() {
		return instance;
	}
	
	public ImageButtonData[] getNavigationData() {
		String url = ResourceManager.getInstance().getAllDealsUrl();
		setBaseUrl(url);
		
		ImageButtonData[] buttonData = new ImageButtonData[0];
		Document document = fetch(url);
		
		if (document != null) {
			Elements listItems = document.select(".refurb-nav ul li");
			buttonData = new ImageButtonData[listItems.size()];
			
			int i = 0;
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
				buttonData[i] = data;
				i++;
			}
			
		}
		return buttonData;
	}
	
	public ProductData[] getProductData(String url) {
		url = normalizeUrl(url);
		
		ProductData[] productData = new ProductData[0];
		Document document = fetch(url);
		
		if (document != null) {
			Elements rows = document.select("div.box-content table tr.product");
			Log.d(tag, "Extracted product rows: " + rows.size());
			
			productData = new ProductData[rows.size()];
			
			int i = 0;
			for (Element row: rows) {
				Element image = row.select("td.image").first();
				Element specsParent = row.select("td.specs").first();
				Element header = specsParent.select("h3").first();
				Elements specs = header.siblingElements();
				Element priceParent = row.select("td.purchase-info p.price").first();
				Element price = priceParent.getElementsByAttributeValue("itemprop", "price").first();
				Element savings = row.select("td.purchase-info p.savings").first();
				ProductData data = new ProductData(
						image.attr("src"),
						Integer.parseInt(image.attr("width")),
						Integer.parseInt(image.attr("height")),
						header.text(),
						specs.html(),
						price.html(),
						savings == null ? null : savings.html());
				productData[i] = data;
				i++;
			}
		}
		
		return productData;
	}
	
	private String normalizeUrl(String url) {
		Uri uri = Uri.parse(url);
		if (!uri.isAbsolute()) {
			url = baseUrl + uri;
			Log.d(tag, "Url changed to: " + url);
		}
		return url;
	}
	
	private void setBaseUrl(String url) {
		Uri fullUri = Uri.parse(url);
		baseUrl = String.format("%s://%s", fullUri.getScheme(), fullUri.getHost());
		Log.d(tag, "Base URL: " + baseUrl);
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
