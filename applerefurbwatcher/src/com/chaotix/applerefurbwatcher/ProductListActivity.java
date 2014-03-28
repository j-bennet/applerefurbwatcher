package com.chaotix.applerefurbwatcher;

import com.chaotix.applerefurbwatcher.data.ProductData;
import com.chaotix.applerefurbwatcher.helpers.AppleUrlManager;
import com.chaotix.applerefurbwatcher.helpers.ProductDataAdapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ProductListActivity extends Activity {
	
	public static final String tag = "ProductListActivity";
	public static final String EXTRA_URL = "url"; 
	private static View progress = null;
	
	private class GetProductTask extends AsyncTask<String, Void, ProductData[]> {
		
		protected void onPostExecute(ProductData[] result) {
			progress.setVisibility(View.GONE);
			displayProducts(result);
		}

		@Override
		protected ProductData[] doInBackground(String... url) {
			return AppleUrlManager.getInstance().getProductData(url[0]);
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        String url = getIntent().getExtras().getString(EXTRA_URL);
        progress = findViewById(R.id.progressList);
        progress.setVisibility(View.VISIBLE);
        new GetProductTask().execute(url);
    }
    
	private void displayProducts(ProductData[] products) {
		ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new ProductDataAdapter(this, R.layout.product_row, products));		
	}
}
