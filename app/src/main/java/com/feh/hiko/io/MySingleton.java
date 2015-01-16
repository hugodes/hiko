package com.feh.hiko.io;

import android.util.Log;

import com.feh.hiko.db.Coord;
import com.feh.hiko.db.Hike;
import com.feh.hiko.db.HikeDataSource;


import org.json.JSONException;
import org.json.JSONObject;

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

    public String customVar;

//    private SocketIO socket;
    private HikeDataSource dataSource;
    Socket sock;

  //  IO socket;

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

            System.out.println("HERE AVANT CONNECT ");
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
                    System.out.println("Coord to Add " + coordToAdd);
                    if (getDb().isHikeExist(coordToAdd.getId())){
                        Log.w("HIKE","ALREADY EXIST");

                    }
                    else {
                        getDb().addLocationPhoneDb(coordToAdd);
                        Log.w("ADDING COORD = ", coordToAdd.toString());
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
    public void addLocationToDb(long id,long hikeId,float latitude,float longitude) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("hikeId",hikeId);
        json.put("latitude",latitude);
        json.put("longitude",longitude);

   //     socket.emit("add_location",json);
    }


    //add an hike to the database
    public void addHikeToDb(Hike hikeToAdd) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id",hikeToAdd.getId());
        json.put("name",hikeToAdd.getHikeName());
        json.put("totalDistance",hikeToAdd.getTotalDistance());
        json.put("totalTime",hikeToAdd.getTotalTime());

   //     socket.emit("add_db", json);

    }

    public void customSingletonMethod()
    {
        // Custom method
        Log.i("Singleton","We are in the singleton");
   //     socket.emit("coucou","ici");
    }
}

