package com.feh.hiko;

import com.feh.hiko.db.Coord;
import com.feh.hiko.db.HikeDataSource;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.Vector;



public class DetailHikeActivity extends FragmentActivity
        implements OnMapReadyCallback {

    /*
     * communication with the db
     */
    private HikeDataSource dataSource;
    Vector<Coord> vCoord;

    /*
     * will contain the details of each hike
     */
    private ListView lw_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detail_hike);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //we get the position of the correct element clicked previously
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        position++; // id start to 1 ,so we have to ++ position


        Log.i("Position clicked",String.valueOf(position));
        //we open the database
        dataSource = new HikeDataSource(this);
        try {
            dataSource.open();
        }
        catch(SQLException e)
        {
            Log.w("SQLExeception",e);
        }

        //We get the different points stored in the db for a specific position
        vCoord = dataSource.getLocationForId(position);

        //We attach the result to a listView
        /*lw_loc = (ListView) findViewById(R.id.listViewLoc);
        ArrayAdapter<Coord> adapter = new ArrayAdapter<Coord>(this,android.R.layout.simple_list_item_1,vCoord);
        lw_loc.setAdapter(adapter);*/


        dataSource.close();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        for (Coord c : vCoord) {
            Log.i("coordonnées", "lat = "+c.getLatitude()+" long = "+c.getLongitude());
            map.addMarker(new MarkerOptions()
            .position(new LatLng(c.getLatitude(), c.getLongitude()))
            .title("Marker"));
        }
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

    /** Called when the user clicks the Start Hike Button */
    public void startHike(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, HikeActivity.class);
        startActivity(intent);
    }


}
