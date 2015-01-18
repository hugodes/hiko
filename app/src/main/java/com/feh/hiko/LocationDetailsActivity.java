package com.feh.hiko;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.feh.hiko.db.Coord;
import com.feh.hiko.db.HikeDataSource;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;


public class LocationDetailsActivity extends Activity {

    //member used to communicate with the database

    HikeDataSource dataSource;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        List<Geofence> list = GeofencingEvent.fromIntent(getIntent()).getTriggeringGeofences();
        int id = Integer.valueOf(list.get(0).getRequestId());
        mImageView = (ImageView)findViewById(R.id.imageView1);

        dataSource = new HikeDataSource(this);
        try {
            dataSource.open();
            Coord coord = dataSource.getSingleCoordForId(id);
            ((TextView)findViewById(R.id.longitude_textView)).setText(String.valueOf(coord.getPoint1()));
            ((TextView)findViewById(R.id.latitude_textView)).setText(String.valueOf(coord.getPoint2()));
            ((TextView)findViewById(R.id.comment_textView)).setText(String.valueOf(coord.getComment()));
            String namePhoto = String.valueOf(coord.getId()) + "_" + String.valueOf(coord.getHikeId()) + ".jpg";
            loadImageFromStorage("/data/data/com.feh.hiko/app_imageDir",namePhoto);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // A toi theo, t'as l'id
    }

    private void loadImageFromStorage(String path,String namePhoto)
    {

        try {
            File f=new File(path, namePhoto);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
        //    mImageView=(ImageView)findViewById(R.id.imageView1);
            mImageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_details, menu);
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
