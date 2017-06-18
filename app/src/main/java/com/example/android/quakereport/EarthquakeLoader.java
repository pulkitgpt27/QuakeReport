package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

/**
 * Created by Pulkit on 09-06-2017.
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;

    public EarthquakeLoader(Context context,String url) {
        super(context);
        Log.e(LOG_TAG,"In constructor");
        mUrl=url;
    }

    @Override
    public List<EarthQuake> loadInBackground() {
        Log.e(LOG_TAG,"In Main Background task");
            if(mUrl==null){
                return null;
            }
        List<EarthQuake> earthquakes = QueryUtils.fetchEarthQuakeData(mUrl);
        return earthquakes;

    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG,"In start loading");
        forceLoad();
    }
}
