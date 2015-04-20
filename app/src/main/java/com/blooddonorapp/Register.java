package com.blooddonorapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import DAL.Donordatasource;
import Model.User;
import Utilities.alert;


public class Register extends ActionBarActivity {
EditText edt_name,edt_mobile,edt_address,edt_bloodgroup;
    RadioGroup rg_type;
    CheckBox accept;
    Donordatasource dat;
    String name,mobile,address,bloodgroup,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       edt_name=(EditText) findViewById(R.id.edttxt_name);
         edt_mobile=(EditText) findViewById(R.id.edttxt_mobile);
         edt_address=(EditText) findViewById(R.id.edttxt_address);
        edt_bloodgroup=(EditText) findViewById(R.id.edttxt_bloodgroup);
         rg_type=(RadioGroup)findViewById(R.id.rg_type);
        accept=(CheckBox)findViewById(R.id.accept);
        dat=new Donordatasource(this);
        dat.open();




    }
public void registerme(View v)
{
    name=edt_name.getText().toString();
    mobile=edt_mobile.getText().toString();
    address=edt_address.getText().toString();
    Geolocationserv h=new Geolocationserv(address,this);
    h.sendaddress();
    bloodgroup=edt_bloodgroup.getText().toString();
    if(rg_type.getCheckedRadioButtonId()==R.id.radioButton_donor)
    {
        type="donor";
    }
    if(rg_type.getCheckedRadioButtonId()==R.id.radioButton_seeker)
    {
        type="seeker";
    }
    if(rg_type.getCheckedRadioButtonId()==R.id.radioButton_both)
    {
        type="both";
    }
    if(rg_type.getCheckedRadioButtonId()== -1)
    {
        type="error";
    }



}
public Boolean Validate()
{
if(name==""){return false;}
    if(mobile==""){return false;}
    if(address==""){return false;}
    if(bloodgroup==""){return false;}
    if(type=="error"){return false;}
    return true;
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public class Geolocationserv
    {
        String source;

       String[] lat=new String[2];
         String[] lang=new String[2];

        Activity context;


        Geolocationserv(String source,  Activity context)
        {
            this.source=source;
            this.context=context;

        }
        public void sendaddress()
        {
            try {
                String address1 = context.getResources().getString(R.string.url) + source + "&key=" + context.getResources().getString(R.string.server_key);
                URL url1 = new URL(address1);
                new Downloadsource().execute(url1);
            }
            catch (Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

        private class Downloadsource extends AsyncTask<URL,Void,String> {
            private ProgressDialog progressDialog;
            @Override
            protected String doInBackground(URL... params) {
                try {

                    HttpURLConnection conn = (HttpURLConnection) params[0].openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStreamReader r=new InputStreamReader(conn.getInputStream());
                    BufferedReader b=new BufferedReader(r);
                    String line,result="";
                    while ((line=b.readLine())!=null) {
                        result += line+"\n";
                    }

                    return(result);
                }
                catch(Exception e)
                {
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }
                return "{";
            }
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Fetching address location, Please wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
            protected void onPostExecute(String result) {
                try {
                    Log.e("json", result);
                    progressDialog.hide();;
                    // Toast.makeText(context,result,Toast.LENGTH_LONG).show();
                    JSONObject reader = new JSONObject(result);
                    JSONArray jsonArray = reader.optJSONArray("results");
                    JSONObject sys  = jsonArray.getJSONObject(0);
                    JSONObject sys1  = sys.getJSONObject("geometry");
                    JSONObject sys2  = sys1.getJSONObject("location");
                    lat[0] = sys2.getString("lat");
                    lang[0]=sys2.getString("lng");
                    Toast.makeText(context,lat[0]+lang[0],Toast.LENGTH_SHORT).show();
                    if(!Validate())
                    {
                        new alert(context);
                    }
                    else{
                        User a=dat.createDonor(name,mobile,Double.parseDouble(lang[0]),Double.parseDouble(lat[0]),address,bloodgroup);
                        Toast.makeText(context,a.toString(),Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }


    }



}
