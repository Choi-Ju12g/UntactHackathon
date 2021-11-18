package com.example.howmanyseats.Geocoding;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GeoThread extends Thread{

    Handler handler;
    boolean isRun = true;
    public String address;

    public GeoThread(Handler handler){
        this.handler = handler;
    }

    public void run(){
        Geocode geo = new Geocode();
        Bundle bun = new Bundle();
        String response;


        try {
            response = geo.run(address);
            JSONObject json = new JSONObject(response);

            JSONArray results = (JSONArray) json.get("results");


            JSONObject resultsArray = (JSONObject) results.get(0);

            JSONObject jsonObject = (JSONObject) resultsArray.get("geometry");

            JSONObject jsonObject2 = (JSONObject) jsonObject.get("location");


            double address_lat = (double) jsonObject2.get("lat");
            double address_lng = (double) jsonObject2.get("lng");

            bun.putDouble("address_lat", address_lat);
            bun.putDouble("address_lng", address_lng);

            Message msg = handler.obtainMessage();
            msg.setData(bun);
            handler.sendMessage(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
