package com.greenrobot;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andytriboletti on 6/23/17.
 * Copyright 2016 GreenRobot LLC
 */

public class GreenRobotAds {
    //static Context context;
    public static void initialize(final Context var0, String var1) {
        //this.context=var0;
        //connect to adserver with app id to get percentages
        //http://dev.adnetwork.greenrobot.com/ads/randomad?wid=1
        Thread thread = new Thread() {
            @Override
            public void run() {
                connect(var0, var1);

            }
        };

        thread.start();

    }

     private static void connect(Context context, String urlString) {
         //URL url;
         HttpURLConnection urlConnection = null;
         URL url = null;
         try {
             url = new URL(urlString);



             //url = new URL("https://myadserver3.greenrobot.com/getadmobile/2");

             urlConnection = (HttpURLConnection) url
                     .openConnection();

             InputStream inputStream = urlConnection.getInputStream();
             BufferedReader bR = new BufferedReader(  new InputStreamReader(inputStream));
             String line = "";

             StringBuilder responseStrBuilder = new StringBuilder();
             while((line =  bR.readLine()) != null){

                 responseStrBuilder.append(line);
             }
             inputStream.close();
             //String result = responseStrBuilder.toString();

             JSONArray result2= new JSONArray(responseStrBuilder.toString());
             Timber.tag("THERESULT");
             Timber.d(String.valueOf(result2));
             SharedPreferences.Editor editor = context.getSharedPreferences("gr_ad_prefs", MODE_PRIVATE).edit();
             editor.putString("weights", result2.toString());
             editor.apply();


         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (urlConnection != null) {
                 urlConnection.disconnect();
             }
         }




     }
}
