package com.chaotix.applerefurbwatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.chaotix.applerefurbwatcher.data.ImageButtonData;
import com.chaotix.applerefurbwatcher.helpers.AppleUrlManager;
import com.chaotix.applerefurbwatcher.helpers.ImageDownloader;
import com.chaotix.applerefurbwatcher.helpers.ImageDownloader.Mode;
import com.chaotix.applerefurbwatcher.helpers.ResourceManager;
/**
 * Scheme colors from: http://www.colourlovers.com/palette/196488/Mod_Mod_Mod_Mod 
 * 
 **/
public class MainActivity extends Activity {
	
	private static final String tag = "MainActivity";
	private static final ImageDownloader imageDownloader = new ImageDownloader();
	private static View progress = null;

	private class GetNavTask extends AsyncTask<Void, Void, ImageButtonData[]> {
		
		protected void onPostExecute(ImageButtonData[] result) {
			progress.setVisibility(View.GONE);
			createNavigationButtons(result);
		}

		@Override
		protected ImageButtonData[] doInBackground(Void... arg) {
			return AppleUrlManager.getInstance().getNavigationData();
		}
	}
    
	private OnClickListener navClickListener = new OnClickListener() {
		
        @Override
        public void onClick(View v) {
        	ImageButtonData imageTag = (ImageButtonData) v.getTag();
            Log.d(tag, "URL clicked: " + imageTag.Url);
            startListActivity(imageTag.Url);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeManagers();
        createNavigation();
    }
    
    private void startListActivity(String url) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra(ProductListActivity.EXTRA_URL, url);
        startActivity(intent);
    }
    
    private void initializeManagers() {
    	imageDownloader.setMode(Mode.CORRECT);
    	imageDownloader.setResources(getResources());
		progress = findViewById(R.id.progressMain);
    	ResourceManager.initialize(this);
    }

	private void createNavigation() {
		progress.setVisibility(View.VISIBLE);
		new GetNavTask().execute();
	}
	
    private void createNavigationButtons(ImageButtonData[] buttonData) {
		LinearLayout llMain = (LinearLayout) findViewById(R.id.llMain);

    	for (int i = 0; i < buttonData.length; i++) {
    		ImageButtonData navData = buttonData[i];
    		Button button = new Button(this);
    		button.setTag(navData);
    		button.setClickable(true);
    		button.setOnClickListener(navClickListener);
    		button.setBackgroundResource(R.drawable.bt_nav);
    		button.setText(navData.Text);
    		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    		params.setMargins(5, 5, 5, 5);
    		button.setLayoutParams(params);
    		llMain.addView(button);
            imageDownloader.download(navData.ImageUrl, button);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
