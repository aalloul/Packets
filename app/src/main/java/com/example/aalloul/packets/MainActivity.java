package com.example.aalloul.packets;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ItemFragment.OnListFragmentInteractionListener,
        OfferDetail.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        DatePickerFragment.TheListener, RegistrationFragment.RegistrationFragmentListener,
        ConfirmPublish.OnCofirmPublishListener, CameraOrGalleryDialog.CameraOrGalleryInterface{

    protected final int MAP_PERMISSION = 1;
    protected final int DROP_OFF_LOCATION_REQUEST = 2;
    protected final int PICK_UP_LOCATION_REQUEST = 3;
    protected final int USER_LOCATION_REQUEST = 4;
    static final int REQUEST_IMAGE_CAPTURE = 5;
    static final int REQUEST_WRITE_FILES_FROM_CAMERA = 6;
    static final int REQUEST_WRITE_FILES_FROM_GALLERY = 7;
    static final int REQUEST_PICK_PICTURE= 8;
    static boolean PICTURE_FOR_CONFIRM=false;

    private HashMap<String, String> u0 = new HashMap<>();


    protected final int PICKUP_AIM = 1;
    protected final int DROPOFF_AIM = 2;
    protected final int REGISTRATION_AIM = 3;
    protected final int UPDATE_STORED_LOCATION_AIM = 4;

    protected final long SESSION_ID = Utilities.CurrentTimeMS();
    protected long MAIN_ACTIVITY_START_TIME;

    private String mCurrentPhotoPath;
    SharedPreferences sharedPref;
    protected boolean MAP_PERMISSION_GRANTED = false;
    private GoogleApiClient mGoogleApiClient;
    private MainFragment mainFragment;
    private RegistrationFragment registrationFragment;
    private ConfirmPublish confirmPublish;
    private ThankYou thankYou;
    private ItemFragment itemFragment;
    private OfferDetail detailsFragment;

    // Interaction with Backend
    protected static HashMap<String, String> buffered_delayed_data = new HashMap<String, String>();
    private BackendInteraction backend = new BackendInteraction();

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

    private boolean isAlreadyRegistered() {
        Log.i(LOG_TAG, "isAlreadyRegistered - Enter");
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        if (sharedPref.getString(getString(R.string.saved_user_firstname), "").equals("")) {
            Log.i(LOG_TAG,"isAlreadyRegistered - User first name not found so not registered");
            return false;
        }
        Log.i(LOG_TAG, "userFirstName = " +
                sharedPref.getString(getString(R.string.saved_user_firstname), ""));
        return false;
    }

    protected HashMap<String, String> getUserDetailedLocation() {
        Log.i(LOG_TAG, "getUserDetailedLocation - Enter");
        HashMap<String, String> tmp = new HashMap<>();
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        tmp.put(getString(R.string.saved_user_postalcode),
                sharedPref.getString(getString(R.string.saved_user_postalcode), ""));
        tmp.put(getString(R.string.saved_user_city),
                sharedPref.getString(getString(R.string.saved_user_city), "Updating"));

        tmp.put(getString(R.string.saved_user_country),
                sharedPref.getString(getString(R.string.saved_user_country), ""));
        tmp.put(getString(R.string.saved_user_state),
                sharedPref.getString(getString(R.string.saved_user_state), ""));
        tmp.put(getString(R.string.saved_user_address),
                sharedPref.getString(getString(R.string.saved_user_address), ""));
        tmp.put(getString(R.string.saved_user_latitude),
                sharedPref.getString(getString(R.string.saved_user_latitude), ""));
        tmp.put(getString(R.string.saved_user_longitude),
                sharedPref.getString(getString(R.string.saved_user_longitude), ""));
        Log.i(LOG_TAG, "getUserDetailedLocation - Exit");
        return tmp;
    }

    /**
     * This function reads the data entered in the registration fragment
     * and returns a hashmap
     * @param nextFrag is used by the data reporting to know what the next fragment is
     * @return a hashmap to send to the back-end
     */

    private HashMap<String, String> storeUserDetails(String nextFrag) {
        Log.i(LOG_TAG, "storeUserDetails - Enter");
        HashMap<String, String> tmp = registrationFragment.getBlob(nextFrag);
        storeUserDetails(tmp);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        int n_prompts = sharedPref.getInt(getString(R.string.saved_user_npromptstoregister), 0);
        tmp.put(getString(R.string.nprompts), Integer.toString(n_prompts));
        Log.i(LOG_TAG, "storeUserDetails - Exit");
        return tmp;
    }

    private void storeUserDetails(HashMap<String, String> details) {
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        int n_prompts = sharedPref.getInt(getString(R.string.saved_user_npromptstoregister), 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getString(R.string.saved_user_npromptstoregister), n_prompts+1);
        Log.i(LOG_TAG, "storeUserDetails(details) - saved_user_firstname = "+
                details.get(getString(R.string.saved_user_firstname)));
        Log.i(LOG_TAG, "storeUserDetails(details) - saved_user_firstname = "+
                details.get(getString(R.string.saved_user_surname)));

        for (Map.Entry<String, String> entry : details.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }

        editor.apply();
    }

    private void updateStoredLocation(Location location) {
        HandleGeoCodingAsync handlegeocoding =
                new HandleGeoCodingAsync(this, UPDATE_STORED_LOCATION_AIM);
        handlegeocoding.execute(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    private void handleNetworkResult(String queryResult) {
        if (queryResult.equals("ok")) {
            ItemFragment.myItemRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            Log.i(LOG_TAG, "handleNetworkResult - An error happened with the network");
        }
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

    protected void updateUserDetails(Location location) {
        // Called from the wrong place?
        if (registrationFragment == null) {
            Log.i(LOG_TAG, "updateUserDetails - This call is weird as registrationFragment is null");
            return;
        }

        // Location already known
//        if (registrationFragment.get_user_location() != "") {
//            Log.i(LOG_TAG, "updateUserDetails - User location is already known. Do nothing");
//            return;
//        }
        // Okay now we can use this new location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        HandleGeoCodingAsync handlegeocoding = new HandleGeoCodingAsync(this, REGISTRATION_AIM);
        handlegeocoding.execute(latLng);
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
        MAP_PERMISSION_GRANTED = false;

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

    protected boolean checkFilePermission(final int which) {

        Log.i(LOG_TAG, "checkFilePermission - Start checking permissions");
        Log.i(LOG_TAG, "checkFilePermission - which = "+which);

        boolean b = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (b) {
            Log.i(LOG_TAG, "Permission already granted");
            return true;
        }

        Log.i(LOG_TAG, "Permission not granted");
        // Should we show an explanation?
        b = ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!b) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, which);
            return false;
        }

        Log.i(LOG_TAG, "Apparently we should show an explanation");
        Snackbar.make(findViewById(R.id.mainActivity_ListView),
                R.string.permission_position_explanation,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.permission_position_explanation_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(LOG_TAG, "Snackbar - which = " + which);
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                which);
                    }
                }).show();
        Log.i(LOG_TAG, "explanation displayed");

        return false;
    }

    public void requestUpdateFromBackend(Intent searchPerformAction){
        Log.i(LOG_TAG, "requestUpdateFromBackend - start");
        Backend backend = new Backend();
        HashMap<String, String> qparams = new HashMap();
        BackendInteraction.startActionQueryDB(this, qparams, "GET");

        Log.i(LOG_TAG, "requestUpdateFromBackend - exit");
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient == null) return;

        if (!mGoogleApiClient.isConnected()) return;
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    // Tries to open the settings 
    public void goToSettings() {
        try {
            Intent gpsOptionsIntent = new Intent(Intent.ACTION_MAIN);
            gpsOptionsIntent.setClassName("com.android.phone", "com.android.phone.Settings");
            startActivity(gpsOptionsIntent);
        } catch (Exception e) {
            Log.w(LOG_TAG, "goToSettings - could not go to Settings");
        }
    }

    // Checks the inputs from the Main Activity
    @SuppressWarnings("unchecked")
    boolean checkMainFragmentInputs(String action) {

        if (mainFragment == null) {
            Log.i(LOG_TAG, "checkMainFragmentInputs - mainFragment is null");
            return false;
        }

        if (!isNetworkOk()) {
            // Report error
            new HandleReportingAsync().execute(u0,mainFragment.mainFragmentError(
                    getString(R.string.ferror),getString(R.string.no_network_error), action));

            final Snackbar snackbar = Snackbar.make(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.no_network_connectivity),
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.okay, new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    goToSettings();
                }
            });
            snackbar.show();
            return false ;
        }

        HashMap<String, String> pickuplocation = mainFragment.getPickupLocation();

        if (pickuplocation == null ||
                !pickuplocation.containsKey(getString(R.string.saved_user_city))) {

            // Report error
            new HandleReportingAsync().execute(u0,mainFragment.mainFragmentError(
                    getString(R.string.ferror),getString(R.string.no_pickup_location), action));

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.pickup_location_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }

        Log.i(LOG_TAG, "checkMainFragmentInputs- pick up location = "+ pickuplocation);

        String dateforpickup = mainFragment.getDateForPickUp();
        if ( dateforpickup == null) {

            // Report error
            new HandleReportingAsync().execute(u0,mainFragment.mainFragmentError(
                    getString(R.string.ferror),getString(R.string.no_pickup_date), action));

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.date_send_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "checkMainFragmentInputs- date of pickup= "+ dateforpickup);

        String numberPackages = mainFragment.getNumberPackages();
        if ( numberPackages == null) {

            // Report error
            new HandleReportingAsync().execute(u0,mainFragment.mainFragmentError(
                    getString(R.string.ferror),getString(R.string.n_packages), action));

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.size_packages_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "checkMainFragmentInputs- Number of Pakcages = "+ numberPackages);

        String sizePackages = mainFragment.getSizePackage();
        if (sizePackages == null) {

            // Report error
            new HandleReportingAsync().execute(u0,mainFragment.mainFragmentError(
                    getString(R.string.ferror),getString(R.string.s_packages), action));

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.number_packages_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "checkMainFragmentInputs- Number of sizePackages = "+ sizePackages);


        HashMap<String, String> dropOffLocation = mainFragment.getDropOffLocation();

        if ( dropOffLocation == null ||
                !dropOffLocation.containsKey(getString(R.string.saved_user_city)) ) {

            // Report error
            new HandleReportingAsync().execute(u0,mainFragment.mainFragmentError(
                    getString(R.string.ferror),getString(R.string.no_dropoff_location), action));

            Log.i(LOG_TAG, "checkMainFragmentInputs- Unbelievable!");
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.dropoff_location_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        Log.i(LOG_TAG, "checkMainFragmentInputs- Drop off location = "+ dropOffLocation);



        return true;
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

    // Returns user personal details
    protected HashMap<String, String> getUserPersonalDetails() {
        HashMap<String, String> dets = new HashMap<>();
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        dets.put(getString(R.string.saved_user_firstname),
                sharedPref.getString(getString(R.string.saved_user_firstname), ""));
        dets.put(getString(R.string.saved_user_surname),
                sharedPref.getString(getString(R.string.saved_user_surname), ""));
        dets.put(getString(R.string.saved_user_phonenumber),
                sharedPref.getString(getString(R.string.saved_user_phonenumber), ""));
        dets.put(getString(R.string.saved_user_picture),
                sharedPref.getString(getString(R.string.saved_user_picture), ""));
        dets.put(getString(R.string.saved_user_picture_path),
                sharedPref.getString(getString(R.string.saved_user_picture_path),""));

        return dets;

    }

    // USed by dispatchTakePictureIntent to get the file path where to store the picture
    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = getString(R.string.user_picture_file);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Creates the intent for the user to take a picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                sharedPref.edit()
                        .putString(getString(R.string.saved_user_picture_path), mCurrentPhotoPath)
                        .commit();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(LOG_TAG, "dispatchTakePictureIntent - IOException happened");
                //TODO handle this exception
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.aalloul.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Creates the intent for the user to pick a picture from gallery
    private void dispatchGetPictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,REQUEST_PICK_PICTURE );
    }

    /* ***************** ***************** ***************** *****************
                        Override section
       ***************** ***************** ***************** ***************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "OnCreate - Enter");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_listview);

        MAIN_ACTIVITY_START_TIME = Utilities.CurrentTimeMS();

        searchPerformAction = getIntent();


        // This piece is to check whether the user is already registered with his city being
        // Updating
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        HashMap<String, String> userDetailedLocation = getUserDetailedLocation();
        String firstname = getUserPersonalDetails().get(getString(R.string.saved_user_firstname));
        String userCity = userDetailedLocation.get(getString(R.string.saved_user_city));
        Log.i(LOG_TAG, "onCreate - userCity = " + userCity);
        boolean isalreadyregistered = isAlreadyRegistered();

        // if not already registered or the city is "updating" then connect to Google API
        if (mGoogleApiClient == null && (!isalreadyregistered || userCity.equals("Updating"))) {
            Log.i(LOG_TAG, "onCreate - mGoogleApiClient is null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } else {
            Log.i(LOG_TAG, "onCreate - mGoogleApiClient is not null");
            mGoogleApiClient.connect();
        }

        if (savedInstanceState == null) {

            Log.i(LOG_TAG, "onCreate - savedInstanceState is null");
            FragmentTransaction fmg = getSupportFragmentManager().beginTransaction();
            if (isalreadyregistered) {
                Log.i(LOG_TAG, "onCreate - user already registered");
                mainFragment = MainFragment.newInstance(userDetailedLocation, firstname);
                fmg.add(R.id.mainActivity_ListView, mainFragment, "mainFragment");
            } else {
                Log.i(LOG_TAG, "onCreate - user not registered");
                registrationFragment = RegistrationFragment.newInstance();
                fmg.add(R.id.mainActivity_ListView, registrationFragment, "registrationFragment");
            }
            fmg.commit();
        } else {
            Log.i(LOG_TAG, "OnCreate - savedInstanceState not null");
            registrationFragment = (RegistrationFragment)
                    getSupportFragmentManager().findFragmentByTag("registrationFragment");

            confirmPublish = (ConfirmPublish)
                    getSupportFragmentManager().findFragmentByTag("confirmPublish");
            detailsFragment = (OfferDetail)
                    getSupportFragmentManager().findFragmentByTag("detailsFragment");
            itemFragment = (ItemFragment)
                    getSupportFragmentManager().findFragmentByTag("itemFragment");
            mainFragment = (MainFragment)
                    getSupportFragmentManager().findFragmentByTag("mainFragment");
        }

        // New data from the back-end was downloaded
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


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(LOG_TAG, "onConnected - Enter ");
        LatLng latLng;

        if (!checkPermission()) return;
        // Get location updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.i(LOG_TAG, "onConnected - Requested location updates");

        if (registrationFragment == null) {
            Log.i(LOG_TAG, "onConnected - Registration Fragment is null");
            return;
        }

        Location userLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i(LOG_TAG, "onConnected - Requested last location");

        if (userLastLocation == null) {
            Log.i(LOG_TAG, "onConnected - userLastLocation is null");
            registrationFragment.set_user_detailed_location(new HashMap<String, String>());
            Log.i(LOG_TAG, "onConnected - Location not available yet");
            return;
        }

        latLng = new LatLng(userLastLocation.getLatitude(), userLastLocation.getLongitude());
        HandleGeoCodingAsync handlegeocoding = new HandleGeoCodingAsync(this, REGISTRATION_AIM);
        handlegeocoding.execute(latLng);

        Log.i(LOG_TAG, "onConnected - Exit ");
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult - Enter");
        Log.i(LOG_TAG, "onRequestPermissionsResult - requestCode = " + requestCode);

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
            case REQUEST_WRITE_FILES_FROM_CAMERA: {
                Log.i(LOG_TAG, "onRequestPermissionsResult - REQUEST_WRITE_FILES_FROM_CAMERA granted");
                dispatchTakePictureIntent();
                break;
            }
            case REQUEST_WRITE_FILES_FROM_GALLERY: {
                Log.i(LOG_TAG, "onRequestPermissionsResult - REQUEST_WRITE_FILES_FROM_GALLERY granted");
                dispatchGetPictureFromGallery();
                break;
            }
        }
        Log.i(LOG_TAG, "onRequestPermissionsResult - Exit");
    }

    @Override
    public void onResume() {
        Log.i(LOG_TAG, "onResume - enter");


        if (mGoogleApiClient == null ) {
            Log.i(LOG_TAG, "onResume - mGoogleApiClient is null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } else {
            Log.i(LOG_TAG, "onResume - mGoogleApiClient is NOT null");
            Log.i(LOG_TAG, "onResume - Connecting mGoogleApiClient ");
            mGoogleApiClient.connect();
        }
        super.onResume();

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
        DataBuffer.clear();
        Log.i(LOG_TAG, "onPause - Exit");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        if (registrationFragment != null && registrationFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "registrationFragment", registrationFragment);
        }
        if (mainFragment != null && mainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "mainFragment", mainFragment);
        }
        if (detailsFragment != null && detailsFragment.isAdded() ) {
            getSupportFragmentManager().putFragment(outState, "detailsFragment", detailsFragment);
        }
        if (confirmPublish != null && confirmPublish.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "confirmPublish", confirmPublish);
        }
        if (itemFragment != null && itemFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "itemFragment", itemFragment);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "onConnectionFailed - Enter");
        Log.i(LOG_TAG, "onConnectionFailed - Exit");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "onConnectionSuspended - Enter");
        Log.i(LOG_TAG, "onConnectionSuspended - Exit");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onListFragmentInteraction(String nameAndFirstName, String source_city,
                                          String source_country, String destination_city,
                                          String destination_country, String n_packets,
                                          String phone_number, String comments, int thepos) {
        Log.i(LOG_TAG, "onListFragmentInteraction - Received a click - content is");
        Log.i(LOG_TAG, "onListFragmentInteraction - nameAndFirstName = " + nameAndFirstName);
        Log.i(LOG_TAG, "onListFragmentInteraction - source_city = " + source_city);

        u0.put(getString(R.string.fName),"itemFragment");

        HashMap<String, String> u1 = new HashMap<>();
        long end_time = Utilities.CurrentTimeMS();
        u1.put(getString(R.string.fStart), Long.toString(itemFragment.getFragment_start_time()));
        u1.put(getString(R.string.fEnd), Long.toString(end_time));
        u1.put(getString(R.string.fDuration), Long.toString(
                end_time - itemFragment.getFragment_start_time()));
        u1.put(getString(R.string.n_results), Integer.toString(itemFragment.getN_results()));
        u1.put(getString(R.string.pos), Integer.toString(thepos));
        u1.put(getString(R.string.nextF), "detailsFragment");

        new HandleReportingAsync().execute(u0,u1);

        detailsFragment =
                OfferDetail.newInstance(nameAndFirstName, source_city, source_country,
                        destination_city, destination_country, n_packets,
                        phone_number, comments);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, detailsFragment, "detailsFragment")
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
    @SuppressWarnings("unchecked")
    public void onSearchButtonPressed() {
        Log.i(LOG_TAG, "onSearchButtonPressed - start");

        if (!checkMainFragmentInputs("SEARCH")) {
            Log.i(LOG_TAG, "onSearchButtonPressed - user did not provide any input");
            return;
        }

        u0.put(getString(R.string.fName),"mainFragment");
        HashMap<String, String> hashMap = mainFragment.getTripDetails("SEARCH","itemFragment");

        // Send data to reporting
        new HandleReportingAsync().execute(u0,hashMap);

        //TODO call the back-end here
        itemFragment = ItemFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_ListView, itemFragment, "itemFragment")
                .addToBackStack("mainToitem")
                .commit();


        Log.i(LOG_TAG, "onSearchButtonPressed - exit");
    }

    // Method for publish offer
    @Override
    @SuppressWarnings("unchecked")
    public void onPostButtonPressed() {
        Log.i(LOG_TAG, "onPostButtonPressed - start");

        u0.put(getString(R.string.fName),"mainFragment");

        // Check whether input data is correct
        if (!checkMainFragmentInputs("POST")) { return ;}

        // Gather all information
        HashMap<String, String> userDetails = getUserPersonalDetails();
        HashMap<String, String> hashMap = mainFragment.getTripDetails("POST", "confirmPublish");

        // Send data to reporting
        new HandleReportingAsync().execute(u0,hashMap);

        // Show confirmation page
        confirmPublish = ConfirmPublish.newInstance(userDetails,hashMap);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, confirmPublish, "confirmPublish")
                .addToBackStack("MainFragmentToConfirmPublish")
                .commit();
        Log.i(LOG_TAG, "onPostButtonPressed - exit");
    }

    @Override
    public void returnDate(String date) {
        Log.i(LOG_TAG, "returnDate - called");
        mainFragment.setDate(date);
    }

    @Override
    public void onPickUpLocationPressed() {
        Log.i(LOG_TAG, "onPickUpLocationPressed - Enter");
        mainFragment.setEdited_pickup_location();
        launchPlaceAutoCompleteRequest(PICK_UP_LOCATION_REQUEST);
    }
    
    @Override
    public void onDropOffLocationPressed() {
        Log.i(LOG_TAG, "onDropOffLocationPressed - Enter");
        launchPlaceAutoCompleteRequest(DROP_OFF_LOCATION_REQUEST);
    }

    // Handles the callback result when the user enters a location somewhere
    private void handleTripLocationRequest(int resultCode, Intent data, int requestAim) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            Log.i(LOG_TAG, "Place: " + place.getName());
            HandleGeoCodingAsync handlegeocoding;

            switch (requestAim) {
                case PICKUP_AIM:
                    handlegeocoding = new HandleGeoCodingAsync(this, PICKUP_AIM);
                    handlegeocoding.execute(place.getLatLng());
                    break;

                case DROPOFF_AIM:
                    handlegeocoding = new HandleGeoCodingAsync(this, DROPOFF_AIM);
                    handlegeocoding.execute(place.getLatLng());
                case REGISTRATION_AIM:
                    handlegeocoding = new HandleGeoCodingAsync(this, REGISTRATION_AIM);
                    handlegeocoding.execute(place.getLatLng());
            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            // TODO: Handle the error.
            Log.i(LOG_TAG, status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            Log.i(LOG_TAG, "handleTripLocationRequest - cancelled");
        }
    }

    // Handles the camera result
    private void handleUserCameraResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            File file = new File(mCurrentPhotoPath);
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Handle this asynchronously
                HandlePictureAsync handlepicture = new HandlePictureAsync();
                handlepicture.execute(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.w(LOG_TAG, "handleUserCameraResult - IOException");
                e.printStackTrace();
            }

        }
    }

    // handles the result after the user chose a photo from Gallery
    private void handleUserPickPicture(int resultCode, Intent data) {

        Log.i(LOG_TAG, "handleUserPickPicture - Enter");
        try {
            if (resultCode == RESULT_OK && null != data) {
                Log.i(LOG_TAG, "handleUserPickPicture - Result okay and data not null");
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                sharedPref.edit()
                        .putString(getString(R.string.saved_user_picture_path), mCurrentPhotoPath)
                        .commit();
                Log.i(LOG_TAG, "handleUserPickPicture - cursor closed");
                Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
                HandlePictureAsync handlepicture = new HandlePictureAsync();
                Log.i(LOG_TAG, "handleUserPickPicture - async handling of the picture");
                handlepicture.execute(bmp);
            }
        } catch (Exception e) {
            //TODO handle this exception
            Log.i(LOG_TAG, "handleUserPickPicture - Exception caught");
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DROP_OFF_LOCATION_REQUEST:
                Log.i(LOG_TAG, "onActivityResult - Handling drop off location result");
                handleTripLocationRequest(resultCode, data, DROPOFF_AIM);
                break;

            case PICK_UP_LOCATION_REQUEST:
                Log.i(LOG_TAG, "onActivityResult - Handling pick up location result");
                handleTripLocationRequest(resultCode, data, PICKUP_AIM);
                break;

            case USER_LOCATION_REQUEST:
                Log.i(LOG_TAG, "onActivityResult - Handling registration location result");
                handleTripLocationRequest(resultCode, data, REGISTRATION_AIM);
                break;

            case REQUEST_IMAGE_CAPTURE:
                Log.i(LOG_TAG, "onActivityResult - Handling picture taking by the user");
                handleUserCameraResult(resultCode);
                break;

            case REQUEST_PICK_PICTURE:
                Log.i(LOG_TAG, "onActivityResult - Handling picture chosen from Gallery");
                handleUserPickPicture(resultCode, data);
                break;
        }
    }

    @Override
    public void onUserPicturePressed() {
        Log.i(LOG_TAG, "onUserPicturePressed - Enter");
        try {
            FragmentManager fm = getSupportFragmentManager();
            CameraOrGalleryDialog dialogFragment = new CameraOrGalleryDialog();
            dialogFragment.show(fm, "Sample Fragment");
        } catch (Exception ex) {
            Log.i(LOG_TAG, "onUserPicturePressed - Exception caught");
            ex.printStackTrace();
        } finally {
            Log.i(LOG_TAG, "onUserPicturePressed - Exit");
        }
    }

    @Override
    public void onUserPicturePressed_Confirm() {
        PICTURE_FOR_CONFIRM=true;
        onUserPicturePressed();

    }

    @Override
    public void onUserLocationPressed() {
        Log.i(LOG_TAG, "onDropOffLocationPressed - Enter");
        registrationFragment.setEdited_location();
        launchPlaceAutoCompleteRequest(USER_LOCATION_REQUEST);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRegisterMePressed() {
        Log.i(LOG_TAG, "onRegisterMePressed - Enter");
        u0.put(getString(R.string.fName),"registration");


        if (registrationFragment == null ) {
            Log.i(LOG_TAG, "onRegisterMePressed - registrationFragment is null");
            registrationFragment = RegistrationFragment.newInstance();
        }
        int i = registrationFragment.isInputOk();

        // At least one import detail is missing
        if (i == 0) {
            new HandleReportingAsync().execute(u0,registrationFragment.getBlob(
                    getString(R.string.ferror),getString(R.string.reg_nopers)));
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.registration_input_incomplete),
                    getResources().getString(R.string.okay));
            return;
        }

        // User location not provided and access not granted
        if (i == 1 && !MAP_PERMISSION_GRANTED) {
            new HandleReportingAsync().execute(u0,registrationFragment.getBlob(
                    getString(R.string.ferror),getString(R.string.reg_noloc)));
                Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                        getResources().getString(R.string.user_location_not_entered),
                        getResources().getString(R.string.okay));
            return;
        }

        // User location granted but not found yet
        if (i == 1 && MAP_PERMISSION_GRANTED) {
            new HandleReportingAsync().execute(u0,registrationFragment.getBlob(
                    getString(R.string.ferror),getString(R.string.reg_locnotfound)));
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.location_will_come_later),
                    getResources().getString(R.string.okay));
        }

        // Store the details in SharedPref
        HashMap<String, String> tmp = storeUserDetails("mainFragment");
        // Report the new data
        new HandleReportingAsync().execute(u0,tmp);


        // Show a thank you note for 3 seconds
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_ListView,
                        RegistrationThanks.newInstance(
                                getUserPersonalDetails().get(
                                        getString(R.string.saved_user_firstname))), "regThank")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("RegisterToMainFragment")
                .commit();

        // And move on
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                if (mainFragment == null)
                {
                    Log.w(LOG_TAG, "onRegisterMePressed - mainFragment is null");
                    mainFragment = MainFragment.newInstance(getUserDetailedLocation(),
                            getUserPersonalDetails().get(getString(R.string.saved_user_firstname)));
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainActivity_ListView, mainFragment, "mainFragment")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack("RegisterToMainFragment")
                        .commit();
            }
        }.start();

    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRegisterLaterPressed() {
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        // This will allow us to know how long before the user decides to register
        int n_prompts = sharedPref.getInt(getString(R.string.saved_user_npromptstoregister), 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_user_npromptstoregister), n_prompts + 1);
        editor.commit();


        if (mainFragment == null) {
            Log.i(LOG_TAG, "onRegisterLaterPressed - mainFragment is null");
            mainFragment = MainFragment.newInstance(getUserDetailedLocation(),"");
        }

        u0.put(getString(R.string.fName),"registration");
        new HandleReportingAsync().execute(u0,registrationFragment.getBlob(
                getString(R.string.action),getString(R.string.reg_later)));


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, mainFragment, "mainFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, "onLocationChanged - Enter");
        Log.i(LOG_TAG, "onLocationChanged - location = " + location.toString());
//        stopLocationUpdates();
        if (isAlreadyRegistered()) {
            if (mainFragment != null ) {
                updateStoredLocation(location);
                mainFragment.setPickup_location(getUserDetailedLocation());
            }
        } else {
            updateUserDetails(location);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onConfirmPublish() {
        Log.i(LOG_TAG, "onConfirmPublish - Publishing new offer");

        u0.put(getString(R.string.fName),"confirmPublish");

        if (!confirmPublish.checkInputs()) {
            new HandleReportingAsync().execute(u0,registrationFragment.getBlob(
                    getString(R.string.ferror),getString(R.string.confirm_no_details)));

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.registration_input_incomplete),
                    getResources().getString(R.string.okay)
            );
            return;
        }

        new HandleReportingAsync().execute(u0,registrationFragment.getBlob("thankYou"));

        HashMap<String, String> tmp = confirmPublish.getAllDetails();
        /** This might need a bit more thinking -- For example, the user is temporarily in another
         * city/country but doesn't us to consider this place as his new home.
        **/
        storeUserDetails(tmp);

        //TODO post to backend
