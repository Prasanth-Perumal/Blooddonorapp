package Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.blooddonorapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prasanth on 19/03/15.
 */
public class Geolocationserv
{
    String source;

    static String[] lat=new String[2];
    static String[] lang=new String[2];

    Context context;


    Geolocationserv(String source,  Context context)
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
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    private class Downloadsource extends AsyncTask<URL,Void,String> {

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
        protected void onPostExecute(String result) {
            try {
                Log.e("json",result);

               // Toast.makeText(context,result,Toast.LENGTH_LONG).show();
                JSONObject reader = new JSONObject(result);
                JSONArray jsonArray = reader.optJSONArray("results");
                JSONObject sys  = jsonArray.getJSONObject(0);
                JSONObject sys1  = sys.getJSONObject("geometry");
                JSONObject sys2  = sys1.getJSONObject("location");
                lat[0] = sys2.getString("lat");
                lang[0]=sys2.getString("lng");
                Toast.makeText(context,lat[0]+lang[0],Toast.LENGTH_SHORT).show();

            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }


}


