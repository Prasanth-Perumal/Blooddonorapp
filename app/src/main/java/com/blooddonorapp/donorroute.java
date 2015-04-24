package com.blooddonorapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import DAL.Donordatasource;
import Utilities.routemap;


public class donorroute extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    Location mylocation;
    GoogleApiClient mGoogleApiClient;
    Donordatasource dat;
    double[] location=new double[2];
    GoogleMap map;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorroute);

        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map=mapFragment.getMap();
        Intent in=this.getIntent();
        location= in.getDoubleArrayExtra("markerlocation");
        String title=in.getStringExtra("title");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(location[0],location[1]))
                .title(title));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ParseQuery userQuery = ParseInstallation.getQuery();
                ParsePush push = new ParsePush();
                push.setQuery(userQuery);
                push.setMessage("Willie Hayes injured by own pop fly.");
                push.sendInBackground();
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mylocation != null) {
            //Toast.makeText(this, String.valueOf(mylocation.getLatitude()), Toast.LENGTH_LONG).show();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()))
                    .title("My Location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mylocation.getLatitude(), mylocation.getLongitude())));
            map.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);
        }
       String uri= makeURL(mylocation.getLatitude(),mylocation.getLongitude(),location[0],location[1]);
        routemap rm=new routemap(uri,donorroute.this,map);
        rm.execute();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }
    public void register(View v)
    {
        Intent in=new Intent(this,Register.class);
        startActivity(in);
    }
}
