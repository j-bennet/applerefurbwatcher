package com.chaotix.applerefurbwatcher;

import java.util.List;

import com.chaotix.applerefurbwatcher.data.ImageButtonData;
import com.chaotix.applerefurbwatcher.helpers.AppleUrlManager;
import com.chaotix.applerefurbwatcher.helpers.ImageDownloader;
import com.chaotix.applerefurbwatcher.helpers.ImageDownloader.Mode;
import com.chaotix.applerefurbwatcher.helpers.ResourceManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {
	
	private static final String tag = "MainActivity";
	
	private static final int NAV_BUTTON_OFFSET = 100;

	private static final ImageDownloader imageDownloader = new ImageDownloader();
	
	private static int[] imageButtonIds = null;
	
	private static View progress = null;

	private OnClickListener navClickListener = new OnClickListener() {
		
        @Override
        public void onClick(View v) {
        	ImageButtonData imageTag = (ImageButtonData) v.getTag();
            Log.d(tag, "URL clicked: " + imageTag.Url);
        }
    };
    
	private class GetNavTask extends AsyncTask<Void, Void, List<ImageButtonData>> {
		
		protected void onPostExecute(List<ImageButtonData> result) {
			progress.setVisibility(View.GONE);
			createNavigationButtons(result);
		}

		@Override
		protected List<ImageButtonData> doInBackground(Void... arg0) {
			return AppleUrlManager.getInstance().getNavigationData();
		}
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeManagers();
        createNavigation();
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
    
    private void createNavigationButtons(List<ImageButtonData> buttonData) {
		LinearLayout llMain = (LinearLayout) findViewById(R.id.llMain);
		imageButtonIds = new int[buttonData.size()];

		int i = 0;
    	for (ImageButtonData navData: buttonData) {
    		int buttonId = NAV_BUTTON_OFFSET + i;
    		Button button = new Button(this);
    		button.setId(buttonId);
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
            imageButtonIds[i] = buttonId;
            i++;
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
