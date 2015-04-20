package Utilities;

import android.app.Activity;
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
public class googledirections
{
    String source;
    String dest;
    public static String[] routes;
    public static String[] dist;
    Activity context;


    googledirections(String source, String dest, Activity context)
    {
        this.source=source;
        this.dest=dest;
        this.context=context;

    }

    public void sendaddress()
    {
        try {
            String address1 = "https://maps.googleapis.com/maps/api/directions/json?origin="+source+"&destination="+dest+"&alternatives=true"+ "&key=" + context.getResources().getString(R.string.server_key);
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
        @Override
        protected void onPostExecute(String result) {
            try {
                Log.e("json",result);
                try {
                    // Toast.makeText(context,result,Toast.LENGTH_LONG).show();
                    JSONObject reader = new JSONObject(result);
                    JSONArray jsonArray = reader.optJSONArray("routes");
                    routes = new String[jsonArray.length()];
                    dist = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sys = jsonArray.getJSONObject(i);
                        routes[i] = sys.getString("summary");
                        JSONArray jsonArray1 = sys.getJSONArray("legs");
                        JSONObject sys1 = jsonArray1.getJSONObject(0);
                        JSONObject sys2 = sys1.getJSONObject("distance");
                        dist[i] = sys2.getString("text");


                    }


                }
                    catch(Exception e)
                    {
                        Toast.makeText(context.getApplicationContext(),e.getCause().toString(),Toast.LENGTH_LONG);
                    }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public String[] getroute(){

        return routes;
    }
    public String[] getdist(){

        return dist;
    }

}


