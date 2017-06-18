/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<EarthQuake>> {

    private static final String USGS_REQUEST_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query";//"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=15";


    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final int EARTHQUAKE_LOADER_ID = 1;


    private EarthQuakeAdapter mAdapter;

    private TextView emptyTextView;
    private ProgressBar spinnerBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG_TAG,"Appear once");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        emptyTextView=(TextView)findViewById(R.id.empty_view);


        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()){

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        }
        else {
            View loadingIndicator=findViewById(R.id.spinner);
            loadingIndicator.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }
        // Create a fake list of earthquake locations.


        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(emptyTextView);
        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthQuakeAdapter(this, new ArrayList<EarthQuake>());
        // Find a reference to the {@link ListView} in the layout

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake curquake = mAdapter.getItem(position);

                Uri earthQuakeUri = Uri.parse(curquake.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, earthQuakeUri);
                startActivity(webIntent);
            }
        });

    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG,"In On create Loader");
        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(this);
        String minMag=sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default)
        );
        String orderBy=sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri=Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuild=baseUri.buildUpon();

        uriBuild.appendQueryParameter("format","geojson");
        uriBuild.appendQueryParameter("limit","10");
        uriBuild.appendQueryParameter("minmag",minMag);
        uriBuild.appendQueryParameter("orderby",orderBy);
        return new EarthquakeLoader(this,uriBuild.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        Log.e(LOG_TAG,"Loader Finished");
        spinnerBar=(ProgressBar) findViewById(R.id.spinner);
        spinnerBar.setVisibility(View.GONE);
        emptyTextView.setText(R.string.no_earthquakes);
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
          mAdapter.addAll(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
        Log.e(LOG_TAG,"Loader restarted");
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            Intent settingsIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}