package com.feh.hiko.io;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.feh.hiko.db.Coord;
import com.feh.hiko.db.Hike;
import com.feh.hiko.db.HikeDataSource;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;

import com.google.gson.Gson;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

/**
 * Created by theoz on 1/12/15.
 */


public class MySingleton
{
    private static MySingleton instance;


    private HikeDataSource dataSource;
    private Context mContext;
    Socket sock;

    public void setContext(Context context){
        mContext = context;
    }
    public static void initInstance() {
        if (instance == null)
        {
            // Create the instance
            instance = new MySingleton();

        }
    }

    public static MySingleton getInstance()
    {
        // Return the instance
        return instance;
    }

    public void setDataSource(HikeDataSource dataSource){
        this.dataSource = dataSource;
    }
    private MySingleton()  {
        try{
            //http://192.168.1.32:3000 local
            //http://nodesocketapplication-hiko.rhcloud.com:8000 net
             sock = IO.socket("http://nodesocketapplication-hiko.rhcloud.com:8000");

        //sock = IO.socket("http://192.168.1.32:3000");
            //Place all events here as per documention
            sock.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args){
                    System.out.println("connect");
                }
            });


            //route to add in the phone database
            sock.on("get_db", new Emitter.Listener(){
                @Override
                public void call(Object... args){
                    System.out.println("GET DB" + args[0].toString());
                    Gson myGson = new Gson();
                    Hike hikeToAdd = myGson.fromJson(args[0].toString(),Hike.class);
                    System.out.println("Hike to Add " + hikeToAdd);
                        if (getDb().isHikeExist(hikeToAdd.getId())){
                        Log.w("HIKE","ALREADY EXIST");


                    }
                    else {
                        getDb().addHikePhoneDb(hikeToAdd);
                        Log.w("ADDING HIKE = ", hikeToAdd.toString());
                    }

                }
            });


            sock.on("get_location", new Emitter.Listener(){
                @Override
                public void call(Object... args){
                    System.out.println("GET Location" + args[0].toString());
                    Gson myGson = new Gson();
                    Coord coordToAdd = myGson.fromJson(args[0].toString(),Coord.class);
                    byte[] decodedByte = Base64.decode(coordToAdd.getPicture(), 0);
                    Bitmap photo = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                    String namePhoto = String.valueOf(coordToAdd.getId()) + "_" + String.valueOf(coordToAdd.getHikeId()) + ".jpg";
                    saveToInternalSorage(photo,namePhoto);
                //    System.out.println("Coord to Add " + coordToAdd);
                    if (getDb().isLocationsExist(coordToAdd.getId())){
                        Log.w("COORD","COORD ALREADY EXIST");

                    }
                    else {
                        getDb().addLocationPhoneDb(coordToAdd);
                        Log.w("ADDING COORD = ", coordToAdd.toString());
                        Log.w("SAVING PICTURE = ", namePhoto);
                    }

                }
            });

            sock.connect();
        } catch(URISyntaxException e){
            e.printStackTrace();
        }
    }

    public HikeDataSource getDb(){
        return dataSource;
    }

    public void getDbFromServer(){
        sock.emit("set_db","");
    }
    //Add a single location to the database
    public void addLocationToDb(long id,long hikeId,float latitude,float longitude,String picture,String comment) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("hikeId",hikeId);
        json.put("latitude",latitude);
        json.put("longitude",longitude);
        json.put("picture", picture);
        json.put("comment", comment);

        sock.emit("add_location",json);
    }


    //add an hike to the database
    public void addHikeToDb(Hike hikeToAdd) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id",hikeToAdd.getId());
        json.put("hikeName",hikeToAdd.getHikeName());
        json.put("totalDistance",hikeToAdd.getTotalDistance());
        json.put("totalTime",hikeToAdd.getTotalTime());

        sock.emit("add_db", json);

    }
    private String saveToInternalSorage(Bitmap bitmapImage,String namePhoto){

        ContextWrapper cw = new ContextWrapper(mContext);
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


}

