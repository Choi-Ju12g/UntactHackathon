package com.example.howmanyseats.Geocoding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

    public class Geocode {

        String response = "";

        public String run(String address) throws IOException {
            HttpPost httpPost = new HttpPost("https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&components=country:KR");

            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse httpResponse = httpclient.execute(httpPost);

            response = EntityUtils.toString(httpResponse.getEntity());

            return response;
        }
    }
