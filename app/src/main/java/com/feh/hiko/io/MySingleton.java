package com.feh.hiko.io;

import android.util.Log;

import com.feh.hiko.db.Coord;
import com.feh.hiko.db.Hike;
import com.feh.hiko.db.HikeDataSource;
import com.feh.hiko.db.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

/**
 * Created by theoz on 1/12/15.
 */


public class MySingleton
{
    private static MySingleton instance;

    public String customVar;

    private SocketIO socket;
    private HikeDataSource dataSource;

    public static void initInstance()
    {
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
    private MySingleton()
    {
        // Constructor hidden because this is a singleton
        try {
            socket = new SocketIO("http://192.168.1.94:3000");
            socket.connect(new IOCallback() {
                @Override
                public void onMessage(JSONObject json, IOAcknowledge ack) {
                    try {
                        System.out.println("Server said:" + json.toString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onMessage(String data, IOAcknowledge ack) {
                    System.out.println("Server said: " + data);
                }

                @Override
                public void onError(SocketIOException socketIOException) {
                    System.out.println("an Error occured");
                    socketIOException.printStackTrace();
                }

                @Override
                public void onDisconnect() {
                    System.out.println("Connection terminated.");
                }

                @Override
                public void onConnect() {
                    System.out.println("Connection established");
                }

                @Override
                public void on(String event, IOAcknowledge ack, Object... args) {
                    System.out.println("Server triggered event '" + event + "'");



                    if (event.equals("get_db")){
                        //      Vector<Hike> hikes = new Vector<Hike>();
                        String []test = args[0].toString().replace("\\[", "").split("\\{");
                        String []second_parse = test[1].split(",");
                        Hike hikeToAdd = new Hike();
                        for (int i=0;i<second_parse.length;i++){
                            Log.w("SECOND_PARSE = ", second_parse[i]);
                            String third_parse = second_parse[i].replace("\"", "");
                            if(i == second_parse.length-1)
                                third_parse = third_parse.substring(0,third_parse.length()-1);
                            String []final_parse = third_parse.split(":");
                            Log.w("THIRD_PARSE = " , third_parse);
                            String hikeName = null,hikeId = null,totalDistance = null,totalTime = null;


                            for (int y=0;y<final_parse.length;y++){


                                if (final_parse[y].equals("id")){
                                    hikeId = final_parse[1];

                                    hikeToAdd.setId(Long.valueOf(hikeId));

                                }
                                if(final_parse[y].equals("totalTime")){
                                    totalTime = final_parse[1].toString();
                                    hikeToAdd.setTotalTime(Float.valueOf(totalTime));

                                }
                                if(final_parse[y].equals("totalDistance")){
                                    totalDistance = final_parse[1].toString();
                                    hikeToAdd.setTotalDistance(Float.valueOf(totalDistance));
                                }

                                if(final_parse[y].equals("name")){
                                    hikeName = final_parse[y+1].toString();
                                    hikeToAdd.setHikeName(hikeName);
                                }



                            }
                        }

                        if (dataSource.isHikeExist(hikeToAdd.getId())){
                            Log.w("HIKE","ALREADY EXIST");

                        }
                        else {
                            dataSource.addHikePhoneDb(hikeToAdd);
                            Log.w("ADDING HIKE = ", hikeToAdd.toString());
                        }

                    }

                    if(event.equals("get_location")){
                        Log.w("LOCATION ",args[0].toString());
                        String []first_parse = args[0].toString().split("\\{");
                        String []second_parse = first_parse[1].split(",");
                        Location locationToAdd = new Location();
                        Coord coordToAdd = new Coord();
                        for (int i=0;i<second_parse.length;i++){
                            Log.w("LOCATION DETAILS ",second_parse[i].toString());
                            String third_parse = second_parse[i].replace("\"", "");
                            if(i == second_parse.length-1)
                                third_parse = third_parse.substring(0,third_parse.length()-1);
                            String []final_parse = third_parse.split(":");
                            for (int y=0;y<final_parse.length;y++){
                                if (final_parse[y].equals("id")){
                                    coordToAdd.setId(Long.valueOf(final_parse[y+1]));
                                }
                                if(final_parse[y].equals("longitude")){
                                    coordToAdd.setPoint1(Float.valueOf(final_parse[y+1]));
                                }

                                if (final_parse[y].equals("latitude")) {
                                    coordToAdd.setPoint2(Float.valueOf(final_parse[y+1]));
                                }
                                if(final_parse[y].equals("hikeId")){
                                    coordToAdd.setHikeId(Long.valueOf(final_parse[y+1]));
                                }


                            }

                        }

                        Log.w("LOCATION DETAILS AFTER ", coordToAdd.toString());
                        if(dataSource.isLocationsExist(coordToAdd.getId())){
                            Log.w("LOCATION ","LOCATION ALREADY EXIST");
                        }
                        else{
                            Log.w("LOCATION","LOCATION ADDED");
                            dataSource.addLocationPhoneDb(coordToAdd);
                        }

                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getDbFromServer(){
        socket.emit("set_db","");
    }
    //Add a single location to the database
    public void addLocationToDb(long id,long hikeId,float latitude,float longitude) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("hikeId",hikeId);
        json.put("latitude",latitude);
        json.put("longitude",longitude);

        socket.emit("add_location",json);
    }


    //add an hike to the database
    public void addHikeToDb(Hike hikeToAdd) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id",hikeToAdd.getId());
        json.put("name",hikeToAdd.getHikeName());
        json.put("totalDistance",hikeToAdd.getTotalDistance());
        json.put("totalTime",hikeToAdd.getTotalTime());

        socket.emit("add_db", json);

    }

    public void customSingletonMethod()
    {
        // Custom method
        Log.i("Singleton","We are in the singleton");
        socket.emit("coucou","ici");
    }
}

