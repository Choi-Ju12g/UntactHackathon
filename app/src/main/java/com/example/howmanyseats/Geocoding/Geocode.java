package com.example.howmanyseats.Geocoding;

import android.util.Log;
import java.io.IOException;;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

    public class Geocode {

        String response;

        public String run(String address) throws IOException {
            HttpPost httpPost = new HttpPost("https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&components=country:KR,+CA&key=AIzaSyBc5yVULjb5W99qj4DtTMhtGy-co4JBavI");

            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse httpResponse = httpclient.execute(httpPost);

            response = EntityUtils.toString(httpResponse.getEntity());
            Log.v("response add",response);
            return response;
        }
    }
