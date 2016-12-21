package com.example.aalloul.packets;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by aalloul on 03/07/16.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ItemFragment.OnListFragmentInteractionListener,
        OfferDetail.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        DatePickerFragment.TheListener, RegistrationFragment.RegistrationFragmentListener {

    // Map permission -- make sure 1 is always for position permission
    protected final int MAP_PERMISSION = 1;
    protected final int DROP_OFF_LOCATION_REQUEST = 2;
    protected final int PICK_UP_LOCATION_REQUEST = 3;
    protected final int USER_LOCATION_REQUEST = 4;

    protected boolean MAP_PERMISSION_GRANTED = false;
    private GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedPref;
    FusedLocationProviderApi fusedLocationProviderApi;
    private Location userLastLocation;
    private LatLng userLocation;
    private View mLayout;
    private MainFragment mainFragment;
    private RegistrationFragment registrationFragment;

    // Interaction with Backend
    protected static HashMap<String, String> buffered_delayed_data = new HashMap();
    private Backend backend = new Backend();

    //Interaction with the offer search activity
    private Intent searchIntent, searchPerformAction;
    public final static String SEARCH_SOURCE_CITY_EXTRA = "com.example.aalloul.packets.SEARCHCITY";
    public final static String SEARCH_SOURCE_COUNTRY_EXTRA = "com.example.aalloul.packets.SEARCHCOUNTRY";


    // Interaction with the new offer publishing
    private Intent postIntent;
    public final static String OFFER_SOURCE_CITY_EXTRA = "com.example.aalloul.packets.OFFERCITY";
    public final static String OFFER_SOURCE_COUNTRY_EXTRA = "com.example.aalloul.packets.OFFERCOUNTRY";

    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "OnCreate - Enter");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_listview);
        searchPerformAction = getIntent();

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        if (mGoogleApiClient == null && !isAlreadyRegistered()) {
            Log.i(LOG_TAG, "onCreate - mGoogleApiClient is null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } else {
            Log.i(LOG_TAG, "onCreate - mGoogleApiClient is not null");
        }

        if (savedInstanceState == null) {
            Log.i(LOG_TAG, "onCreate - savedInstanceState is null");
            FragmentTransaction fmg = getSupportFragmentManager().beginTransaction();
            if (isAlreadyRegistered()) {
                Log.i(LOG_TAG, "onCreate - user already registered");
                sharedPref.getString(getString(R.string.saved_user_city), "");

                mainFragment = MainFragment.newInstance("23423");
                fmg.add(R.id.mainActivity_ListView, mainFragment, "mainFragment");
            } else {
                Log.i(LOG_TAG, "onCreate - user not registered");
                registrationFragment = RegistrationFragment.newInstance(new LatLng(1.0, 2.0));
                fmg.add(R.id.mainActivity_ListView, registrationFragment, "MainFragment");
            }
            fmg.commit();
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

    private boolean isAlreadyRegistered() {
        Log.i(LOG_TAG, "isAlreadyRegistered - Enter");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        if (sharedPref.getString(getString(R.string.saved_user_firstname), "").equals("")) {
            Log.i(LOG_TAG,"isAlreadyRegistered - User first name not found so not registered");
            return false;
        }
        return true;
    }

    private void storeUserDetails() {
        Log.i(LOG_TAG, "storeUserDetails - Enter");
        // This will allow us to know how long before the user decides to register
        int n_prompts = sharedPref.getInt(getString(R.string.saved_user_npromptstoregister), 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_user_firstname),
                registrationFragment.get_user_firstname());
        editor.putString(getString(R.string.saved_user_picture),
                registrationFragment.get_user_picture());
        editor.putString(getString(R.string.saved_user_phonenumber),
                registrationFragment.get_user_phone_number());
        editor.putString(getString(R.string.saved_user_phonenumber),
                registrationFragment.get_user_phone_number());
        editor.putInt(getString(R.string.saved_user_npromptstoregister), n_prompts+1);
        // Location data
        HashMap<String, String> tmp = registrationFragment.get_user_detailed_location();
        editor.putString(getString(R.string.saved_user_address),tmp.get("address"));
        editor.putString(getString(R.string.saved_user_city),tmp.get("city"));
        editor.putString(getString(R.string.saved_user_state),tmp.get("state"));
        editor.putString(getString(R.string.saved_user_country),tmp.get("country"));
        editor.putString(getString(R.string.saved_user_postalcode),tmp.get("postalCode"));

        editor.commit();

        Log.i(LOG_TAG, "storeUserDetails - Exit");
    }

    private void retrieveUserDetails() {
        //TODO retrieve user details to pass on to MainFragment
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
        HashMap<String, String> tmp;
        LatLng latLng;

        // Get location updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        if (!checkPermission()) return;

        userLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (userLocation == null) {
            Log.i(LOG_TAG, "onConnected - Location not available yet");
            return;
        }

        if (registrationFragment == null) {
            return;
        }

        latLng = new LatLng(userLastLocation.getLatitude(), userLastLocation.getLongitude());
        try {
            tmp = getParsedLocation(latLng);
            registrationFragment.set_user_detailed_location(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "onConnected - Exit ");
    }


    protected boolean checkPermission() {
        Log.i(LOG_TAG, "Start checking permissions");
        boolean b = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        if (b) {
            Log.i(LOG_TAG, "Permission already granted");
            MAP_PERMISSION_GRANTED = true;
            return true;
        }

        Log.i(LOG_TAG, "Permission not granted");
        // Should we show an explanation?
        b = ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (!b) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION);
            return false;
        }

        Log.i(LOG_TAG, "Apparently we should show an explanation");
        Snackbar.make(findViewById(R.id.mainActivity_ListView),
                R.string.permission_position_explanation,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.permission_position_explanation_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                MAP_PERMISSION);
                    }
                }).show();
                Log.i(LOG_TAG, "explanation displayed");

        return false;
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
                    // permission denied, boo!
                    MAP_PERMISSION_GRANTED = false;
                }
                break;
            }

        }
        Log.i(LOG_TAG, "onRequestPermissionsResult - Exit");
    }


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
        if (mGoogleApiClient != null ) {
            mGoogleApiClient.connect();
        }
        super.onStart();
        Log.i(LOG_TAG, "onStart - Exit");
    }

    @Override
    public void onStop() {
        Log.i(LOG_TAG, "onStop - Enter");
        if (mGoogleApiClient != null ) mGoogleApiClient.disconnect();
        super.onStop();
        Log.i(LOG_TAG, "onStop - Exit");
    }

    @Override
    public void onPause() {
        Log.i(LOG_TAG, "onPause - Enter");
        super.onPause();
        stopLocationUpdates();
        Log.i(LOG_TAG, "onPause - Exit");
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient == null) return;

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
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
                .replace(R.id.mainActivity_ListView, detailsFragment, "OfferDetail")
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
    boolean checkMainFragmentInputs() {
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

        if (checkMainFragmentInputs()) {
            // TODO launch the search
        }


        Log.i(LOG_TAG, "onSearchButtonPressed - exit");
    }

    // Method for publish offer
    @Override
    public void onPostButtonPressed() {
        Log.i(LOG_TAG, "onPostNewOfferPressed - start");
        if (checkMainFragmentInputs()) {
            // TODO launch the search
        }
        Log.i(LOG_TAG, "onPostNewOfferPressed - exit");
    }


    @Override
    public void returnDate(String date) {
        Log.i(LOG_TAG, "returnDate - called");
        mainFragment.setDate(date);
    }

    @Override
    public void onPickUpLocationPressed() {
        Log.i(LOG_TAG, "onPickUpLocationPressed - Enter");
        launchPlaceAutoCompleteRequest(PICK_UP_LOCATION_REQUEST);
    }
    
    @Override
    public void onDropOffLocationPressed() {
        Log.i(LOG_TAG, "onDropOffLocationPressed - Enter");
        launchPlaceAutoCompleteRequest(DROP_OFF_LOCATION_REQUEST);
    }

    // Launches the request to Google Places
    protected void launchPlaceAutoCompleteRequest(int reqCode) {
        // TODO better handle the exception - e.g. accept input or check internet connectivity
        try {
            // Only show addresses
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, reqCode);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(LOG_TAG, "GooglePlayServicesRepairableException Exception");
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(LOG_TAG, "GooglePlayServicesNotAvailableException Exception");
        }
    }

    // Calls geo coder to get the city and country
    protected HashMap<String, String> getParsedLocation(LatLng latlong) throws IOException {
        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        addresses = geocoder.getFromLocation(latlong.latitude, latlong.longitude, 1);

        // If any additional address line present than only, check with max available address lines
        // by getMaxAddressLineIndex()
        HashMap<String, String> tmp = new HashMap<>();
        tmp.put("address",addresses.get(0).getAddressLine(0));
        tmp.put("city", addresses.get(0).getLocality());
        tmp.put("state",addresses.get(0).getAdminArea());
        tmp.put("country", addresses.get(0).getCountryName());
        tmp.put("postalCode", addresses.get(0).getPostalCode());

        return tmp;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DROP_OFF_LOCATION_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.i(LOG_TAG, "Place: " + place.getName());
                    if (mainFragment != null) {
                        try {
                            mainFragment.setDrop_off_location(getParsedLocation(place.getLatLng()));
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "onActivityResult - IOException from drop off location geocoder");
                            Log.e(LOG_TAG, e.toString());
                        }
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.i(LOG_TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;

            case PICK_UP_LOCATION_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.i(LOG_TAG, "Place: " + place.getName());
                    if (mainFragment != null) {
                        try {
                            mainFragment.setPickup_location(getParsedLocation(place.getLatLng()));
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "onActivityResult - IOException from pick up location geocoder");
                            Log.e(LOG_TAG, e.toString());
                        }
                        Log.i(LOG_TAG, "get Address" + place.getAddress());
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.i(LOG_TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;

            case USER_LOCATION_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.i(LOG_TAG, "Place: " + place.getName());
                    if (registrationFragment != null) {
                        try {
                            registrationFragment.set_user_detailed_location(
                                    getParsedLocation(place.getLatLng()));
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "onActivityResult - IOException from pick up location " +
                                    "geocoder");
                            Log.e(LOG_TAG, e.toString());
                        }
                        Log.i(LOG_TAG, "get Address" + place.getAddress());
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.i(LOG_TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
        }
    }


    @Override
    public void onUserPicturePressed() {
        // TODO handle user providing picture

    }

    @Override
    public void onUserLocationPressed() {
        Log.i(LOG_TAG, "onDropOffLocationPressed - Enter");
        launchPlaceAutoCompleteRequest(USER_LOCATION_REQUEST);
    }

    @Override
    public void onRegisterMePressed() {
        Log.i(LOG_TAG, "onRegisterMePressed - Enter");
        int i = registrationFragment.isInputOk();

        // At least one import detail is missing
        if (i == 0) {
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.registration_input_incomplete),
                    getResources().getString(R.string.okay));
            return;
        }

        // User location not provided and access not granted
        if (i == 1 && !MAP_PERMISSION_GRANTED) {
                Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                        getResources().getString(R.string.user_location_not_entered),
                        getResources().getString(R.string.okay));
            return;
        }

        // User location granted but not found yet
        if (i == 1 && MAP_PERMISSION_GRANTED) {
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.location_will_come_later),
                    getResources().getString(R.string.okay));
        }


        storeUserDetails();
        if (mainFragment == null) {
            // TODO pass on the user data
            mainFragment = MainFragment.newInstance("");
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, mainFragment, "MainFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRegisterLaterPressed() {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        // This will allow us to know how long before the user decides to register
        int n_prompts = sharedPref.getInt(getString(R.string.saved_user_npromptstoregister), 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_user_npromptstoregister), n_prompts + 1);
        editor.commit();

        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance("");
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, mainFragment, "MainFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, "onLocationChanged - Enter");
        Log.i(LOG_TAG, "onLocationChanged - location = " + location.toString());
        stopLocationUpdates();
        updateUserDetails(location);
    }

    protected void updateUserDetails(Location location) {
        // Called from the wrong place?
        if (registrationFragment == null) {
            Log.i(LOG_TAG, "updateUserDetails - This call is weird as registrationFragment is null");
            return;
        }
        // Location already known
        if (registrationFragment.get_user_location() != "") {
            Log.i(LOG_TAG, "updateUserDetails - User location is already known. Do nothing");
            return;
        }
        // Okay now we can use this new location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            HashMap<String, String> tmp = getParsedLocation(latLng);
            registrationFragment.set_user_detailed_location(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
