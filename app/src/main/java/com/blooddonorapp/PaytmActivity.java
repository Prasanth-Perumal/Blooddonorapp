package com.blooddonorapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.Random;


public class PaytmActivity extends ActionBarActivity {
    public static String THEME = "merchant";
    public static String WEBSITE = "yksoftware";
    public static String PAYTM_CHANNEL_ID = "WAP";
    public static String INDUSTRY_TYPE_ID = "Retail";
    public static String PAYTM_CUSTOMER_ID = "ETYS13";
    public static String PAYTM_MERCHANT_kEY = "O@9#W9fOlm29up3&";
    public static String PAYTM_MERCHANT_ID = "sumjkE62398232705701";
    public static String PAYTM_REBOOKING_URL = "http://test.etravelsmart.com/etsAPI/bus/paytmReBooking.htm?etsno=ETSNO";
    public static String PAYTM_BASE_URL = "https://pguat.paytm.com/oltp-web/processTransaction";
    public static String PAYTM_RETURN_URL="http://test.etravelsmart.com/etsAPI/payuresponse.jsp";
    public static String PAYTM_REFUND_URL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/REFUND";
    public static String PAYTM_RETURN_URL_FOR_REFUND = "http://test.etravelsmart.com/etsAPI/bus/processRefund.htm";
    public static String PAYTM_STATUS_URL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/TXNSTATUS";

    //http://localhost:8080/bus/paytm-booking.htm

    private int randomInt = 0;
    private PaytmPGService Service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        Log.d("LOG", "onCreate of MainActivity");


        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000);

        Service = PaytmPGService.getStagingService();


        PaytmMerchant Merchant = new PaytmMerchant(PAYTM_MERCHANT_ID,
                PAYTM_CHANNEL_ID, INDUSTRY_TYPE_ID, WEBSITE, THEME,
                "http://test.etravelsmart.com/etsAPI/paytmCheckSumGenerator.jsp","http://test.etravelsmart.com/etsAPI/paytmCheckSumValidator.jsp");

        //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values




        PaytmOrder Order = new PaytmOrder(""+randomInt,PAYTM_CUSTOMER_ID, "100",
                "suryavathi@ibeesolutions.com", "9948972017");

        Service.initialize(Order, Merchant,null);
        Service.startPaymentTransaction(this,false,false,new PaytmPaymentTransactionCallback() {

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {

                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage, Bundle inResponse) {
                        Log.i("Error", "onTransactionFailure :" + inErrorMessage);
                    }

                    @Override
                    public void networkNotAvailable() {

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Log.i("Error", "clientAuthenticationFailed :" + inErrorMessage);
                    }

                    @Override
                    public void someUIErrorOccurred(String s) {

                    }

                    @Override
                    public void onErrorLoadingWebPage(int i, String s, String s2) {

                    }
                }
            );
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_paytm, menu);
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
}