//        Log.i(LOG_TAG, "onConfirmPublish - confirmPublish " + tmp.toString());

        // Show a thank you note
        thankYou = ThankYou.newInstance(tmp.get(getString(R.string.saved_user_firstname)));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, thankYou, "Thank you")
                .commit();

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                if (mainFragment == null) {
                    mainFragment = MainFragment.newInstance(getUserDetailedLocation(),
                            getUserPersonalDetails().get(getString(R.string.saved_user_firstname)));
                }
                FragmentManager fm;
                fm = getSupportFragmentManager();
                int ec = fm.getBackStackEntryCount();
                Log.i(LOG_TAG, "onBackToMainFragment - ec = "+ec);
                int id = fm.getBackStackEntryAt(ec-1).getId();
                Log.i(LOG_TAG, "onBackToMainFragment - id = "+id);

                for (int i = 0; i > ec; i++) {
                    fm.popBackStack(i, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

                fm.beginTransaction()
                        .remove(thankYou)
                        .remove(mainFragment)
                        .commit();

                fm.beginTransaction()
                        .add(R.id.mainActivity_ListView, mainFragment, "mainFragment")
                        .commit();
            }
        }.start();


    }


    @Override
    public void onBackPressed()
    {
        //TODO check this is stable
        Log.i(LOG_TAG, "onBackPressed - start");
//        if (BACK_CALLED_AFTER_POSTING) {
//            BACK_CALLED_AFTER_POSTING = false;
//            Log.i(LOG_TAG, "onBackPressed - is instance");
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            return;
//        }
        FragmentTransaction fmg = getSupportFragmentManager().beginTransaction();
        if (thankYou != null && thankYou.isVisible()) {
            Log.i(LOG_TAG, "onBackPressed - is instance");
            // Detach the "thank you" to avoid displaying 2 fragments at the same time
            fmg.detach(thankYou);
            // Detaching the confirmation to avoid people re-publishing the same offer
            if (confirmPublish != null) fmg.detach(confirmPublish);
            fmg.commit();
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            return;
        }

        try {
            super.onBackPressed();
        } catch (Exception e) {
            Log.i(LOG_TAG, "STUCK STUCK STUCK STUCK ");
            // If exception, try to detach the mainFragment
            if (mainFragment != null ) fmg.detach(mainFragment);
            /* if we try to detach registrationFragment, then the back button will open a white
            screen which is ugly -- so better just leave it there
            fmg.detach(registrationFragment);
             */
            fmg.commit();
            // and try again
            super.onBackPressed();
        }
    }

    @Override
    public void onCameraOrGallerySelected(DialogInterface dialog, int which ) {
        Log.i(LOG_TAG, "onCameraOrGallerySelected - Enter");

        switch (which) {
            case 0:
                // Gallery chosen
                Log.i(LOG_TAG, "onCameraOrGallerySelected - choose from Gallery ");
                if (checkFilePermission(REQUEST_WRITE_FILES_FROM_GALLERY)) {
                    dispatchGetPictureFromGallery();
                }
                break;

            case 1:
                // Take a new picture
                Log.i(LOG_TAG, "onCameraOrGallerySelected - take a new picture");
                if (checkFilePermission(REQUEST_WRITE_FILES_FROM_CAMERA)) {
                    dispatchTakePictureIntent();
                }
                break;
        }
    }

    @Override
    public void OnPrivacyButtonPressed() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_ListView, PrivacyNotice.newInstance())
                .addToBackStack("toPrivacy")
                .commit();
    }

    @Override
    public void OnPrivacyButtonPressed2() {
        this.OnPrivacyButtonPressed();
    }

    /*
     This little class is called when the user takes a picture of himself. It crops, rotates if
     necessary and displays the picture on the registration page.
     */
    public class HandlePictureAsync extends AsyncTask<Bitmap, Void, Bitmap> {

        // Crops and scales images
        private Bitmap cropAndScale (Bitmap source,int scale){
            int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
            int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
            int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
            int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
            source = Bitmap.createBitmap(source, x, y, factor, factor);
            source = Bitmap.createScaledBitmap(source, scale, scale, false);
            return source;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            Bitmap bitmap = ExifUtil.rotateBitmap(mCurrentPhotoPath, params[0]);
            return cropAndScale(bitmap, 250);
        }


        protected void onPostExecute(Bitmap result) {
            if (PICTURE_FOR_CONFIRM && confirmPublish!=null){
                confirmPublish.setUserPicture(result);
                PICTURE_FOR_CONFIRM=false;
            }
            if (registrationFragment != null) {
                registrationFragment.setUserPicture(result);
                registrationFragment.unsetCaption_user_picture();
            }
        }
    }

    /*
     This little class is called to geo-encode the (latitude, longitude) of any place
      it's done as an AsyncTask to avoid blocking the main UI thread
     */
    public class HandleGeoCodingAsync extends AsyncTask<LatLng, Void, HashMap<String, String>> {
        private Context context;
        private int pickupaim;

        private HandleGeoCodingAsync(Context ctx, int pickupaim) {
            context = ctx;
            this.pickupaim = pickupaim;
        }

        @Override
        protected HashMap<String, String> doInBackground(LatLng... params) {
            LatLng latlong = params[0];
             Geocoder geocoder;
             List<android.location.Address> addresses;
             geocoder = new Geocoder(context, Locale.getDefault());

            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                addresses = geocoder.getFromLocation(latlong.latitude, latlong.longitude, 1);
                HashMap<String, String> tmp = new HashMap<>();
                tmp.put(getString(R.string.saved_user_address),addresses.get(0).getAddressLine(0));
                tmp.put(getString(R.string.saved_user_city), addresses.get(0).getLocality());
                tmp.put(getString(R.string.saved_user_state),addresses.get(0).getAdminArea());
                tmp.put(getString(R.string.saved_user_country), addresses.get(0).getCountryName());
                tmp.put(getString(R.string.saved_user_postalcode), addresses.get(0).getPostalCode());
                tmp.put(getString(R.string.saved_user_latitude),
                        Double.toString(addresses.get(0).getLatitude()));
                tmp.put(getString(R.string.saved_user_longitude),
                        Double.toString(addresses.get(0).getLongitude()));
                return tmp;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(HashMap<String, String> res) {
            switch (pickupaim) {
                case PICKUP_AIM:
                    if (mainFragment != null && mainFragment.isVisible()) {
                        mainFragment.setPickup_location(res);
                    }
                    break;

                case DROPOFF_AIM:
                    if (mainFragment != null && mainFragment.isVisible()) {
                        mainFragment.setDrop_off_location(res);
                    }
                    break;

                case REGISTRATION_AIM:
                    if (registrationFragment != null && registrationFragment.isVisible()) {
                        registrationFragment.set_user_detailed_location(res);
                    }
                    break;

                case UPDATE_STORED_LOCATION_AIM:
                    sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor  editor = sharedPref.edit();
                    editor.putString(getString(R.string.saved_user_address),
                            res.get(getString(R.string.saved_user_address)));
                    editor.putString(getString(R.string.saved_user_city),
                            res.get(getString(R.string.saved_user_city)));
                    editor.putString(getString(R.string.saved_user_state),
                            res.get(getString(R.string.saved_user_state)));
                    editor.putString(getString(R.string.saved_user_country),
                            res.get(getString(R.string.saved_user_country)));
                    editor.putString(getString(R.string.saved_user_postalcode),
                            res.get(getString(R.string.saved_user_postalcode)));
                    editor.commit();
            }

        }

    }

    /*
    This class is intended to fill the data buffer asynchronously.
     The why:
         the data reporting should not block the main thread
         all of the fragments live within an activity so it makes sense to centralize the reporting
         the activity knows which fragment is being called next
     */
    public class HandleReportingAsync extends AsyncTask<HashMap<String, String>, Void, Void> {

        @Override
        protected Void doInBackground(HashMap<String, String>... params) {
            HashMap<String, String> tmp = new HashMap<>();

            Gson gson = new Gson();
            tmp.put(getString(R.string.sID), Long.toString(SESSION_ID));
            tmp.put(getString(R.string.acStart), Long.toString(MAIN_ACTIVITY_START_TIME));

            tmp.put(getString(R.string.acName), "mainActivity");

            tmp.put(getString(R.string.fName), params[0].get(getString(R.string.fName)));

            if (params[0].get(getString(R.string.sEnd)) != null) {
                // If leaving the app
                long t = Utilities.CurrentTimeMS();
                tmp.put(getString(R.string.sEnd), Long.toString(t));
                tmp.put(getString(R.string.sDuration), Long.toString(t-SESSION_ID));
            } else {
                // If navigating from 1 fragment to another
                tmp.put(getString(R.string.fActions), gson.toJson(params[1]));
            }


            Log.i("HandleReportingAsync","data = "+gson.toJson(tmp));
            DataBuffer.addEvent(gson.toJson(tmp));
            return null;
        }

    }
}
