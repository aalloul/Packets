package com.example.aalloul.packets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class PublishOffer extends AppCompatActivity implements DatePickerFragment.TheListener{

    //Logging
    private final static String LOG_TAG = PublishOffer.class.getSimpleName();

    // Button initialization
    private String srcCity, srcCountry;
    private EditText sendingFromCity, sendingFromCountry;
    private EditText sendingToCity, sendingToCountry;

    private FloatingActionButton  conFirmNewOffer, cancelNewOffer;
    private Button date_picker_button;
    private String sendingDate= "Choose a date";
    private String dateForPublication;

    // Communication from MainActivity
    private Intent fromMainActivityIntent;

    // Communication to MainActivity (canceling search)
    private Intent toMainActivityIntent;
    public final static String POST_SOURCE_CITY_EXTRA =
            "com.example.aalloul.packets.POSTOffer.POSTSOURCECITY";
    public final static String POST_SOURCE_COUNTRY_EXTRA =
            "com.example.aalloul.packets.POSTOffer.POSTSOURCECOUNTRY";
    public final static String POST_DESTINATION_CITY_EXTRA =
            "com.example.aalloul.packets.POSTOffer.POSTDESTINATIONCITY";
    public final static String POST_DESTINATION_COUNTRY_EXTRA =
            "com.example.aalloul.packets.POSTOffer.POSTDESTINATIONCOUNTRY";
    public final static String POST_DATE_EXTRA =
            "com.example.aalloul.packets.POSTOffer.POSTDATE";
    public final static String POST_PERFORM_POST_ACTION =
            "com.example.aalloul.packets.POSTOffer.PERFORMPOSTACTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate - start");
        setContentView(R.layout.activity_publish_offer);

        Log.i(LOG_TAG, "onCreate - get the Intent");
        fromMainActivityIntent = getIntent();
        srcCity = fromMainActivityIntent.getStringExtra(MainActivity.OFFER_SOURCE_CITY_EXTRA);
        srcCountry = fromMainActivityIntent.getStringExtra(MainActivity.OFFER_SOURCE_COUNTRY_EXTRA);

        Log.i(LOG_TAG, "onCreate - get the edit text");
        sendingFromCity = (EditText) findViewById(R.id.sendingFromCity);
        sendingFromCountry = (EditText) findViewById(R.id.sendingFromCountry);
        sendingFromCity.setText(srcCity);
        sendingFromCountry.setText(srcCountry);
        sendingToCity = (EditText) findViewById(R.id.sendingToCity);
        sendingToCountry = (EditText) findViewById(R.id.sendingToCountry);

        // Get the confirm button
        Log.i(LOG_TAG, "onCreate - Getting the confim button");
        conFirmNewOffer = (FloatingActionButton) findViewById(R.id.confirmNewOffer);
        conFirmNewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmNewOffer();
            }
        });

        // Get the cancel button
        Log.i(LOG_TAG, "onCreate - Getting the cancel button");
        cancelNewOffer = (FloatingActionButton) findViewById(R.id.cancelNewOffer);
        cancelNewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelNewOffer();
            }
        });


        // Get the date picker button
        Log.i(LOG_TAG, "onCreate - Getting the date picker");
        date_picker_button = (Button) findViewById(R.id.offerDatePicker);
        date_picker_button.setText(sendingDate);
    }

    private void onCancelNewOffer() {
        Log.i(LOG_TAG, "onCancelNewOffer - start");
        toMainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(toMainActivityIntent);
        Log.i(LOG_TAG, "onCancelNewOffer - End");
    }

    private void onConfirmNewOffer() {
        Log.i(LOG_TAG, "onConfirmNewOffer - start");

        if (dateForPublication == null) {
            Log.i(LOG_TAG, "dateForSearch not set");
            Utilities.makeThesnack(findViewById(R.id.activity_publish_offer),
                    getResources().getString(R.string.date_send_not_set),
                    getResources().getString(R.string.okay));
            return;
        }

        if (sendingFromCity == null || sendingFromCity.getText().toString().equals("")) {
            Log.i(LOG_TAG, "sendingFromCity not set");
            Utilities.makeThesnack(findViewById(R.id.activity_publish_offer),
                    getResources().getString(R.string.send_source_city),
                    getResources().getString(R.string.okay));
            return;
        }

        if (sendingFromCountry == null || sendingFromCountry.getText().toString().equals("")) {
            Log.i(LOG_TAG, "sendingFromCountry not set");
            Utilities.makeThesnack(findViewById(R.id.activity_publish_offer),
                    getResources().getString(R.string.send_source_country),
                    getResources().getString(R.string.okay));
            return;
        }

        if (sendingToCity == null || sendingToCity.getText().toString().equals("")) {
            Log.i(LOG_TAG, "sendingToCity not set");
            Utilities.makeThesnack(findViewById(R.id.activity_publish_offer),
                    getResources().getString(R.string.send_destination_city),
                    getResources().getString(R.string.okay));
            return;
        }

        if (sendingToCountry == null || sendingToCountry.getText().toString().equals("")) {
            Log.i(LOG_TAG, "sendingToCity not set");
            Utilities.makeThesnack(findViewById(R.id.activity_publish_offer),
                    getResources().getString(R.string.send_destination_country),
                    getResources().getString(R.string.okay));
            return;
        }

        // Start the intent now everything's okay
        if (isNetworkOk()) {
            toMainActivityIntent = new Intent(this, MainActivity.class);
            toMainActivityIntent.putExtra(POST_SOURCE_CITY_EXTRA, sendingFromCity.getText().toString());
            toMainActivityIntent.putExtra(POST_DESTINATION_CITY_EXTRA, sendingToCity.getText().toString());
            toMainActivityIntent.putExtra(POST_SOURCE_COUNTRY_EXTRA, sendingFromCountry.getText().toString());
            toMainActivityIntent.putExtra(POST_DESTINATION_COUNTRY_EXTRA, sendingToCountry.getText().toString());
            toMainActivityIntent.putExtra(POST_DATE_EXTRA, dateForPublication);
            toMainActivityIntent.putExtra(POST_PERFORM_POST_ACTION, true);
            Log.i(LOG_TAG, "onConfirmNewOffer - End");
            startActivity(toMainActivityIntent);
        } else {
            Log.w(LOG_TAG, "onConfirmNewOffer - No network connection found");

            final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_publish_offer),
                    getResources().getString(R.string.no_network_connectivity),
                    Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction(R.string.okay, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSettings(PublishOffer.this);
                }
            });
            snackbar.show();
        }
    }

    // Tries to open the settings
    public void goToSettings(AppCompatActivity activity) {
        try {
            Intent gpsOptionsIntent = new Intent(Intent.ACTION_MAIN);
            gpsOptionsIntent.setClassName("com.android.phone", "com.android.phone.Settings");
            startActivity(gpsOptionsIntent);
        } catch (Exception e) {
            Log.w(LOG_TAG,"goToSettings - could not go to Settings");
        } finally {
            return;
        }
    }

    private boolean isNetworkOk() {
        Log.i(LOG_TAG, "isNetworkOk - Enter");

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(LOG_TAG, "isNetworkOk - Network available");
            return true;
        } else {
            Log.w(LOG_TAG, "isNetworkOk - Network not available");
            return false;
        }
    }
    public void newOffershowDatePickerDialog(View v) {
        Log.i(LOG_TAG, "showDatePickerDialog - start");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        Log.i(LOG_TAG, "showDatePickerDialog - end");
    }

    @Override
    public void returnDate(String date) {
        // Most important stuff first
        dateForPublication = date;
        date_picker_button.setText(dateForPublication);

        // Clears the focus out of the various input fields
        sendingFromCity.clearFocus();
        sendingToCity.clearFocus();
        sendingToCountry.clearFocus();
        sendingFromCountry.clearFocus();

        // Hides the goddamn keyboard
        // TODO this might not work depending on 'recommendations' i.e. if destCity and destCity
        // are set by default
        if (!sendingToCity.getText().toString().equals("")) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}
