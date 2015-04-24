package com.blooddonorapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.List;

import DAL.Donordatasource;
import Model.User;


public class bcMapSearchresult extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    GoogleMap map;
    Location mylocation;
    GoogleApiClient mGoogleApiClient;
    Donordatasource dat;
    String bloodtype;
    static Marker mark;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bc_map_searchresult);
        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map=mapFragment.getMap();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        dat=new Donordatasource(this);
        Intent in=this.getIntent();
         bloodtype= in.getStringExtra("bloodtype");
        dat.open();
        SharedPreferences settings = getSharedPreferences("flag", 0);
        boolean firstStart = settings.getBoolean("firstStart", true);

        if(firstStart) {
            //display your Message here
            dat.createDonor("prasanth","9980568952",77.664921,12.989435,"Bangalore","A+");
            dat.createDonor("dina","9980568952",77.664931,12.989435,"Bangalore","A+");
            dat.createDonor("prakash","9980568952",77.664941,12.989545,"Bangalore","A+");
            dat.createDonor("vijay","9980568952",77.664103,12.989435,"Bangalore","A+");

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();
        }

map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
    @Override
    public boolean onMarkerClick(final Marker marker) {
        mark=marker;
        LinearLayout lay=(LinearLayout) findViewById(R.id.layout_markerclick);
        lay.setVisibility(View.VISIBLE);
        TextView txt_name=(TextView)findViewById(R.id.txtvw_markername);
         TextView txt_dist=(TextView)findViewById(R.id.txvw_markerdistance);
        txt_name.setText(marker.getTitle());
        double distance;
        distance=CalculationByDistance(new LatLng(mylocation.getLatitude(),mylocation.getLongitude()),marker.getPosition());
        txt_dist.setText(String.valueOf(distance)+"kms");
        Button get_directions=(Button)findViewById(R.id.btn_markerdirection);
        get_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(bcMapSearchresult.this,donorroute.class);
              double[] location=new double[2];
                location[0]=mark.getPosition().latitude;
                location[1]=mark.getPosition().longitude;
                in.putExtra("markerlocation",location);
                in.putExtra("title",mark.getTitle());
               startActivity(in);
                Toast.makeText(bcMapSearchresult.this,mark.getTitle(),Toast.LENGTH_LONG).show();
            }
        });
        return false;
    }
});
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LinearLayout lay=(LinearLayout) findViewById(R.id.layout_markerclick);
                lay.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        map.clear();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bc_map_searchresult, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mylocation != null) {
            //Toast.makeText(this, String.valueOf(mylocation.getLatitude()), Toast.LENGTH_LONG).show();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(mylocation.getLatitude(),mylocation.getLongitude()))
                    .title("My Location"));
            map.addCircle(new CircleOptions().center(new LatLng(mylocation.getLatitude(),mylocation.getLongitude())).radius(1000).fillColor(R.color.lightgrey));
            map.addCircle(new CircleOptions().center(new LatLng(mylocation.getLatitude(),mylocation.getLongitude())).radius(2000));
            map.addCircle(new CircleOptions().center(new LatLng(mylocation.getLatitude(),mylocation.getLongitude())).radius(3000));

            List<User> values = dat.getAllDonors(bloodtype);
            Marker[] a=new Marker[values.size()];
            for(int i=0;i<values.size();i++)
            {
                Log.e("names", String.valueOf(values.get(i).getLatitude())+String.valueOf(values.get(i).getLongitude()));

                a[i]=map.addMarker(new MarkerOptions()
                        .position(new LatLng(values.get(i).getLatitude(),values.get(i).getLongitude()))
                        .title(values.get(i).getName()));

            }
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mylocation.getLatitude(), mylocation.getLongitude())));
          //
          //
          //
          // map.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);
           map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()),(float) 14.6));

        } else {
            Toast.makeText(this, "no locaTION", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

        return Radius * c;
    }
    public void register(View v)
    {
        Intent in=new Intent(this,Register.class);
        startActivity(in);
    }
}
