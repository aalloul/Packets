package com.app.shippy.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class MainFragment extends Fragment {
    private long fragment_start_time;
    private final static boolean DEBUG = true;
    private final static String LOG_TAG = "MainFragment";
    private OnMainFragmentInteractionListener mListener;
    private View view;
    private FragmentActivity myContext;
    private TextView dropOffLocation, pickUpLocation, pickupDate;
    private Spinner number_packages, size_package;
    private Button searchButton, postButton;
    private boolean hasEditedPickupLocation = false;
    final static int DROPOFF_LCATION_MISSING = 1;
    final static int PICKUP_LOCATION_MISSING = 2;
    final static int ALL_GOOD = 0;

    public MainFragment() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public long getFragmentStartTime() {
        return fragment_start_time;
    }

    public static MainFragment newInstance() {
        if (DEBUG) Log.i("MainFragment","newInstsance - Enter");
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Enter");

        view = inflater.inflate(R.layout.fragment_main, container, false);

        // DropOff button
        dropOffLocation = (TextView) view.findViewById(R.id.dropofflocation_mainactivity);
        dropOffLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDropOffLocationPressed();
            }
        });
        setDropOffLocation(mListener.getTripRequestDetailsToMainFragment().getDropoffLocation());

        // Pickup button
        pickUpLocation = (TextView) view.findViewById(R.id.pickuplocation_main_activity);
        pickUpLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPickUpLocationPressed();
            }
        });
        setPickupLocation(mListener.getTripRequestDetailsToMainFragment().getPickupLocation());
        setPickupDate(mListener.getTripRequestDetailsToMainFragment().getPickup_date());

        setActivityTitle();
        setNumberPackages();
        setSizePackages();

        setPostButton();
        setSearchButton();

        return view;
    }

    private void setActivityTitle() {
        String text = getString(R.string.explanation_main_activity);

        if (mListener.getUserForMainFragment().getFirstname().equals("")) {
            text += " "+getString(R.string.explanation_main_activity2);
            text += getString(R.string.explanation_main_activity3);
        } else {
            text += " "+mListener.getUserForMainFragment().getFirstname();
            text += getString(R.string.explanation_main_activity3);
        }
        getActivity().setTitle(text);
    }

    private void setNumberPackages() {
        number_packages = (Spinner) view.findViewById(R.id.setNumberPackages);
        ArrayAdapter<CharSequence> adapterNPackages = ArrayAdapter.createFromResource(myContext,
                R.array.number_packages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterNPackages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        number_packages.setAdapter(adapterNPackages);
    }

    private void setSizePackages() {
        size_package = (Spinner) view.findViewById(R.id.packagesize);
        ArrayAdapter<CharSequence> adapterSizePackage = ArrayAdapter.createFromResource(myContext,
                R.array.package_size, android.R.layout.simple_spinner_item);

        adapterSizePackage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_package.setAdapter(adapterSizePackage);
    }

    private void setPickupLocation(LocationObject locationObject) {

        String tmp = getResources().getString(R.string.explain_usage_search2) + ": ";

        if (locationObject.getCity().equals("")) {
            return;}

        tmp += locationObject.getCity();

        if (locationObject.getState().equals("")) {
            pickUpLocation.setText(tmp);
            return;
        }

        tmp += " ("+locationObject.getState() +")";
        pickUpLocation.setText(tmp);

    }

    private void setDropOffLocation(LocationObject locationObject) {

        String tmp = getResources().getString(R.string.explain_usage_search5) + ": ";

        if (locationObject.getCity().equals("")) {return;}
        tmp += locationObject.getCity();

        if (locationObject.getState().equals("")) {
            dropOffLocation.setText(tmp);
            return;
        }

        tmp += " ("+locationObject.getState() +")";
        dropOffLocation.setText(tmp);

    }

    private void setPickupDate(long date) {
        pickupDate = (TextView) view.findViewById(R.id.setdate_mainActivity);
        String tmp = getResources().getString(R.string.explain_usage_search3) + ": ";
        tmp += Utilities.Epoch2Date(date, "yyyy-MM-dd");

        pickupDate.setText(tmp);
        pickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment()
                        .show(myContext.getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private void setPostButton() {
        postButton= (Button) view.findViewById(R.id.postbutton_main_activity);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPostButtonPressed();
            }
        });
    }

    private void setSearchButton() {
        searchButton = (Button) view.findViewById(R.id.searchbutton_main_activity);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSearchButtonPressed();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        myContext= (FragmentActivity) context;
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    boolean hasEditedPickupLocation() {
        return hasEditedPickupLocation;
    }

    void setHasEditedPickupLocation(boolean bool) {
        hasEditedPickupLocation = bool;
    }

    void updatePickuppDate() {
        setPickupDate(mListener.getTripRequestDetailsToMainFragment().getPickup_date());
    }

    void updatePickupLocation() {
        setPickupLocation(mListener.getTripRequestDetailsToMainFragment().getPickupLocation());
    }

    void updateDropOffLocation() {
        setDropOffLocation(mListener.getTripRequestDetailsToMainFragment().getDropoffLocation());
    }

    int checkInputs() {
        if (dropOffLocation == null || dropOffLocation.getText().toString().equals("")) {
            return DROPOFF_LCATION_MISSING;
        }

        if (pickUpLocation == null || pickUpLocation.getText().toString().equals("")) {
            return PICKUP_LOCATION_MISSING;
        }

        /* Here we do not update getTripRequestDetailsToMainFragment with
            dropOffLocation
            pickupLocation
            pickup_date
           because they're supposed to be updated by the call-backs in the MainActivity.
           Only the fields package_size and number_packages need to be updated.
           For the latter number_packages, as JAVA is 0 based, we increment the value returned by
           getSelectedItemPosition() by +1 to avoid any confusion (esp. when the user wants to
           transport only 1 package)
        */

        mListener.getTripRequestDetailsToMainFragment().setPackage_size_int(
                size_package.getSelectedItemPosition());

        mListener.getTripRequestDetailsToMainFragment().setNumber_packages(
                number_packages.getSelectedItemPosition() + 1);

        return ALL_GOOD;
    }

    interface OnMainFragmentInteractionListener {
        void onSearchButtonPressed();
        void onPostButtonPressed();
        void onDropOffLocationPressed();
        void onPickUpLocationPressed();
        TripRequestDetails getTripRequestDetailsToMainFragment();
        User getUserForMainFragment();
    }
}
