package com.feh.hiko;

import com.feh.hiko.db.Hike;
import com.feh.hiko.db.HikeDataSource;
import com.feh.hiko.db.Location;
import com.feh.hiko.db.Coord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.sql.SQLException;



public class CreateHikeActivity extends Activity {


    /*
     * used for the communication with the db
     */
    private HikeDataSource dataSource;

    /*
     * used to get information from the layout
     */
    String hikeName;
    String hikeDistance;
    String hikeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_hike);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void createHikeDetailsActivity(View view) throws SQLException {
        hikeName = ((EditText)findViewById(R.id.hike_name_editText)).getText().toString();
        hikeDistance = ((EditText)findViewById(R.id.hike_distance_editText)).getText().toString();
        hikeTime = ((EditText)findViewById(R.id.hike_time_editText)).getText().toString();

        HikeDataSource hike_db = new HikeDataSource(this);
        hike_db.open();
        int hikeId = hike_db.getNbHike();
        Log.w("hikeId in CREATEHIKE", Integer.toString(hikeId));
        Intent n = new Intent(getApplicationContext(), CreateHikeDetailsActivity.class);
        n.putExtra("hikeId",hikeId);
        n.putExtra("hikeName", hikeName);
        n.putExtra("hikeDistance", hikeDistance);
        n.putExtra("hikeTime", hikeTime);
        startActivity(n);
    }




}
