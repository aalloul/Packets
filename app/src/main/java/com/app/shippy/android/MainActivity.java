package com.app.shippy.android;


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
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ItemFragment.OnListFragmentInteractionListener,
        OfferDetail.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        DatePickerFragment.TheListener, RegistrationFragment.RegistrationFragmentListener,
        ConfirmPublish.OnCofirmPublishListener, CameraOrGalleryDialog.CameraOrGalleryInterface,
        ThankYou.ShareFragmentListener, NoResultsFound.OnNoResultsFoundInteraction, PrivacyNotice.ContactUsListener {

    protected final int MAP_PERMISSION = 1;
    protected final int DROP_OFF_LOCATION_REQUEST = 2;
    protected final int PICK_UP_LOCATION_REQUEST = 3;
    protected final int USER_LOCATION_REQUEST = 4;
    static final int REQUEST_IMAGE_CAPTURE = 5;
    static final int REQUEST_WRITE_FILES_FROM_CAMERA = 6;
    static final int REQUEST_WRITE_FILES_FROM_GALLERY = 7;
    static final int REQUEST_PICK_PICTURE= 8;
    static final int REQUEST_SHARE_APP = 9;
    static boolean PICTURE_FOR_CONFIRM=false;
    private HashMap<String, String> u0 = new HashMap<>();
    protected final int PICKUP_AIM = 1;
    protected final int DROPOFF_AIM = 2;
    protected final int REGISTRATION_AIM = 3;
    protected final int UPDATE_STORED_LOCATION_AIM = 4;
    protected long SESSION_ID = Utilities.CurrentTimeMS();
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
    private RequestQueue volleyQueue;
    private PrivacyNotice privacyNotice;
    private NoResultsFound noresultsfound;
    private DataBuffer dataBuffer;
    private ReportingEvent reportingEvent;

    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private static boolean DEBUG = false;

    private boolean isAlreadyRegistered() {
        if (DEBUG) Log.i(LOG_TAG, "isAlreadyRegistered - Enter");
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        if (sharedPref.getString(getString(R.string.saved_user_firstname), "").equals("")) {
            if (DEBUG) Log.i(LOG_TAG,"isAlreadyRegistered - User first name not found so not registered");
            return false;
        }
        if (DEBUG) Log.i(LOG_TAG, "userFirstName = " +
                sharedPref.getString(getString(R.string.saved_user_firstname), ""));
        return false;
    }

    // TODO looks like our SQLite is not closed when the app crashes

    protected HashMap<String, String> getUserDetailedLocation() {
        if (DEBUG) Log.i(LOG_TAG, "getUserDetailedLocation - Enter");
        HashMap<String, String> tmp = new HashMap<>();
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        tmp.put(getString(R.string.saved_user_postalcode),
                sharedPref.getString(getString(R.string.saved_user_postalcode), ""));
        tmp.put(getString(R.string.saved_user_city),
                sharedPref.getString(getString(R.string.saved_user_city), ""));

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
        if (DEBUG) Log.i(LOG_TAG, "getUserDetailedLocation - Exit");
        return tmp;
    }

    /**
     * This function reads the data entered in the registration fragment
     * and returns a hashmap
     * @param nextFrag is used by the data reporting to know what the next fragment is
     * @return a hashmap to send to the back-end
     */

    private HashMap<String, String> storeUserDetails(String nextFrag) {
        if (DEBUG) Log.i(LOG_TAG, "storeUserDetails - Enter");
        HashMap<String, String> tmp = registrationFragment.getBlob(nextFrag);
        storeUserDetails(tmp);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        int n_prompts = sharedPref.getInt(getString(R.string.saved_user_npromptstoregister), 0);
        tmp.put(getString(R.string.nprompts), Integer.toString(n_prompts));
        if (DEBUG) Log.i(LOG_TAG, "storeUserDetails - Exit");
        return tmp;
    }

    private void storeUserDetails(HashMap<String, String> details) {
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        int n_prompts = sharedPref.getInt(getString(R.string.saved_user_npromptstoregister), 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_user_npromptstoregister), n_prompts+1);
        if (DEBUG) Log.i(LOG_TAG, "storeUserDetails(details) - saved_user_firstname = "+
                details.get(getString(R.string.saved_user_firstname)));
        if (DEBUG) Log.i(LOG_TAG, "storeUserDetails(details) - saved_user_firstname = "+
                details.get(getString(R.string.saved_user_surname)));

        for (Map.Entry<String, String> entry : details.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }

        editor.apply();
    }

    private void generateDeviceID(){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String devid = sp.getString(getString(R.string.deviceid), "");
        String devtype = sp.getString(getString(R.string.devicetype), "");

        if (devid.equals("")) {
            /*
             * here we assume this is the first time this user opens the app and hope the test above
             * will never return True while the deviceid was already set.
             */
            SharedPreferences.Editor editor = sp.edit();
            devid = UUID.randomUUID().toString();
            devtype = Build.DEVICE;
            if (DEBUG) Log.i(LOG_TAG, "generateDeviceID - 1st install");
            if (DEBUG) Log.i(LOG_TAG, "generateDeviceID - deviceID = "+devid + " device type = "+devtype);
            editor.putString(getString(R.string.deviceid), devid);
            editor.putString(getString(R.string.devicetype), devtype);
            /*
             * here we use apply (asynchronous) rather then commit (synchronous), the reasoning being
             * that if this is not successful, next time we'll try again.
             */
            editor.apply();
        }
        dataBuffer.setDeviceId(devid);
        dataBuffer.setDeviceType(devtype);

    }

    private void updateStoredLocation(Location location) {
        HandleGeoCodingAsync handlegeocoding =
                new HandleGeoCodingAsync(this, UPDATE_STORED_LOCATION_AIM);
        handlegeocoding.execute(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    private void handleNetworkResult(int queryResult) {
        if (DEBUG) Log.i(LOG_TAG, "handleNetworkResult  - queryResult = " + queryResult);

        if (mainFragment==null) {
            if (DEBUG) Log.e(LOG_TAG, "handleNetworkResult - mainFragment is null");
        }

        switch (queryResult) {
            case 0:
                if (DEBUG) Log.i(LOG_TAG, "handleNetworkResult - empty results");
                handleEmptyResults();
                break;

            default :
                if (DEBUG) Log.i(LOG_TAG, "handleNetworkResult - Notifying dataset change");
                handleNonEmptyResults(queryResult);
                break;
        }
    }

    private void handleNonEmptyResults(int responseLEngth) {
        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
        reportingEvent.setFragmentName("ItemFragment");
        reportingEvent.setFragmentStart(itemFragment.getFragmentStartTime());
        reportingEvent.setFragmentEnd();
        reportingEvent.addEvent("searchResults", responseLEngth);

        if (itemFragment == null) {
            if (DEBUG) Log.i(LOG_TAG, "handleNetworkResult - itemFragment is null");
        } else {
            itemFragment.updateData();
        }
    }

    private void handleEmptyResults(){
        if (noresultsfound==null) noresultsfound = NoResultsFound.newInstance();

        // Reporting block
        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
        reportingEvent.setFragmentName("ItemFragment");
        reportingEvent.setFragmentStart(itemFragment.getFragmentStartTime());
        reportingEvent.addEvent("searchResults", 0);

        if (isFinishing()) {Log.i(LOG_TAG, "handleNetworkResult - Is Finishing");}
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainActivity_ListView, noresultsfound, "noresultsfound")
                    .addToBackStack("tonoresultsfound")
                    .commitAllowingStateLoss();

        } catch (Exception e){
            Log.e(LOG_TAG, "handleNetworkResult - WAS GONNA CRASH");
            e.printStackTrace();
        }
    }

    private boolean isNetworkOk() {
        if (DEBUG) Log.i(LOG_TAG, "isNetworkOk - Enter");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (DEBUG) Log.i(LOG_TAG, "isNetworkOk - Network available");
            return true;
        } else {
            if (DEBUG) Log.w(LOG_TAG, "isNetworkOk - Network not available");
            return false;
        }
    }

    protected void updateUserDetails(Location location) {
        // Called from the wrong place?
        if (registrationFragment == null) {
            if (DEBUG) Log.i(LOG_TAG, "updateUserDetails - This call is weird as registrationFragment is null");
            return;
        }

        // Location already known
//        if (registrationFragment.get_user_location() != "") {
//            if (DEBUG) Log.i(LOG_TAG, "updateUserDetails - User location is already known. Do nothing");
//            return;
//        }
        // Okay now we can use this new location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        HandleGeoCodingAsync handlegeocoding = new HandleGeoCodingAsync(this, REGISTRATION_AIM);
        handlegeocoding.execute(latLng);
    }

    protected boolean checkPermission() {
        if (DEBUG) Log.i(LOG_TAG, "Start checking permissions");
        boolean b = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        if (b) {
            if (DEBUG) Log.i(LOG_TAG, "Permission already granted");
            MAP_PERMISSION_GRANTED = true;
            return true;
        }

        if (DEBUG) Log.i(LOG_TAG, "Permission not granted");
        // Should we show an explanation?
        MAP_PERMISSION_GRANTED = false;

        b = ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (!b) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION);
            return false;
        }

        if (DEBUG) Log.i(LOG_TAG, "Apparently we should show an explanation");
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
        if (DEBUG) Log.i(LOG_TAG, "explanation displayed");

        return false;
    }

    protected boolean checkFilePermission(final int which) {

        if (DEBUG) Log.i(LOG_TAG, "checkFilePermission - Start checking permissions");
        if (DEBUG) Log.i(LOG_TAG, "checkFilePermission - which = "+which);

        boolean b = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (b) {
            if (DEBUG) Log.i(LOG_TAG, "Permission already granted");
            return true;
        }

        if (DEBUG) Log.i(LOG_TAG, "Permission not granted");
        // Should we show an explanation?
        b = ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!b) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, which);
            return false;
        }

        if (DEBUG) Log.i(LOG_TAG, "Apparently we should show an explanation");
        Snackbar.make(findViewById(R.id.mainActivity_ListView),
                R.string.permission_position_explanation,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.permission_position_explanation_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (DEBUG) Log.i(LOG_TAG, "Snackbar - which = " + which);
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                which);
                    }
                }).show();
        if (DEBUG) Log.i(LOG_TAG, "explanation displayed");

        return false;
    }

    protected void stopLocationUpdates() {
        if (DEBUG) Log.i(LOG_TAG, "stopLocationUpdates - Enter");
        if (mGoogleApiClient == null) return;
        if (DEBUG) Log.i(LOG_TAG, "stopLocationUpdates - mGoogleApiClient not null");
        if (!mGoogleApiClient.isConnected()) return;
        if (DEBUG) Log.i(LOG_TAG, "stopLocationUpdates - mGoogleApiClient connected");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        if (DEBUG) Log.i(LOG_TAG, "stopLocationUpdates - mGoogleApiClient disconnected");
    }

    // Tries to open the settings 
    public void goToSettings() {
        try {
            Intent gpsOptionsIntent = new Intent(Intent.ACTION_MAIN);
            gpsOptionsIntent.setClassName("com.android.phone", "com.android.phone.Settings");
            startActivity(gpsOptionsIntent);
        } catch (Exception e) {
            dataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "MainActivity", "goToSettings");

            if (DEBUG) Log.w(LOG_TAG, "goToSettings - could not go to Settings");
        }
    }

    // Checks the inputs from the Main Activity
    @SuppressWarnings("unchecked")
    boolean checkMainFragmentInputs(String action) {

        if (mainFragment == null) {
            if (DEBUG) Log.i(LOG_TAG, "checkMainFragmentInputs - mainFragment is null");
            return false;
        }

        if (reportingEvent == null) reportingEvent = new ReportingEvent();

        reportingEvent.setFragmentName("MainFragment");
        reportingEvent.setFragmentStart(mainFragment.getFragmentStartTime());

        if (!isNetworkOk()) {
            // Report error
            reportingEvent.addEvent("action",action+"WithNoNetwork");
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
            reportingEvent.addEvent("action",action+"WithNoPickupLocation");

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.pickup_location_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }

        if (DEBUG) Log.i(LOG_TAG, "checkMainFragmentInputs- pick up location = "+ pickuplocation);

        String dateforpickup = mainFragment.getDateForPickUp();
        if ( dateforpickup == null) {

            // Report error
            reportingEvent.addEvent("action",action+"WithNoPickupDate");

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.date_send_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        if (DEBUG) Log.i(LOG_TAG, "checkMainFragmentInputs- date of pickup= "+ dateforpickup);

        String numberPackages = mainFragment.getNumberPackages();
        if ( numberPackages == null) {

            // Report error
            reportingEvent.addEvent("action",action+"WithNoNumberPackages");

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.size_packages_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        if (DEBUG) Log.i(LOG_TAG, "checkMainFragmentInputs- Number of Pakcages = "+ numberPackages);

        String sizePackages = mainFragment.getSizePackage();
        if (sizePackages == null) {

            // Report error
            reportingEvent.addEvent("action",action+"WithNoPackageSize");

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.number_packages_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        if (DEBUG) Log.i(LOG_TAG, "checkMainFragmentInputs- Number of sizePackages = "+ sizePackages);


        HashMap<String, String> dropOffLocation = mainFragment.getDropOffLocation();

        if ( dropOffLocation == null ||
                !dropOffLocation.containsKey(getString(R.string.saved_user_city)) ) {

            // Report error
            reportingEvent.addEvent("action",action+"WithNoDropoffLocation");

            if (DEBUG) Log.i(LOG_TAG, "checkMainFragmentInputs- Unbelievable!");
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.dropoff_location_not_set),
                    getResources().getString(R.string.okay)
            );
            return false ;
        }
        if (DEBUG) Log.i(LOG_TAG, "checkMainFragmentInputs- Drop off location = "+ dropOffLocation);



        return true;
    }

    // Launches the request to Google Places
    protected void launchPlaceAutoCompleteRequest(int reqCode) {
        // Let's assume the fragment name is already set
        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}

        // TODO better handle the exception - e.g. accept input or check internet connectivity
        try {
            // Only show addresses
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, reqCode);
        } catch (GooglePlayServicesRepairableException e) {
            reportingEvent.addException(e.getCause().toString(), e.toString(), "launchPlaceAutoCompleteRequest");

            if (DEBUG) Log.e(LOG_TAG, "GooglePlayServicesRepairableException Exception");
        } catch (GooglePlayServicesNotAvailableException e) {
            reportingEvent.addException(e.getCause().toString(), e.toString(), "launchPlaceAutoCompleteRequest");
            if (DEBUG) Log.e(LOG_TAG, "GooglePlayServicesNotAvailableException Exception");
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
        dets.put(getString(R.string.deviceid),
                sharedPref.getString(getString(R.string.deviceid),""));
        dets.put(getString(R.string.devicetype),
                sharedPref.getString(getString(R.string.devicetype),""));
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
                        .apply();

            } catch (IOException ex) {
                // Error occurred while creating the File
                if (DEBUG) Log.i(LOG_TAG, "dispatchTakePictureIntent - IOException happened");

                if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
                if (PICTURE_FOR_CONFIRM){
                    reportingEvent.setFragmentName("ConfirmPublish");
                    reportingEvent.setFragmentStart(confirmPublish.getFragmentStartTime());
                } else {
                    reportingEvent.setFragmentName("RegistrationFragment");
                    reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());
                }
                reportingEvent.addException(ex.getCause().toString(), ex.toString(), "dispatchTakePictureIntent");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.app.shippy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                reportingEvent.addEvent("action","PhotoNotTaken");
            }
        } else {
            reportingEvent.addEvent("action","NoCameraFound");
        }
    }

    // Creates the intent for the user to pick a picture from gallery
    private void dispatchGetPictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,REQUEST_PICK_PICTURE );
    }

    private void informNetworkError(final Fragment fragment) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.network_error))
                .setMessage(getResources().getString(R.string.network_erro2))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getSupportFragmentManager()
                                .beginTransaction()
