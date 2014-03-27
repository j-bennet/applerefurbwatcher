package com.chaotix.applerefurbwatcher.helpers;

import com.chaotix.applerefurbwatcher.R;
import com.chaotix.applerefurbwatcher.R.string;

import android.content.Context;
import android.content.res.Resources;

public class ResourceManager {
	
	private static final ResourceManager instance = new ResourceManager();
	
	private Resources resources;
	
	private ResourceManager() {
	}
	
	public static ResourceManager getInstance() {
		return instance;
	}

	public static void initialize(Context context) {
		ResourceManager.getInstance().resources = context.getResources();
	}
	
	public String getAllDealsUrl() {
		return resources.getString(R.string.url_all_deals);
	}
}
