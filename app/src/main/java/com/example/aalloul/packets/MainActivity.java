package com.example.aalloul.packets;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by aalloul on 03/07/16.
 */
class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ItemFragment.OnListFragmentInteractionListener,
OfferDetail.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        DatePickerFragment.TheListener{

    // Map permission -- make sure 1 is always for position permission
    protected final int MAP_PERMISSION = 1;
    protected boolean MAP_PERMISSION_GRANTED = false;
    private GoogleApiClient mGoogleApiClient;
    private Location userLastLocation;
    private LatLng userLocation;
    private View mLayout;
    private MainFragment mainFragment;

    // Interaction with Backend
    protected static HashMap<String, String> buffered_delayed_data = new HashMap();
    private Backend backend = new Backend();

    //Interaction with the offer search activity
    private Intent searchIntent, searchPerformAction;
    public final static String SEARCH_SOURCE_CITY_EXTRA = "com.example.aalloul.packets.SEARCHCITY";
    public final static String SEARCH_SOURCE_COUNTRY_EXTRA = "com.example.aalloul.packets.SEARCHCOUNTRY";

    // Interaction with the new offer publishing
    private Intent postIntent;
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


        // Load the fragments
        mainFragment = MainFragment.newInstance("23423");
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentMain,mainFragment, "MainFragment")
                    .commit();
        }


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String queryResult = intent.getStringExtra("queryResult");
                handleNetworkResult(queryResult);
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter("newDataHasArrived"));

        Log.i(LOG_TAG, "OnCreate - Exit");
    }

    private void postNewOfferToBackend(Intent searchPerformAction) {
        Log.i(LOG_TAG, "postNewOfferToBackend - start");
        Backend backend = new Backend();
        HashMap<String, String> qparams = new HashMap();

        BackendInteraction.startActionQueryDB(this, qparams, "POST");

        Log.i(LOG_TAG, "postNewOfferToBackend - exit");
    }

    private void handleNetworkResult(String queryResult) {
        if (queryResult.equals("ok")) {
            ItemFragment.myItemRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            // TODO display error
        }
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
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
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


    public void requestUpdateFromBackend(Intent searchPerformAction){
        Log.i(LOG_TAG, "requestUpdateFromBackend - start");
        Backend backend = new Backend();
        HashMap<String, String> qparams = new HashMap();
        BackendInteraction.startActionQueryDB(this, qparams, "GET");

        Log.i(LOG_TAG, "requestUpdateFromBackend - exit");
    }



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

    private boolean isNetworkOk() {
        Log.i(LOG_TAG, "isNetworkOk - Enter");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(LOG_TAG, "isNetworkOk - Network available");
            return true;
        } else {
            Log.w(LOG_TAG, "isNetworkOk - Network not available");
            return false;
        }
    }

    // Tries to open the settings 
    public void goToSettings(AppCompatActivity activity) {
        try {
            Intent gpsOptionsIntent = new Intent(Intent.ACTION_MAIN);
            gpsOptionsIntent.setClassName("com.android.phone", "com.android.phone.Settings");
            startActivity(gpsOptionsIntent);
        } catch (Exception e) {
            Log.w(LOG_TAG, "goToSettings - could not go to Settings");
        }
    }

    // Checks the inputs from the Main Activity
    boolean checkInputs() {
        if (!isNetworkOk()) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.no_network_connectivity),
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.okay, new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    goToSettings(MainActivity.this);
                }
            });
            snackbar.show();
            return false ;
        }

        String pickuplocation = mainFragment.getPickupLocation();
        if (pickuplocation == null || pickuplocation.equals("")) {
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.pickup_location_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }

        Log.i(LOG_TAG, "pick up location = "+ pickuplocation);

        String dateforpickup = mainFragment.getDateForPickUp();
        if ( dateforpickup == null) {
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.date_send_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "date of pickup= "+ dateforpickup);

        String numberPackages = mainFragment.getNumberPackages();
        if ( numberPackages == null) {
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.size_packages_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "Number of Pakcages = "+ numberPackages);

        String sizePackages = mainFragment.getSizePackage();
        if ( sizePackages == null) {
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.number_packages_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "Number of Pakcages = "+ numberPackages);


        String dropOffLocation = mainFragment.getDropOffLocation();
        if ( dropOffLocation == null || dropOffLocation.equals("")) {
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.dropoff_location_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "Drop off location = "+ dropOffLocation);



        return true;
    }

    // Method for search_button
    @Override
    public void onSearchButtonPressed() {
        Log.i(LOG_TAG, "onSearchButtonPressed - start");

        if (checkInputs()) {
            // TODO launch the search
        }


        Log.i(LOG_TAG, "onSearchButtonPressed - exit");
    }

    // Method for publish offer
    @Override
    public void onPostButtonPressed() {
        Log.i(LOG_TAG, "onPostNewOfferPressed - start");
        if (checkInputs()) {
            // TODO launch the search
        }
        Log.i(LOG_TAG, "onPostNewOfferPressed - exit");
    }


    @Override
    public void returnDate(String date) {
        Log.i(LOG_TAG, "returnDate - called");
        mainFragment.setDate(date);
    }


}
