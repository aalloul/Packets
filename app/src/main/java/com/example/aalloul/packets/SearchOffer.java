package com.example.aalloul.packets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchOffer extends AppCompatActivity implements DatePickerFragment.TheListener{
    // Logging
    private final static String LOG_TAG = SearchOffer.class.getSimpleName();

    // Button initialization
    private EditText srcCity;
    private EditText srcCountry;
    private EditText destCity;
    private EditText destCountry;
    private static String sourceCity = "sourceCity";
    private static String sourceCountry = "sourceCountry";

    private FloatingActionButton confirm, cancel;
    private Button date_picker_button;
    private String sendingDate= "Choose a date";
    private String dateForSearch;

    // Communication from MainActivity
    private Intent fromMainActivityIntent;

    // Communication to MainActivity (canceling search)
    private Intent toMainActivityIntent;
    public final static String SEARCH_SOURCE_CITY_EXTRA =
            "com.example.aalloul.packets.SearchOffer.SEARCHSOURCECITY";
    public final static String SEARCH_SOURCE_COUNTRY_EXTRA =
            "com.example.aalloul.packets.SearchOffer.SEARCHSOURCECOUNTRY";
    public final static String SEARCH_DESTINATION_CITY_EXTRA =
            "com.example.aalloul.packets.SearchOffer.SEARCHDESTINATIONCITY";
    public final static String SEARCH_DESTINATION_COUNTRY_EXTRA =
            "com.example.aalloul.packets.SearchOffer.SEARCHDESTINATIONCOUNTRY";
    public final static String SEARCH_DATE_EXTRA =
            "com.example.aalloul.packets.SearchOffer.SEARCHDATE";
    public final static String SEARCH_PERFORM_SEARCH_ACTION =
            "com.example.aalloul.packets.SearchOffer.PERFORMSEARCHACTION";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate - start");
        setContentView(R.layout.activity_search_offer);

        Log.i(LOG_TAG, "onCreate - Get the intent");
        fromMainActivityIntent = getIntent();
        sourceCity = fromMainActivityIntent.getStringExtra(MainActivity.SEARCH_SOURCE_CITY_EXTRA);
        sourceCountry = fromMainActivityIntent.getStringExtra(
                MainActivity.SEARCH_SOURCE_COUNTRY_EXTRA);

        Log.i(LOG_TAG, "onCreate - get the edit text");
        srcCity = (EditText) findViewById(R.id.sourceCity);
        srcCountry = (EditText) findViewById(R.id.sourceCountry);
        srcCity.setText(sourceCity);
        srcCountry.setText(sourceCountry);
        destCity = (EditText) findViewById(R.id.destCity);
        destCountry = (EditText) findViewById(R.id.destCountry);

        // Get the confirm button
        Log.i(LOG_TAG, "onCreate - Getting the confim button");
        confirm = (FloatingActionButton) findViewById(R.id.confirmSearch);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmSearch();
            }
        });


        // Get the cancel button
        Log.i(LOG_TAG, "onCreate - Getting the cancel button");
        cancel = (FloatingActionButton) findViewById(R.id.cancelSearch);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelSearch();
            }
        });

        // Get the date picker button
        Log.i(LOG_TAG, "onCreate - Getting the date picker");
        date_picker_button = (Button) findViewById(R.id.date_picker);
        date_picker_button.setText(sendingDate);

        Log.i(LOG_TAG, "onCreateView - exit");
    }

    public void showDatePickerDialog(View v) {
        Log.i(LOG_TAG, "showDatePickerDialog - start");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        Log.i(LOG_TAG, "showDatePickerDialog - end");
    }
    private void onConfirmSearch() {
        Log.i(LOG_TAG, "onConfirmSearch - start");
        Log.i(LOG_TAG, "onConfirmSearch - srcCity "+srcCity.getText()+"- srcCountry = "+
                srcCountry.getText());
        Log.i(LOG_TAG, "onConfirmSearch - destCity "+destCity.getText()+"- destCountry = "+
                destCountry.getText());
        Log.i(LOG_TAG, "onConfirmSearch - thedate = "+dateForSearch);


        if (dateForSearch == null ) {
            Log.i(LOG_TAG,"dateForSearch not set");
            Utilities.makeThesnack(findViewById(R.id.activity_search_offer),
                    getResources().getString(R.string.date_send_not_set),
                    getResources().getString(R.string.okay));
            return;
        }

        if (srcCity == null || srcCity.getText().toString().equals("")) {
            Log.i(LOG_TAG,"srcCity not set");
            Utilities.makeThesnack(findViewById(R.id.activity_search_offer),
                    getResources().getString(R.string.send_source_city),
                    getResources().getString(R.string.okay));
            return;
        }

        if (srcCountry == null || srcCountry.getText().toString().equals("")) {
            Log.i(LOG_TAG,"srcCountry not set");
            Utilities.makeThesnack(findViewById(R.id.activity_search_offer),
                    getResources().getString(R.string.send_source_country),
                    getResources().getString(R.string.okay));
            return;
        }

        if (destCity == null || destCity.getText().toString().equals("")) {
            Log.i(LOG_TAG,"destCity not set");
            Utilities.makeThesnack(findViewById(R.id.activity_search_offer),
                    getResources().getString(R.string.send_destination_city),
                    getResources().getString(R.string.okay));
            return;
        }

        if (destCountry == null || destCountry.getText().toString().equals("")) {
            Log.i(LOG_TAG,"destCountry not set");
            Utilities.makeThesnack(findViewById(R.id.activity_search_offer),
                    getResources().getString(R.string.send_destination_country),
                    getResources().getString(R.string.okay));
            return;
        }

        // Start the intent now everything's okay
        if (isNetworkOk()) {
            toMainActivityIntent = new Intent(this, MainActivity.class);
            toMainActivityIntent.putExtra(SEARCH_SOURCE_CITY_EXTRA, srcCity.getText().toString());
            toMainActivityIntent.putExtra(SEARCH_SOURCE_COUNTRY_EXTRA,
                    srcCountry.getText().toString());
            toMainActivityIntent.putExtra(SEARCH_DESTINATION_CITY_EXTRA,
                    destCity.getText().toString());
            toMainActivityIntent.putExtra(SEARCH_DESTINATION_COUNTRY_EXTRA,
                    destCity.getText().toString());
            toMainActivityIntent.putExtra(SEARCH_DATE_EXTRA,
                    dateForSearch.toString());
            toMainActivityIntent.putExtra(SEARCH_PERFORM_SEARCH_ACTION, true);
            startActivity(toMainActivityIntent);
        } else {
            Log.w(LOG_TAG, "onConfirmSearch - No network connection found");

            final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_search_offer),
                    getResources().getString(R.string.no_network_connectivity),
                    Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction(R.string.okay, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSettings(SearchOffer.this);
                }
            });
            snackbar.show();
        }
        Log.i(LOG_TAG, "onConfirmSearch - End");

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

    private void onCancelSearch() {
        Log.i(LOG_TAG, "onCancelSearch - start");
        toMainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(toMainActivityIntent);
        Log.i(LOG_TAG, "onCancelSearch - End");

    }


    @Override
    public void returnDate(String date) {
        Log.i(LOG_TAG, "returnDate - start");
        dateForSearch = date;

        // Clears the focus out of the various input fields
        srcCity.clearFocus();
        destCity.clearFocus();
        srcCountry.clearFocus();
        destCountry.clearFocus();

        // Hides the goddamn keyboard
        // TODO this might not work depending on 'recommendations' i.e. if destCity and destCity
        // are set by default
        if (!destCity.getText().toString().equals("")) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
        // Set the date as the content of the button
        date_picker_button.setText(dateForSearch);
        Log.i(LOG_TAG, "returnDate - dateForSearch = " + dateForSearch);

        Log.i(LOG_TAG, "returnDate - End");

    }
}


