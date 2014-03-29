package com.chaotix.applerefurbwatcher;

import com.chaotix.applerefurbwatcher.data.ProductData;
import com.chaotix.applerefurbwatcher.helpers.AppleUrlManager;
import com.chaotix.applerefurbwatcher.helpers.ProductDataAdapter;
import com.chaotix.applerefurbwatcher.interfaces.ICountListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListActivity extends Activity implements ICountListener {
	
	public static final String tag = "ProductListActivity";
	public static final String EXTRA_URL = "url";
	
	private ProductDataAdapter adapter = null;
	private static View progress = null;
	private TextView txtSearch = null;
	private TextView txtCount = null;
	
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
        initializeProgress();
        initializeCount();
        retrieveProducts();
        initializeSearch();
    }
    
	private void retrieveProducts() {
		String url = getIntent().getExtras().getString(EXTRA_URL);
        new GetProductTask().execute(url);
	}

	private void initializeProgress() {
		progress = findViewById(R.id.progressList);
        progress.setVisibility(View.VISIBLE);
	}
	
    private void initializeCount() {
    	txtCount = (TextView) findViewById(R.id.txtCount);
    	updateCount(0);
    }

	private void initializeSearch() {
		txtSearch = (TextView) findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
   	     
    	    @Override
    	    public void onTextChanged(CharSequence cs, int start, int count, int after) {
    	        adapter.getFilter().filter(cs);  
    	    }
    	     
    	    @Override
    	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    	    }
    	     
    	    @Override
    	    public void afterTextChanged(Editable txt) {
    	    }
    	});
	}
    
	private void displayProducts(ProductData[] products) {
		ListView list = (ListView) findViewById(R.id.list);
		adapter = new ProductDataAdapter(this, R.layout.product_row, products, this);
        list.setAdapter(adapter);
	}

	@Override
	public void updateCount(int count) {
    	txtCount.setText(String.format(getResources().getString(R.string.found_items), count));
	}
}
