package com.greenrobot;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.greenrobot.pirates.R;
import com.greenrobot.pirates.activities.Home;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andytriboletti on 6/21/17.
 * Copyright 2016 GreenRobot LLC
 */

public class GreenRobotAdView extends FrameLayout implements MoPubView.BannerAdListener {
    String adUnitId;
    MoPubView.BannerAdListener listener;
    Context context;
    public GreenRobotAdView(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    public GreenRobotAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

    }

    public GreenRobotAdView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public GreenRobotAdView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;

    }


    public void setAdUnitId(String adUnitId) {
        HashMap<String, Float> weightMap = new HashMap<String, Float>();
        this.adUnitId=adUnitId;
        Random rand = new Random();
        //int value = rand.nextInt(3);
        SharedPreferences shared = context.getSharedPreferences("gr_ad_prefs", MODE_PRIVATE);
//        liberty();
//        return;

        String strJson = shared.getString("weights","0");//second parameter is necessary ie.,Value to return if this preference does not exist.
        if(strJson != null) try {
            JSONArray jsonData = new JSONArray(strJson);
            float mopubWeight=0;
            float admobWeight=0;
            for(int i=0; i< jsonData.length(); i++) {
                JSONObject myObject = jsonData.getJSONObject(i);
                String type = myObject.getString("type");
                float weight = Float.valueOf(myObject.getString("weight"));
                if(type.equals("mopub")) {
                    weightMap.put("mopub", weight);

                    //mopubWeight = weight;
                }
                else if(type.equals("adsense")) {
                   // admobWeight = weight;
                    weightMap.put("adsense", weight);


                }

            }
            sortByValue(weightMap);

            Random r = new Random();

            float random = 0 + r.nextFloat() * (1 - 0);
            Timber.tag("RANDOM");
            Timber.e(String.valueOf(random));
            //if(random < )
            Iterator it = weightMap.entrySet().iterator();
            float previousWeights=0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Timber.tag("HASHMAP2");
                Timber.e(pair.getKey() + " = " + pair.getValue());
                //it.remove(); // avoids a ConcurrentModificationException
                float myWeight = Float.valueOf(pair.getValue().toString());
                if(random < myWeight + previousWeights) {
                    Timber.tag("ADTOUSE");
                    String network = pair.getKey().toString();
                    Timber.e(network);
                    if(network.equals("mopub")) {
                        mopub();
                    }
                    else if(network.equals("adsense")) {
                        admob();
                    }
                    else if(network.equals("liberty")) {
                        liberty();
                    }

                    return;

                }
                else {
                    previousWeights=previousWeights+myWeight;
                }

                //it.next();


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//
//        if(value == 0) {
//        //if(false) {
//            liberty();
//        }
//        else if(value == 1) {
//        //if(true) {
//            mopub();
//        }
//        else if(value == 2) {
//            admob();
//        }

    }
//952813141309-1leqjgonu3lakv01noh6vlb5a9snvhr8.apps.googleusercontent.com


    //https://stackoverflow.com/a/109389/211457
    private static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private void admob() {
        AdView adView = new AdView(context);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //Log.i("Ads", "onAdFailedToLoad");
                //liberty();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });
        adView.setAdSize(AdSize.BANNER);
        final String s = context.getString(R.string.admob_id);

        adView.setAdUnitId(s);
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
        addView(adView);
    }

    private void liberty() {


//        LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = (View) inflater.inflate(R.layout.ad_layout, null);
//
//        WebView webView = (WebView) view.findViewById(R.id.gr_webview);
//        webView.loadUrl("http://comingsoon");
//
//        //removeAllViews();
//        ((ViewGroup)(webView.getParent())).removeView(webView);
//        addView(webView);


    }

    private void mopub() {
        //removeAllViews();


        LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (View) inflater.inflate(R.layout.ad_layout, null);



        MoPubView moPubView = (MoPubView) view.findViewById(R.id.mopub_adview);
        moPubView.setAdUnitId(context.getString(R.string.mopub_id));
        moPubView.loadAd();
        moPubView.setBannerAdListener(this);
        ((ViewGroup)(moPubView.getParent())).removeView(moPubView);
        addView(moPubView);

    }

    public void setBannerAdListener(MoPubView.BannerAdListener banner) {
        this.listener=banner;
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {

    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        //mopub failed
        Timber.e("Mopub Failed");
        admob();
    }

    @Override
    public void onBannerClicked(MoPubView banner) {

    }

    @Override
    public void onBannerExpanded(MoPubView banner) {

    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {

    }


}
