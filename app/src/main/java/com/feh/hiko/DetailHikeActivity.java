package com.feh.hiko;

import com.feh.hiko.db.Coord;
import com.feh.hiko.db.HikeDataSource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.Vector;



public class DetailHikeActivity extends Activity {


    /*
     * communication with the db
     */
    private HikeDataSource dataSource;

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

        //we get the position of the correct element clicked previously
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);



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
        Vector<Coord> vCoord = dataSource.getLocationForId(position);

        //We attach the result to a listView
        lw_loc = (ListView) findViewById(R.id.listViewLoc);
        ArrayAdapter<Coord> adapter = new ArrayAdapter<Coord>(this,android.R.layout.simple_list_item_1,vCoord);
        lw_loc.setAdapter(adapter);


        dataSource.close();
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


}
