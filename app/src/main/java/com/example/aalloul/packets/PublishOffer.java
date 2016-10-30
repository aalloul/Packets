package com.example.aalloul.packets;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        toMainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(toMainActivityIntent);
        Log.i(LOG_TAG, "onConfirmNewOffer - End");
    }

    public void newOffershowDatePickerDialog(View v) {
        Log.i(LOG_TAG, "showDatePickerDialog - start");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        Log.i(LOG_TAG, "showDatePickerDialog - end");
    }

    @Override
    public void returnDate(String date) {
        dateForPublication = date;
        date_picker_button.setText(dateForPublication);
    }
}
