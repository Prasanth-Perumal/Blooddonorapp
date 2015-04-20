package com.blooddonorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import Utilities.alert;


public class bcSearch extends ActionBarActivity {
Spinner spn_bloodtype,spn_condition;
    String blood_type,condition;
    String[] bloodtype_array={"blood type","A+","B+","O+","AB+","A-","B-","O-","AB-"},condition_array={"condition","PMD-platelets"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcsearch);
        spn_bloodtype = (Spinner) findViewById(R.id.spin_bloodtype);
        spn_condition = (Spinner) findViewById(R.id.spin_condition);
        CustomAdapter adapter = new CustomAdapter(this, R.layout.layout_bclist, bloodtype_array, 0);
        CustomAdapter adapter1 = new CustomAdapter(this, R.layout.layout_bclist, condition_array, 0);
        spn_bloodtype.setAdapter(adapter);
        spn_condition.setAdapter(adapter1);
        spn_bloodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> aParentView,
                                       View aView, int aPosition, long anId) {
                blood_type = aParentView.getItemAtPosition(aPosition).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> anAdapterView) {
                // do nothing
            }
        });
        spn_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> aParentView,
                                       View aView, int aPosition, long anId) {
                condition = aParentView.getItemAtPosition(aPosition).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
public void searchdonors(View v)
{
       //Toast.makeText(getApplicationContext(),blood_type+condition,Toast.LENGTH_LONG).show();
    if(validate())
    {
        Intent in=new Intent(this,bcMapSearchresult.class);
        in.putExtra("bloodtype",blood_type);
        startActivity(in);

    }
}
    private Boolean validate()
    {
        if (blood_type.equals("blood type")||condition.equals("condition"))
        {
            new alert(this);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up searchbutton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class CustomAdapter extends ArrayAdapter<String> {

        private int hidingItemIndex;

        public CustomAdapter(Context context, int textViewResourceId, String[] objects, int hidingItemIndex) {
            super(context, textViewResourceId, objects);
            this.hidingItemIndex = hidingItemIndex;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = null;
            TextView tv = new TextView(getContext());
            tv.setPadding(15,5,0,0);
            if (position == hidingItemIndex) {
                tv.setVisibility(View.GONE);
                tv.setHeight(0);
                v = tv;
            }
            else {
                v = super.getDropDownView(position, null, parent);
            }
            return v;
        }
    }
    public void register(View v)
    {
        Intent in=new Intent(this,Register.class);
        startActivity(in);
    }
}
