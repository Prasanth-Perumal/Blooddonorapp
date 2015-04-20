package Utilities;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * Created by Prasanth on 02/04/15.
 */
public class alert {
    public alert(Activity context)
    {
        new AlertDialog.Builder(context)
            .setTitle("Blood Cloud")
            .setMessage("Please fill in all the details")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("ok",null)
            .show();}
}
