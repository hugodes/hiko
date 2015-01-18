package com.feh.hiko;

import com.feh.hiko.db.Coord;
import com.feh.hiko.db.HikeDataSource;
import com.feh.hiko.io.MySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;


public class CreateHikeDetailsActivity extends Activity
        implements  ConnectionCallbacks,
                    OnConnectionFailedListener,
                    LocationListener {
    /**
     * Will contain the different Location Point
     */
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "basic-location-sample";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    protected android.location.Location mCurrentLocation;
    protected android.location.Location mLastLocation;
    protected String mLatitudeText;
    protected String mLongitudeText;

    HikeDataSource dataSource;


    private ImageView mImageView;
    private Bitmap mPhoto;

    private EditText mEditTextComment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_hike_details);
        buildGoogleApiClient();
        dataSource = new HikeDataSource(this);
        try {
            dataSource.open();
        }
        catch(SQLException e)
        {
            Log.w("SQLException", e);
        }
        mImageView = (ImageView) findViewById(R.id.imageView1);
        mEditTextComment = ((EditText)findViewById(R.id.comment_editText));
        mEditTextComment.setVisibility(View.INVISIBLE);

    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        //mGoogleApiClient.setMockMode(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private String saveToInternalSorage(Bitmap bitmapImage,String namePhoto){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir


        File mypath=new File(directory,namePhoto);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Saving on " + directory.getAbsolutePath().toString() + " " + namePhoto);
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path,String namePhoto)
    {

        try {
            File f=new File(path, namePhoto);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imageView1);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1888 && resultCode == RESULT_OK) {
            mPhoto = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(mPhoto);
            mEditTextComment.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onConnected(Bundle connectionHint) {

        Log.i(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updateUI();
        }

        startLocationUpdates();

        /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
            ((EditText)findViewById(R.id.point1_editText)).setText(mLatitudeText);
            ((EditText)findViewById(R.id.point2_editText)).setText(mLongitudeText);
        }
        else if (mLastLocation == null){
            ((EditText)findViewById(R.id.point1_editText)).setText("-1");
            ((EditText)findViewById(R.id.point2_editText)).setText("-1");
        }*/
    }


    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        Log.i(TAG, "je suis ici");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        mCurrentLocation = location;
        updateUI();

    }

    private void updateUI() {
        mLatitudeText = String.valueOf(mCurrentLocation.getLatitude());
        mLongitudeText = String.valueOf(mCurrentLocation.getLongitude());
        ((EditText)findViewById(R.id.point1_editText)).setText(mLatitudeText);
        ((EditText)findViewById(R.id.point2_editText)).setText(mLongitudeText);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void onDisconnected() {
        Log.i(TAG, "Disconnected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
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


    public void takePicture(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1888);
    }
    public void addLocation(View view) throws JSONException {

        String latitude = ((EditText)findViewById(R.id.latitude_editText)).getText().toString();
        String longitude = ((EditText)findViewById(R.id.longitude_editText)).getText().toString();


        updateUI();

        if(latitude.equals("") || longitude.equals("")){

            Toast.makeText(getApplicationContext(), R.string.validateException, Toast.LENGTH_LONG).show();

        }else {

            int coordId = dataSource.getNbLocations() + 1;
            Intent intent = getIntent();
            long hikeId = intent.getLongExtra("hikeId", 0);

            String namePhoto = String.valueOf(coordId) + "_" + String.valueOf(hikeId) + ".jpg";
            saveToInternalSorage(mPhoto, namePhoto);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            dataSource.addLocationPhoneDb(new Coord(coordId, hikeId, Float.parseFloat(latitude), Float.parseFloat(longitude), encodedImage, mEditTextComment.getText().toString()));

            //adding to server
            MySingleton.getInstance().addLocationToDb(coordId, hikeId, Float.parseFloat(latitude), Float.parseFloat(longitude), encodedImage, mEditTextComment.getText().toString());
            ((EditText) findViewById(R.id.point1_editText)).setText("");
            ((EditText) findViewById(R.id.point2_editText)).setText("");
            mEditTextComment.setText("");
        }





    }

    public void addHikeDb(View view) throws JSONException {

        Intent Nintent = new Intent(this,MenuActivity.class);
        startActivity(Nintent);

    }



}