//                                .detach(itemFragment)
                                .replace(R.id.mainActivity_ListView, fragment, fragment.getTag())
                                .commit();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * This method is used to setup a GsonRequest towards the back-end, i.e. it expects a result
     * as a JSON.
     * The request method is a POST
     * @param searchParams is the HashMap with the search parameters
     */
    @SuppressWarnings("unchecked")
    private void searchRequest(HashMap<String, String> searchParams) {
        if (DEBUG) Log.i(LOG_TAG, "searchRequest - Enter");

        String url = "https://2ewwhcff9d.execute-api.eu-west-1.amazonaws.com/production/shippy-search";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("deviceID",dataBuffer.getDeviceId());
        headers.put("deviceType",dataBuffer.getDeviceType());
        headers.put("networkRequestTime", String.valueOf(Utilities.CurrentTimeMS()));
        headers.put("x-api-key","vmXvcob4V33NpNVXKsDll8nAQAcmHGZZ87Fl4HF6");

        if (DEBUG) Log.i(LOG_TAG, "searchRequest - searchParams = " + searchParams);

        GsonRequest request = new GsonRequest(url, searchParams, SearchResponse[].class, headers,
                Request.Method.POST, new Response.Listener<SearchResponse[]>() {
                    @Override
                    public void onResponse(SearchResponse[] response) {
                        new Postman(getApplicationContext(), response);
                        Intent newdata = new Intent("newDataHasArrived");
                        if (response == null) {
                            if (DEBUG) Log.i(LOG_TAG, "searchRequest - response is null");
                            newdata.putExtra("queryResult", response.length);
                        } else {
//                            newdata.putExtra("queryResult", "ok");
                            newdata.putExtra("queryResult", response.length);
                        }

                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newdata);
                    }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataBuffer.addException("searchRequestError", error.getMessage(), "mainFragment",
                                "search");
                        if (DEBUG) Log.i(LOG_TAG, "error.getMesage() = " + error.getMessage());
                        if (DEBUG) Log.i(LOG_TAG, "error.getCause() = " + error.getCause());
                        informNetworkError(mainFragment);
                    }
                }
        );
        request.setTag("searchOfferTAG");
        // 1st argument is initial timeout, 2nd is number of retries, 3rd is multiplier
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 0, 0.0f));

        if (DEBUG) Log.i(LOG_TAG, "searchRequest - Adding request to queue");
        volleyQueue.add(request);
    }

    /**
     * This method is used to setup a GsonRequest towards the back-end but doesn't expect any result
     * back but an HTTP code.
     * @param dataToPost is the HashMap containing the data we want to post to the back-end
     */
    @SuppressWarnings("unchecked")
    private void postUsageRequest(final ArrayList<HashMap<String, Object>> dataToPost) {
        String url = "https://2ewwhcff9d.execute-api.eu-west-1.amazonaws.com/production/shippy-logging";
        if (DEBUG) Log.i(LOG_TAG, "postUsageRequest - Enter");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("deviceID",dataBuffer.getDeviceId());
        headers.put("deviceType",dataBuffer.getDeviceType());
        headers.put("networkRequestTime", String.valueOf(Utilities.CurrentTimeMS()));
        headers.put("x-api-key","vmXvcob4V33NpNVXKsDll8nAQAcmHGZZ87Fl4HF6");

        if (DEBUG) Log.i(LOG_TAG, "dataToPost = " + dataToPost);

        GsonRequest request = new GsonRequest(url, dataToPost, null, headers, Request.Method.POST,
                new Response.Listener<Gson>() {
                    @Override
                    public void onResponse(Gson response) {
                        if (DEBUG) Log.i(LOG_TAG, "postUsageRequest - Got positive answer");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataBuffer.addException("postUsageRequestError", error.getMessage(),
                                "postUsage", "postUsage");
//                        dataBuffer.addEvent(dataToPost);
                    }
                }
        );
        request.setTag("usageStatsTag");
        if (DEBUG) Log.i(LOG_TAG, "postUsageRequest - Adding to request queue");
//        volleyQueue.add(request);
    }

    /**
     * This method is çalled when the user wants to publish an offer
     * @param dataToPost this is simply the data
     */
    @SuppressWarnings("unchecked")
    private void postOfferRequest(HashMap<String, String> dataToPost) {
        String url = "https://2ewwhcff9d.execute-api.eu-west-1.amazonaws.com/production/shippy-offers";
        if (DEBUG) Log.i(LOG_TAG, "postUsageRequest - Enter");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("deviceID",dataBuffer.getDeviceId());
        headers.put("deviceType",dataBuffer.getDeviceType());
        headers.put("networkRequestTime", String.valueOf(Utilities.CurrentTimeMS()));
        headers.put("x-api-key","vmXvcob4V33NpNVXKsDll8nAQAcmHGZZ87Fl4HF6");

        GsonRequest request = new GsonRequest(url, dataToPost, null, headers, Request.Method.POST,
                new Response.Listener<Gson>() {
                    @Override
                    public void onResponse(Gson response) {
                        if (DEBUG) Log.i(LOG_TAG, "postUsageRequest - Got positive answer");
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataBuffer.addException("postOfferRequestError", error.getMessage(),
                                "ConfirmPublish", "postOffer");
                        informNetworkError(confirmPublish);
                    }
                }
        );
        request.setTag("postOfferTag");
        if (DEBUG) Log.i(LOG_TAG, "postUsageRequest - Adding to request queue");
        // 1st argument is initial timeout, 2nd is number of retries, 3rd is multiplier
        request.setRetryPolicy(new DefaultRetryPolicy(4000, 0, 0.0f));

        volleyQueue.add(request);
    }

    public void OnPrivacyButtonPressed() {
        privacyNotice = PrivacyNotice.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_ListView, privacyNotice, "privacyNotice")
                .addToBackStack("toPrivacy")
                .commit();
    }


    /* ***************** ***************** ***************** *****************
                        Override section
       ***************** ***************** ***************** ***************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(LOG_TAG, "OnCreate - Enter");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_listview);

        dataBuffer = new DataBuffer();
        dataBuffer.setSessionStart();

        reportingEvent = new ReportingEvent();
        reportingEvent.setActivityName("MainActivity");

        // Initialize the volley queue
        // Get a RequestQueue
        volleyQueue = VolleySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        // This piece is to check whether the user is already registered with his city being
        // Updating
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        generateDeviceID();
        HashMap<String, String> userDetailedLocation = getUserDetailedLocation();
        String userCity = userDetailedLocation.get(getString(R.string.saved_user_city));
        String firstname = getUserPersonalDetails().get(getString(R.string.saved_user_firstname));

        if (DEBUG) Log.i(LOG_TAG, "onCreate - userCity = " + userCity);
        boolean isalreadyregistered = isAlreadyRegistered();

        reportingEvent.addEvent("wasRegistered", isalreadyregistered);

        // if not already registered or the city is "updating" then connect to Google API
        if (mGoogleApiClient == null || (!isalreadyregistered || userCity.equals(""))) {
            if (DEBUG) Log.i(LOG_TAG, "onCreate - mGoogleApiClient is null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } else {
            if (DEBUG) Log.i(LOG_TAG, "onCreate - mGoogleApiClient is not null");
            mGoogleApiClient.connect();
        }

        if (savedInstanceState == null) {
            if (DEBUG) Log.i(LOG_TAG, "onCreate - savedInstanceState is null");
            FragmentTransaction fmg = getSupportFragmentManager().beginTransaction();
            if (isalreadyregistered) {
                if (DEBUG) Log.i(LOG_TAG, "onCreate - user already registered");
                mainFragment = MainFragment.newInstance(userDetailedLocation, firstname);
                fmg.add(R.id.mainActivity_ListView, mainFragment, "mainFragment");
            } else {
                if (DEBUG) Log.i(LOG_TAG, "onCreate - user not registered");
                registrationFragment = RegistrationFragment.newInstance();
                fmg.add(R.id.mainActivity_ListView, registrationFragment, "registrationFragment");
            }
            fmg.commit();
        } else {
            if (DEBUG) Log.i(LOG_TAG, "OnCreate - savedInstanceState not null");
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
            noresultsfound = (NoResultsFound)
                    getSupportFragmentManager().findFragmentByTag("noresultsfound");
        }

        // New data from the back-end was downloaded
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int queryResult = intent.getIntExtra("queryResult", 0);
                handleNetworkResult(queryResult);
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter("newDataHasArrived"));

        if (DEBUG) Log.i(LOG_TAG, "OnCreate - Exit");
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        if (DEBUG) Log.i(LOG_TAG, "onConnected - Enter ");
        LatLng latLng;

        if (!checkPermission()) return;
        // Get location updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        if (DEBUG) Log.i(LOG_TAG, "onConnected - Requested location updates");

        if (registrationFragment == null) {
            if (DEBUG) Log.i(LOG_TAG, "onConnected - Registration Fragment is null");
            return;
        }

        if (!registrationFragment.isVisible()) {
            if (DEBUG) Log.i(LOG_TAG, "onConnected - Registration Fragment is not visible");
            return;
        }

        Location userLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (DEBUG) Log.i(LOG_TAG, "onConnected - Requested last location");

        if (userLastLocation == null) {
            if (DEBUG) Log.i(LOG_TAG, "onConnected - userLastLocation is null");
            registrationFragment.set_user_detailed_location(new HashMap<String, String>());
            if (DEBUG) Log.i(LOG_TAG, "onConnected - Location not available yet");
            return;
        }

        latLng = new LatLng(userLastLocation.getLatitude(), userLastLocation.getLongitude());
        HandleGeoCodingAsync handlegeocoding = new HandleGeoCodingAsync(this, REGISTRATION_AIM);
        handlegeocoding.execute(latLng);

        if (DEBUG) Log.i(LOG_TAG, "onConnected - Exit ");
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (DEBUG) Log.i(LOG_TAG, "onRequestPermissionsResult - Enter");
        if (DEBUG) Log.i(LOG_TAG, "onRequestPermissionsResult - requestCode = " + requestCode);

        switch (requestCode) {
            case MAP_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (DEBUG) Log.i(LOG_TAG, "onRequestPermissionsResult - Position permission granted");
                    // permission was granted, yay! Do your thing mama!
                    MAP_PERMISSION_GRANTED = true;
                } else {
                    if (DEBUG) Log.i(LOG_TAG, "onRequestPermissionsResult - Position permission denied");
                    // permission denied, boo!
                    MAP_PERMISSION_GRANTED = false;
                }
                break;
            }
            case REQUEST_WRITE_FILES_FROM_CAMERA: {
                if (DEBUG) Log.i(LOG_TAG, "onRequestPermissionsResult - REQUEST_WRITE_FILES_FROM_CAMERA granted");
                dispatchTakePictureIntent();
                break;
            }
            case REQUEST_WRITE_FILES_FROM_GALLERY: {
                if (DEBUG) Log.i(LOG_TAG, "onRequestPermissionsResult - REQUEST_WRITE_FILES_FROM_GALLERY granted");
                dispatchGetPictureFromGallery();
                break;
            }
        }
        if (DEBUG) Log.i(LOG_TAG, "onRequestPermissionsResult - Exit");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (DEBUG) Log.i(LOG_TAG, "onResume - enter");

        if (mGoogleApiClient == null ) {
            if (DEBUG) Log.i(LOG_TAG, "onResume - mGoogleApiClient is null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } else {
            if (DEBUG) Log.i(LOG_TAG, "onResume - mGoogleApiClient is NOT null");
            if (DEBUG) Log.i(LOG_TAG, "onResume - Connecting mGoogleApiClient ");
            mGoogleApiClient.connect();
        }

        if (dataBuffer == null) {
            dataBuffer = new DataBuffer();
            dataBuffer.setSessionStart();
        }

        if (reportingEvent == null) {
            reportingEvent = new ReportingEvent();
            reportingEvent.setActivityName("MainActivity");
        }
        reportingEvent.setend_session(true);

    }

    @Override
    public void onStart() {
        if (DEBUG) Log.i(LOG_TAG, "onStart - Enter");
        if (mGoogleApiClient != null ) {mGoogleApiClient.connect();}

        super.onStart();

        if (dataBuffer == null) {
            dataBuffer = new DataBuffer();
            dataBuffer.setSessionStart();
        }

        if (reportingEvent == null) {
            reportingEvent = new ReportingEvent();
            reportingEvent.setActivityName("MainActivity");
        }
        reportingEvent.setend_session(true);

        if (DEBUG) Log.i(LOG_TAG, "onStart - Exit");
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.i(LOG_TAG, "onStop - Enter");
        super.onStop();

        if (reportingEvent != null && reportingEvent.end_session()) {
            reportingEvent.setFragmentEnd();
            reportingEvent.setSessionEnd();
            reportingEvent.addEvent("action","onStop");
            ArrayList<HashMap<String, Object>>  thedata = reportingEvent.getEvents();
            reportingEvent.clearEvents();
            postUsageRequest(thedata);
        }

        // Disconnect the Google API client
        if (mGoogleApiClient != null ) mGoogleApiClient.disconnect();

        // Cancel the volley requests
        if (volleyQueue != null) {
            volleyQueue.cancelAll("searchTAG");
            volleyQueue.cancelAll("postTAG");
        }
        if (DEBUG) Log.i(LOG_TAG, "onStop - Exit");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onPause() {
        if (DEBUG) Log.i(LOG_TAG, "onPause - Enter");
        super.onPause();
        stopLocationUpdates();


        if (registrationFragment != null && registrationFragment.isVisible()) {
            reportingEvent.setFragmentName("RegistrationFragment");
        }
        if (mainFragment != null && mainFragment.isVisible()) {
            reportingEvent.setFragmentName("MainFragment");
        }
        if (confirmPublish != null && confirmPublish.isVisible()) {
            reportingEvent.setFragmentName("ConfirmPublish");
        }
        if (thankYou != null && thankYou.isVisible()) {
            reportingEvent.setFragmentName("ThankYou");
        }
        if (itemFragment != null && itemFragment.isVisible()) {
            reportingEvent.setFragmentName("ItemFragment");
        }
        if (detailsFragment != null && detailsFragment.isVisible()) {
            reportingEvent.setFragmentName("DetailsFragment");
        }

        if (reportingEvent.end_session()){
            reportingEvent.setFragmentEnd();
            reportingEvent.setSessionEnd();
            reportingEvent.addEvent("action","onPause");
            ArrayList<HashMap<String, Object>>  thedata = reportingEvent.getEvents();
            reportingEvent.clearEvents();
            postUsageRequest(thedata);
        }
        reportingEvent.addEvent("action","onPause");

        if (DEBUG) Log.i(LOG_TAG, "onPause - Exit");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSaveInstanceState(Bundle outState) {

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
        if (noresultsfound != null && noresultsfound.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "noresultsfound", noresultsfound);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (DEBUG) Log.i(LOG_TAG, "onConnectionFailed - Enter");
        if (DEBUG) Log.i(LOG_TAG, "onConnectionFailed - Exit");

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (DEBUG) Log.i(LOG_TAG, "onConnectionSuspended - Enter");
        if (DEBUG) Log.i(LOG_TAG, "onConnectionSuspended - Exit");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onOfferSelected(String nameAndFirstName, String pickup_city, String pickup_country,
                                String pickup_date, String dropoff_city, String dropoff_country,
                                String n_packets, String package_size, String picture,
                                String phone_number, String transport_methods, String comments,
                                int thepos) {
        if (DEBUG) Log.i(LOG_TAG, "onListFragmentInteraction - Received a click - content is");
        if (DEBUG) Log.i(LOG_TAG, "onListFragmentInteraction - nameAndFirstName = " + nameAndFirstName);
        if (DEBUG) Log.i(LOG_TAG, "onListFragmentInteraction - source_city = " + pickup_city);

        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
        reportingEvent.setFragmentName("ItemFragment");
        reportingEvent.setFragmentStart(itemFragment.getFragmentStartTime());
        reportingEvent.setFragmentEnd();
        reportingEvent.addEvent("action","selectedOffer", "offerPosition",thepos);

        detailsFragment =
                OfferDetail.newInstance(nameAndFirstName, pickup_city, pickup_country, pickup_date,
                        dropoff_city, dropoff_country, n_packets, package_size, picture,
                        phone_number, transport_methods,comments);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, detailsFragment, "detailsFragment")
                .addToBackStack(null)
                .commit();

    }

    // Method for offerDetail
    @Override
    @SuppressWarnings("unchecked")
    public void onCallButtonPress(String phonenumber) {
        if (DEBUG) Log.i(LOG_TAG, "onCallButtonPress - Start");
        if (DEBUG) Log.i(LOG_TAG, "onCallButtonPress - Calling " + phonenumber);

        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
        reportingEvent.setFragmentName("OfferDetail");
        reportingEvent.setFragmentStart(detailsFragment.getFragmentStartTime());
        reportingEvent.setFragmentEnd();
        reportingEvent.addEvent("action", "call");

        Uri number = Uri.parse("tel:" + phonenumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
        if (DEBUG) Log.i(LOG_TAG, "onCallButtonPress - Exit");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onMessageButtonPress(String phonenumber) {
        if (DEBUG) Log.i(LOG_TAG, "onMessageButtonPress - Start");
        if (DEBUG) Log.i(LOG_TAG, "onMessageButtonPress - Calling " + phonenumber);

        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
        reportingEvent.setFragmentName("OfferDetail");
        reportingEvent.setFragmentStart(detailsFragment.getFragmentStartTime());
        reportingEvent.setFragmentEnd();
        reportingEvent.addEvent("action", "sendMessage");

        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phonenumber, null));

        startActivity(smsIntent);
        if (DEBUG) Log.i(LOG_TAG, "onMessageButtonPress - Exit");
    }

    // Method for search_button
    @Override
    @SuppressWarnings("unchecked")
    public void onSearchButtonPressed() {
        if (DEBUG) Log.i(LOG_TAG, "onSearchButtonPressed - start");

        if (reportingEvent == null) {
            reportingEvent = new ReportingEvent();
        }

        reportingEvent.setFragmentName("MainFragment");
        reportingEvent.setFragmentStart(mainFragment.getFragmentStartTime());


        if (!checkMainFragmentInputs("SEARCH")) {
            if (DEBUG) Log.i(LOG_TAG, "onSearchButtonPressed - user did not provide any input");
            return;
        }

        reportingEvent.setFragmentEnd();
        HashMap<String, String> tmp = mainFragment.getTripDetails("SEARCH","itemFragment");

        reportingEvent.addEvent("action","Search",
                "PickupDate",tmp.get(getString(R.string.date_for_pickup)),
                "NumberPackages",tmp.get(getString(R.string.number_packages)),
                "SizePackages",tmp.get(getString(R.string.size_packages)),
                "EditedPickupLocation", mainFragment.getEditedPickupLocation()
                );

        searchRequest(tmp);

        itemFragment = ItemFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainActivity_ListView, itemFragment, "itemFragment")
                .addToBackStack("mainToitem")
                .commit();


        if (DEBUG) Log.i(LOG_TAG, "onSearchButtonPressed - exit");
    }

    // Method for publish offer
    @Override
    @SuppressWarnings("unchecked")
    public void onPostButtonPressed() {
        if (DEBUG) Log.i(LOG_TAG, "onPostButtonPressed - start");

        if (reportingEvent == null) reportingEvent = new ReportingEvent();

        reportingEvent.setFragmentName("MainFragment");
        reportingEvent.setFragmentStart(mainFragment.getFragmentStartTime());

        // Check whether input data is correct
        if (!checkMainFragmentInputs("POST")) { return ;}

        // Gather all information
        HashMap<String, String> userDetails = getUserPersonalDetails();
        HashMap<String, String> hashMap = mainFragment.getTripDetails("POST", "confirmPublish");

        // Send data to reporting
        reportingEvent.setFragmentEnd();
        reportingEvent.addEvent( "action", "PostOffer",
                "PickupDate",hashMap.get(getString(R.string.date_for_pickup)),
                "NumberPackages",hashMap.get(getString(R.string.number_packages)),
                "SizePackages",hashMap.get(getString(R.string.size_packages)),
                "EditedPickupLocation", mainFragment.getEditedPickupLocation()
        );


        stopLocationUpdates();
        // Show confirmation page
        confirmPublish = ConfirmPublish.newInstance(userDetails,hashMap);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, confirmPublish, "confirmPublish")
                .addToBackStack("MainFragmentToConfirmPublish")
                .commit();
        if (DEBUG) Log.i(LOG_TAG, "onPostButtonPressed - exit");
    }

    @Override
    public void returnDate(String date) {
        if (DEBUG) Log.i(LOG_TAG, "returnDate - called");
        mainFragment.setDate(date);
    }

    @Override
    public void onPickUpLocationPressed() {
        if (DEBUG) Log.i(LOG_TAG, "onPickUpLocationPressed - Enter");
        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);

        mainFragment.setEdited_pickup_location();
        launchPlaceAutoCompleteRequest(PICK_UP_LOCATION_REQUEST);
    }

    @Override
    public void onDropOffLocationPressed() {
        if (DEBUG) Log.i(LOG_TAG, "onDropOffLocationPressed - Enter");

        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);

        launchPlaceAutoCompleteRequest(DROP_OFF_LOCATION_REQUEST);
    }

    // Handles the callback result when the user enters a location somewhere
    private void handleTripLocationRequest(int resultCode, Intent data, int requestAim) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            if (DEBUG) Log.i(LOG_TAG, "Place: " + place.getName());
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
            if (DEBUG) Log.i(LOG_TAG, status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            if (DEBUG) Log.i(LOG_TAG, "handleTripLocationRequest - cancelled");
        }
    }

    // Handles the camera result
    private void handleUserCameraResult(int resultCode) {
        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
        if (PICTURE_FOR_CONFIRM) {
            reportingEvent.setFragmentName("ConfirmPublish");
            reportingEvent.setFragmentStart(confirmPublish.getFragmentStartTime());
        } else {
            reportingEvent.setFragmentName("RegistrationFragment");
            reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());
        }

        if (resultCode == RESULT_OK) {
            File file = new File(mCurrentPhotoPath);
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                reportingEvent.addEvent("action","PictureTaken");
                // Handle this asynchronously
                HandlePictureAsync handlepicture = new HandlePictureAsync();
                handlepicture.execute(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                reportingEvent.addException(e.getCause().toString(), e.toString(), "handleUserCameraResult");

                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                reportingEvent.addException(e.getCause().toString(), e.toString(), "handleUserCameraResult");

                if (DEBUG) Log.w(LOG_TAG, "handleUserCameraResult - IOException");
                e.printStackTrace();
            }

        } else {
            reportingEvent.addEvent("action","PictureNotTaken");
        }
    }

    // handles the result after the user chose a photo from Gallery
    private void handleUserPickPicture(int resultCode, Intent data) {
        if (reportingEvent==null) {
            reportingEvent = new ReportingEvent();
        }

        if (PICTURE_FOR_CONFIRM) {
            reportingEvent.setFragmentName("ConfirmPublish");
            reportingEvent.setFragmentStart(confirmPublish.getFragmentStartTime());
        } else {
            reportingEvent.setFragmentName("RegistrationFragment");
            reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());
        }

        if (DEBUG) Log.i(LOG_TAG, "handleUserPickPicture - Enter");
        try {
            if (resultCode == RESULT_OK && null != data) {
                if (DEBUG) Log.i(LOG_TAG, "handleUserPickPicture - Result okay and data not null");
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor == null) return;

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                sharedPref.edit()
                        .putString(getString(R.string.saved_user_picture_path), mCurrentPhotoPath)
                        .apply();
                if (DEBUG) Log.i(LOG_TAG, "handleUserPickPicture - cursor closed");
                Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
                HandlePictureAsync handlepicture = new HandlePictureAsync();

                reportingEvent.addEvent("action","TookPicture");
                if (DEBUG) Log.i(LOG_TAG, "handleUserPickPicture - async handling of the picture");
                handlepicture.execute(bmp);
            }
        } catch (Exception e) {
            //TODO handle this exception
            reportingEvent.addException(e.getCause().toString(), e.toString(), "handleUserPickPicture");
            if (DEBUG) Log.i(LOG_TAG, "handleUserPickPicture - Exception caught");
            e.printStackTrace();
        }
    }

    // Gets result after sharing is done
    @SuppressWarnings("unchecked")
    private void handleSharingResult(int resultCode) {
        /*
          Here we don't force the transition to the main fragment and let the user do it by himself
          2 reasons:
            the fragmentSupportManager crashes with a commit (see http://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa)
            the user can decide to share the app again :)
        */
        u0.put(getString(R.string.fName),"thankyou");
        HashMap<String, String> u1 = new HashMap<>();
        long end_time = Utilities.CurrentTimeMS();
        long start_time = ThankYou.getStartTime();
        if (start_time == 0) start_time = Utilities.CurrentTimeMS();
        u1.put(getString(R.string.fStart), Long.toString(start_time));
        u1.put(getString(R.string.fEnd), Long.toString(end_time));
        u1.put(getString(R.string.fDuration),Long.toString(Utilities.CurrentTimeMS() - start_time));
        u1.put(getString(R.string.nextF), "mainFragment");

        if (resultCode == RESULT_OK) {
            if (DEBUG) Log.i(LOG_TAG, "handleSharingResult - RESULT OK");
            u1.put("sharing_result","OK");
//            new HandleReportingAsync().execute(u0,u1);
        } else {
            if (DEBUG) Log.i(LOG_TAG, "handleSharingResult - RESULT NOK");
            u1.put("sharing_result","FAILED");
//            new HandleReportingAsync().execute(u0,u1);
        }
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance(getUserDetailedLocation(),
                    getUserPersonalDetails().get(getString(R.string.saved_user_firstname)));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DROP_OFF_LOCATION_REQUEST:
                if (DEBUG) Log.i(LOG_TAG, "onActivityResult - Handling drop off location result");
                handleTripLocationRequest(resultCode, data, DROPOFF_AIM);
                break;

            case PICK_UP_LOCATION_REQUEST:
                if (DEBUG) Log.i(LOG_TAG, "onActivityResult - Handling pick up location result");
                handleTripLocationRequest(resultCode, data, PICKUP_AIM);
                break;

            case USER_LOCATION_REQUEST:
                if (DEBUG) Log.i(LOG_TAG, "onActivityResult - Handling registration location result");
                handleTripLocationRequest(resultCode, data, REGISTRATION_AIM);
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (DEBUG) Log.i(LOG_TAG, "onActivityResult - Handling picture taking by the user");
                handleUserCameraResult(resultCode);
                break;

            case REQUEST_PICK_PICTURE:
                if (DEBUG) Log.i(LOG_TAG, "onActivityResult - Handling picture chosen from Gallery");
                handleUserPickPicture(resultCode, data);
                break;
            case REQUEST_SHARE_APP:
                if (DEBUG) Log.i(LOG_TAG, "onActivityResult - Handling picture chosen from Gallery");
                // The intent data is useless here
                handleSharingResult(resultCode);
        }
    }

    @Override
    public void onUserPicturePressed() {
        if (DEBUG) Log.i(LOG_TAG, "onUserPicturePressed - Enter");

        if (reportingEvent==null) {
            reportingEvent = new ReportingEvent();
        }

        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);

        if (PICTURE_FOR_CONFIRM) {
            reportingEvent.setFragmentName("ConfirmPublish");
            reportingEvent.setFragmentStart(confirmPublish.getFragmentStartTime());
        } else {
            reportingEvent.setFragmentName("RegistrationFragment");
            reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());
        }

        reportingEvent.addEvent("action","choosePicture");

        try {
            FragmentManager fm = getSupportFragmentManager();
            CameraOrGalleryDialog dialogFragment = new CameraOrGalleryDialog();
            dialogFragment.show(fm, "Sample Fragment");
        } catch (Exception ex) {
            if (DEBUG) Log.i(LOG_TAG, "onUserPicturePressed - Exception caught");
            reportingEvent.addException(ex.getCause().toString(), ex.toString(), "onUserPicturePressed");
            ex.printStackTrace();
        } finally {
            if (DEBUG) Log.i(LOG_TAG, "onUserPicturePressed - Exit");
        }
    }

    @Override
    public void onUserPicturePressed_Confirm() {
        PICTURE_FOR_CONFIRM=true;
        onUserPicturePressed();
    }

    @Override
    public void onUserLocationPressed() {
        if (DEBUG) Log.i(LOG_TAG, "onDropOffLocationPressed - Enter");
        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}

        reportingEvent.setFragmentName("RegistrationFragment");
        reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());
        reportingEvent.addEvent("action","EditLocation");
        registrationFragment.setEdited_location();
        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);
        launchPlaceAutoCompleteRequest(USER_LOCATION_REQUEST);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRegisterMePressed() {
        if (DEBUG) Log.i(LOG_TAG, "onRegisterMePressed - Enter");

        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}

        reportingEvent.setFragmentName("RegistrationFragment");
        reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());

        if (registrationFragment == null ) {
            if (DEBUG) Log.i(LOG_TAG, "onRegisterMePressed - registrationFragment is null");
            registrationFragment = RegistrationFragment.newInstance();
        }
        int i = registrationFragment.isInputOk();

        // At least one important detail is missing
        if (i == 0) {
            reportingEvent.addEvent("action","RegisterWithoutDetails");
            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.registration_input_incomplete),
                    getResources().getString(R.string.okay));
            return;
        }

        // User location not provided and access not granted
        if (i == 1 && !MAP_PERMISSION_GRANTED) {
            reportingEvent.addEvent("action","RegisterWithoutLocation");
                Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                        getResources().getString(R.string.user_location_not_entered),
                        getResources().getString(R.string.okay));
            return;
        }

        // Store the details in SharedPref
        HashMap<String, String> tmp = storeUserDetails("mainFragment");
        // Report the new data
        reportingEvent.addEvent("action","RegisterSuccessful");

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
                    if (DEBUG) Log.w(LOG_TAG, "onRegisterMePressed - mainFragment is null");
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
        editor.apply();

        if (reportingEvent == null) {reportingEvent = new ReportingEvent();}
        reportingEvent.setFragmentName("RegistrationFragment");
        reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());
        reportingEvent.addEvent("action","RegisterLater", "n_prompts", n_prompts+1);


        if (mainFragment == null) {
            if (DEBUG) Log.i(LOG_TAG, "onRegisterLaterPressed - mainFragment is null");
            mainFragment = MainFragment.newInstance(getUserDetailedLocation(),"");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, mainFragment, "mainFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (DEBUG) Log.i(LOG_TAG, "onLocationChanged - Enter");
        if (DEBUG) Log.i(LOG_TAG, "onLocationChanged - location = " + location.toString());
        if (mainFragment != null && !mainFragment.isVisible()) {
            if (DEBUG) Log.i(LOG_TAG, "onLocationChanged - not visible -- Stopping" );
            stopLocationUpdates();
            return;
        }
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
        if (DEBUG) Log.i(LOG_TAG, "onConfirmPublish - Publishing new offer");

        u0.put(getString(R.string.fName),"confirmPublish");

        if (!confirmPublish.checkInputs()) {
//            new HandleReportingAsync().execute(u0,confirmPublish.getBlob(
//                    getString(R.string.ferror),getString(R.string.confirm_no_details)));

            Utilities.makeThesnack(findViewById(R.id.mainActivity_ListView),
                    getResources().getString(R.string.registration_input_incomplete),
                    getResources().getString(R.string.okay)
            );
            return;
        }

//        new HandleReportingAsync().execute(u0,confirmPublish.getBlob("thankYou"));

        HashMap<String, String> tmp = confirmPublish.getAllDetails();
        /* This might need a bit more thinking -- For example, the user is temporarily in another
         * city/country but doesn't want us to consider this place as his new home.
        */
//        storeUserDetails(tmp);

        postOfferRequest(tmp);

        // Show a thank you note
        thankYou = ThankYou.newInstance(tmp.get(getString(R.string.saved_user_firstname)));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_ListView, thankYou, "Thank you")
                .commit();

    }

    @Override
    public void onBackPressed()
    {
        if (DEBUG) Log.i(LOG_TAG, "onBackPressed - start");

        FragmentTransaction fmg = getSupportFragmentManager().beginTransaction();
        if (thankYou != null && thankYou.isVisible()) {
            if (DEBUG) Log.i(LOG_TAG, "onBackPressed - is instance");
            // Detach the "thank you" to avoid displaying 2 fragments at the same time
            fmg.detach(thankYou);
            // Detaching the confirmation to avoid people re-publishing the same offer
            if (confirmPublish != null) fmg.detach(confirmPublish);
            fmg.commit();
            return;
        }

        try {
            Log.i(LOG_TAG, "onBackPressed - trying super method");
            super.onBackPressed();
            Log.i(LOG_TAG, "onBackPressed - trying super method - succeeded");
        } catch (Exception e) {
            if (DEBUG) Log.i(LOG_TAG, "onBackPressed - STUCK STUCK STUCK STUCK ");
            dataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "MainActivity",
                    "onBackPressed");

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
        if (DEBUG) Log.i(LOG_TAG, "onCameraOrGallerySelected - Enter");

        if (reportingEvent==null) {
            reportingEvent = new ReportingEvent();
        }

        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);

        if (PICTURE_FOR_CONFIRM) {
            reportingEvent.setFragmentName("ConfirmPublish");
            reportingEvent.setFragmentStart(confirmPublish.getFragmentStartTime());
        } else {
            reportingEvent.setFragmentName("RegistrationFragment");
            reportingEvent.setFragmentStart(registrationFragment.getFragmentStartTime());
        }

        switch (which) {
            case 0:
                // Gallery chosen
                if (DEBUG) Log.i(LOG_TAG, "onCameraOrGallerySelected - choose from Gallery ");
                reportingEvent.addEvent("picture_chosen_from","Gallery");
                if (checkFilePermission(REQUEST_WRITE_FILES_FROM_GALLERY)) {
                    dispatchGetPictureFromGallery();
                } else {
                    reportingEvent.addEvent("error","REQUEST_WRITE_FILES_FROM_GALLERY Not Allowed");
                }
                break;

            case 1:
                // Take a new picture
                if (DEBUG) Log.i(LOG_TAG, "onCameraOrGallerySelected - take a new picture");
                reportingEvent.addEvent("picture_chosen_from","Selfie");
                if (checkFilePermission(REQUEST_WRITE_FILES_FROM_CAMERA)) {
                    dispatchTakePictureIntent();
                } else {
                    reportingEvent.addEvent("error","REQUEST_WRITE_FILES_FROM_CAMERA Not Allowed");
                }
                break;
        }
    }

    @Override
    public void onShareButtonPressed() {

        if (confirmPublish == null) return;

        if (reportingEvent == null) reportingEvent = new ReportingEvent();
        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);

        HashMap<String, String> tmp = confirmPublish.getAllDetails();
        String tmp2 = tmp.get(getString(R.string.drop_off_city));
        String mystr = getResources().getString(R.string.share_link_pt1) + " " + tmp2;
        mystr = mystr + " "+ getResources().getString(R.string.share_link_pt2);

        Intent sendIntent = returnShareIntent(mystr);
        startActivityForResult(
                Intent.createChooser(sendIntent, getResources().getText(R.string.share_with_friends)),
                REQUEST_SHARE_APP);
    }

    private Intent returnShareIntent (String mystr) {

        HashMap<String, String> u1 = new HashMap<>();
        u0.put(getString(R.string.fName),"shareIntent");
        u1.put(getString(R.string.fActions), "ShareTheApp");
        u1.put(getString(R.string.fStart), Long.toString(Utilities.CurrentTimeMS()));
//        new HandleReportingAsync().execute(u0,u1);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mystr);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    @Override
    public void onNoResultsShareButtonPressed() {

        if (reportingEvent == null) reportingEvent = new ReportingEvent();
        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String mystr = getResources().getString(R.string.share_link_noresults);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mystr);
        sendIntent.setType("text/plain");
        startActivityForResult(
                Intent.createChooser(sendIntent, getResources().getText(R.string.share_with_friends)),
                REQUEST_SHARE_APP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onOptionsItemSelected(MenuItem item) {
        HashMap<String, String> u1 = new HashMap<>();

        switch (item.getItemId()) {
            case R.id.menu_item_share:
                u0.put(getString(R.string.fName),"shareApplication");
                u1.put(getString(R.string.fActions), "shareApplication");
                u1.put(getString(R.string.fStart), Long.toString(Utilities.CurrentTimeMS()));
//                new HandleReportingAsync().execute(u0,u1);

                ShareActionProvider mShareActionProvider = (ShareActionProvider)
                        MenuItemCompat.getActionProvider(item);
                String tmp = getString(R.string.share_link_noresults);
                mShareActionProvider.setShareIntent(returnShareIntent(tmp));
                return true;

            case R.id.menu_item_contact:
                u0.put(getString(R.string.fName),"shareApplication");
                u1.put(getString(R.string.fActions), "shareApplication");
                u1.put(getString(R.string.fStart), Long.toString(Utilities.CurrentTimeMS()));
//                new HandleReportingAsync().execute(u0,u1);
                ShareActionProvider contactUsActionProvider = (ShareActionProvider)
                        MenuItemCompat.getActionProvider(item);
                contactUsActionProvider.setShareIntent(getEmailItentenForContact());

                return true;
            case R.id.menu_item_legal:
                u0.put(getString(R.string.fName),"privacyNotice");
                u1.put(getString(R.string.fActions), "privacyNotice");
                u1.put(getString(R.string.fStart), Long.toString(Utilities.CurrentTimeMS()));
//                new HandleReportingAsync().execute(u0,u1);
                this.OnPrivacyButtonPressed();


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onContactUsPressed() {
        HashMap<String, String> u1 = new HashMap<>();
        u0.put(getString(R.string.fName),"PrivacyNotice");
        u1.put(getString(R.string.fActions), "contactUs");
        u1.put(getString(R.string.fStart), Long.toString(privacyNotice.getFragmentStartTime()));
//        new HandleReportingAsync().execute(u0,u1);

        if (reportingEvent == null) reportingEvent = new ReportingEvent();
        // This is to make sure that when onStop is called, the session is not
        // ended in the reporting
        reportingEvent.setend_session(false);

        startActivity(Intent.createChooser(getEmailItentenForContact(), "Choose an Email client :"));
    }

    private Intent getEmailItentenForContact() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"shippy@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "My feedback about Shippy");
        email.putExtra(Intent.EXTRA_TEXT, "");
        email.setType("message/rfc822");
        return email;
    }


    /*
     This little class is called when the user takes a picture of himself. It crops, rotates if
     necessary and displays the picture on the registration page.
     */
    private class HandlePictureAsync extends AsyncTask<Bitmap, Void, Bitmap> {

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
            return cropAndScale(bitmap, 300);
        }


        protected void onPostExecute(Bitmap result) {
            if (PICTURE_FOR_CONFIRM && confirmPublish!=null){
                confirmPublish.setUserPicture(result);
                PICTURE_FOR_CONFIRM=false;
                return;
            }
            if (registrationFragment != null && !PICTURE_FOR_CONFIRM) {
                if (DEBUG) Log.i(LOG_TAG, "HandlePictureAsync - registrationFragment NOT NULL");
                registrationFragment.setUserPicture(result);
                registrationFragment.unsetCaption_user_picture();
            }

        }
    }

    /*
     This little class is called to geo-encode the (latitude, longitude) of any place
      it's done as an AsyncTask to avoid blocking the main UI thread
     */
    private class HandleGeoCodingAsync extends AsyncTask<LatLng, Void, HashMap<String, String>> {
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
                //TODO this seems to lead to some problems and this URL has a solution that doesn't invole
                // asking the user to restart their phone
                // http://stackoverflow.com/questions/19059894/google-geocoder-service-is-unavaliable-coordinates-to-address
                dataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "MainActivity",
                        "doInBackground");
                if (DEBUG) Log.e(LOG_TAG, "HandleGeoCodingAsync - " + e.getMessage());
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
                    editor.apply();
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
//    private class HandleReportingAsync extends AsyncTask<HashMap<String, Object>, Void, Void> {
//
//        @Override
//        protected Void doInBackground(HashMap<String, String>... params) {
//
//            Gson gson = new Gson();
//            if (params[1] == null) {
//                if (DEBUG) Log.i(LOG_TAG, "HandleReportingAsync - Leaving the app");
//                // If leaving the app
//                dataBuffer.addEvent(params[0]);
////                postUsageRequest(dataBuffer.getTheData());
//            } else {
//                if (DEBUG) Log.i(LOG_TAG, "HandleReportingAsync - Navigate around");
//                HashMap<String, String> tmp = new HashMap<>();
//                tmp.put(getString(R.string.sID), Long.toString(SESSION_ID));
//                tmp.put(getString(R.string.acStart), Long.toString(MAIN_ACTIVITY_START_TIME));
//                tmp.put(getString(R.string.acName), "mainActivity");
//                tmp.put(getString(R.string.fName), params[0].get(getString(R.string.fName)));
//                // If navigating from 1 fragment to another
//                tmp.put(getString(R.string.fActions), gson.toJson(params[1]));
//                dataBuffer.addEvent(tmp);
////                if (dataBuffer.ShouldIPostData()) postUsageRequest(dataBuffer.getTheData());
//            }
//
//            return null;
//        }
//
//    }
}
