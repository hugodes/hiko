package com.feh.hiko;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.feh.hiko.db.HikeDataSource;
import com.feh.hiko.io.MyApplication;
import com.feh.hiko.io.MySingleton;

import java.sql.SQLException;
import java.util.Locale;

public class StartPageActivity extends Activity {

       /*
     * member for the communication with the database
     */

    private HikeDataSource dataSource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start_page);



  /*      //Trying AsyncTask
        ServerASyncTask server_sync = new ServerASyncTask();
        server_sync.execute();*/

        dataSource = new HikeDataSource(this);
        try {
            dataSource.open();
        }
        catch(SQLException e)
        {
            Log.w("SQLExeception", e);
        }


        //Database synchronisation
        MyApplication app = (MyApplication)getApplication(); //to remove i think
        MySingleton.getInstance().setDataSource(dataSource);
        MySingleton.getInstance().getDbFromServer();


    }

    public void setFrLanguage(View view){
        Locale locale = new Locale("fr-FR");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        ((TextView)findViewById(R.id.start_button)).setText(R.string.start_button);

    }

    public void setUsLanguage(View view)
    {
        Locale locale = new Locale("en-US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        ((TextView)findViewById(R.id.start_button)).setText(R.string.start_button);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startMenuActivity(View view)
	{
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}
}
