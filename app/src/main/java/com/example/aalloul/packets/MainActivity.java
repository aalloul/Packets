package com.example.aalloul.packets;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;


/**
 * Created by aalloul on 03/07/16.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ItemFragment.OnListFragmentInteractionListener,
OfferDetail.OnFragmentInteractionListener, buttonSearchOffer.onSearchButtonInteractionListener{

    // Map permission -- make sure 1 is always for position permission
    protected final int MAP_PERMISSION = 1;
    protected boolean MAP_PERMISSION_GRANTED = false;
    private GoogleApiClient mGoogleApiClient;
    private Location userLastLocation;
    private LatLng userLocation;
    private View mLayout;

    // Interaction with Backend
    protected static HashMap<String, String> buffered_delayed_data = new HashMap();
    private Backend backend = new Backend();

    //Interaction with the offer search activity
    private Fragment mySearchButtonFragment = buttonSearchOffer.newInstance();
    private Intent searchIntent, searchPerformAction;
    public final static String SEARCH_SOURCE_CITY_EXTRA = "com.example.aalloul.packets.SEARCHCITY";
    public final static String SEARCH_SOURCE_COUNTRY_EXTRA = "com.example.aalloul.packets.SEARCHCOUNTRY";

    // Interaction with the new offer publishing
    private Intent postIntent, postPerformAction;
    public final static String OFFER_SOURCE_CITY_EXTRA =  "com.example.aalloul.packets.OFFERCITY";
    public final static String OFFER_SOURCE_COUNTRY_EXTRA = "com.example.aalloul.packets.OFFERCOUNTRY";

    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "OnCreate - Enter");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_listview);
        searchPerformAction = getIntent();

        if (searchPerformAction.hasExtra(SearchOffer.SEARCH_PERFORM_SEARCH_ACTION)) {
            requestUpdateFromBackend(searchPerformAction);
        }

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            Log.i(LOG_TAG, "onCreate - mGoogleApiClient is null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } else {
            Log.i(LOG_TAG, "onCreate - mGoogleApiClient is not null");
        }

        // Insert ListView fragment


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainActivity_ListView, ItemFragment.newInstance(1), "Offers")
                    .add(R.id.mainActivity_SearchButton, mySearchButtonFragment, "search")
                    .commit();
        }


        Log.i(LOG_TAG, "OnCreate - Exit");
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(LOG_TAG, "onConnected - Enter ");
        checkPermission();
        Log.i(LOG_TAG, "onConnected - Exit ");
    }

    protected void checkPermission() {
        // TODO better handling of permissions
        Log.i(LOG_TAG, "Start checking permissions");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(LOG_TAG, "Permission not granted");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.i(LOG_TAG, "Apparently we should show an explanation");
                Snackbar.make(findViewById(R.id.mainActivity_ListView), R.string.permission_position_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.permission_position_explanation_ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MAP_PERMISSION);
                            }
                        })
                        .show();
                Log.i(LOG_TAG, "explanation displayed");
                if (MAP_PERMISSION_GRANTED) userLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

            } else {
                // No explanation needed, we can request the permission.
                Log.i(LOG_TAG, "No explanation needed");
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MAP_PERMISSION);
                if (MAP_PERMISSION_GRANTED) userLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
        }
        Log.i(LOG_TAG, "Permission already granted");
        MAP_PERMISSION_GRANTED = true;
        userLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult - Enter");
        switch (requestCode) {
            case MAP_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "onRequestPermissionsResult - Position permission granted");

                    // permission was granted, yay! Do your thing mama!
                    MAP_PERMISSION_GRANTED = true;
                } else {
                    Log.i(LOG_TAG, "onRequestPermissionsResult - Position permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(findViewById(R.id.map), R.string.explain_usage_search,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {//don't don anything
                                }
                            })
                            .show();
                    MAP_PERMISSION_GRANTED = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        Log.i(LOG_TAG, "onRequestPermissionsResult - Exit");
    }

//
//    protected void updatePosition(){
//        if (! MAP_PERMISSION_GRANTED) return;
//
//        Log.i(LOG_TAG, "updatePosition - Enter");
//
//        if (mGoogleApiClient != null) {
//            Log.i(LOG_TAG, "updatePosition - mGoogleApiClient is not null");
//            userLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                    mGoogleApiClient);
//        } else {
//            Log.i(LOG_TAG, "updatePosition - mGoogleApiClient is null");
//        }
//
//        if (userLastLocation != null) {
//            Log.i(LOG_TAG, "updatePosition - mLastLocation not null");
//            userLocation = new LatLng(userLastLocation.getLatitude(), userLastLocation.getLongitude());
//        } else {
//            Log.i(LOG_TAG,"updatePosition - userLocation is null");
//        }
//    }


    public void startSearchOffer(View view) {
        Log.i(LOG_TAG, "startSearchOffer - start");
        Log.i(LOG_TAG, "startSearchOffer - exit");

    }

    public void requestUpdateFromBackend(Intent searchPerformAction){
        Log.i(LOG_TAG, "requestUpdateFromBackend - start");
        Backend backend = new Backend();
        HashMap<String, String> qparams = new HashMap();

        qparams.put("destinationCity",searchPerformAction
                .getStringExtra(SearchOffer.SEARCH_DESTINATION_CITY_EXTRA));
        qparams.put("destinationCountry",searchPerformAction
                .getStringExtra(SearchOffer.SEARCH_DESTINATION_COUNTRY_EXTRA));
        qparams.put("sourceCity",searchPerformAction
                .getStringExtra(SearchOffer.SEARCH_SOURCE_CITY_EXTRA));
        qparams.put("sourceCountry",searchPerformAction
                .getStringExtra(SearchOffer.SEARCH_SOURCE_COUNTRY_EXTRA));
        backend.requestUpdate(qparams);

        Log.i(LOG_TAG, "requestUpdateFromBackend - exit");
    }
    // ************************************************************
    // Required by implement
    @Override
    public void onStart() {
        Log.i(LOG_TAG, "onStart - Enter");
        mGoogleApiClient.connect();
        super.onStart();
        Log.i(LOG_TAG, "onStart - Exit");
    }

    @Override
    public void onStop() {
        Log.i(LOG_TAG, "onStop - Enter");
        mGoogleApiClient.disconnect();
        super.onStop();
        Log.i(LOG_TAG, "onStop - Exit");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "onConnectionFailed - Enter");
        Log.i(LOG_TAG, "onConnectionFailed - Exit");

    }

    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "onConnectionSuspended - Enter");
        Log.i(LOG_TAG, "onConnectionSuspended - Exit");
    }


    @Override
    public void onListFragmentInteraction(String nameAndFirstName, String source_city,
                                          String source_country, String destination_city,
                                          String destination_country, String n_packets,
                                          String phone_number, String comments) {
        Log.i(LOG_TAG, "onListFragmentInteraction - Received a click - content is");
        Log.i(LOG_TAG, "onListFragmentInteraction - nameAndFirstName = " + nameAndFirstName);
        Log.i(LOG_TAG, "onListFragmentInteraction - source_city = " + source_city);

        final OfferDetail detailsFragment =
                OfferDetail.newInstance(nameAndFirstName, source_city, source_country,
                        destination_city, destination_country, n_packets,
                        phone_number, comments);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, detailsFragment, "rageComicDetails")
                .remove(mySearchButtonFragment)
                .addToBackStack(null)
                .commit();

    }


    // Method for offerDetail
    @Override
    public void onCallButtonPress(String phonenumber) {
        Log.i(LOG_TAG, "onCallButtonPress - Start");
        Log.i(LOG_TAG, "onCallButtonPress - Calling " + phonenumber);
        Uri number = Uri.parse("tel:" + phonenumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
        Log.i(LOG_TAG, "onCallButtonPress - Exit");
    }

    @Override
    public void onMessageButtonPress(String phonenumber) {
        Log.i(LOG_TAG, "onMessageButtonPress - Start");
        Log.i(LOG_TAG, "onMessageButtonPress - Calling " + phonenumber);
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phonenumber, null));

        startActivity(smsIntent);
        Log.i(LOG_TAG, "onMessageButtonPress - Exit");
    }

    // Method for search_button
    @Override
    public void onSearchButtonPressed() {
        Log.i(LOG_TAG, "onSearchButtonPressed - start");
        searchIntent = new Intent(this, SearchOffer.class);
        searchIntent.putExtra(SEARCH_SOURCE_CITY_EXTRA, "AMSTERDAM");
        searchIntent.putExtra(SEARCH_SOURCE_COUNTRY_EXTRA, "NETHERLANDS");
        startActivity(searchIntent);

        Log.i(LOG_TAG, "onSearchButtonPressed - exit");
    }

    // Method for post new offer button
    public void onPostNewOfferPressed() {
        Log.i(LOG_TAG, "onPostNewOfferPressed - start");
        postIntent = new Intent(this, PublishOffer.class);
        postIntent.putExtra(OFFER_SOURCE_CITY_EXTRA, "AMSTERDAM");
        postIntent.putExtra(OFFER_SOURCE_COUNTRY_EXTRA, "NETHERLANDS");
        startActivity(postIntent);

        Log.i(LOG_TAG, "onPostNewOfferPressed - exit");
    }
}
