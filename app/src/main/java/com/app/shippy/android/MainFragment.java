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
import java.util.HashMap;


public class MainFragment extends Fragment {

    //TODO check why when user hits "register later" then "back button" then "register"
    // the location is not set in the UI
    private static final String ARG_CITY_STATE = "city_state";
    private static final String ARG_FIRST_NAME= "first_name";
    private View view;
    private TextView pickup_date;
    private Spinner number_packages, size_package;
    private TextView pickupdate_ui, drop_off_location, pickup_location;
    static String pickupdate, first_name;
    private final String LOG_TAG = com.app.shippy.android.MainFragment.class.getSimpleName();
    private FragmentActivity myContext;
    private HashMap<String, String> drop_off_detailed_location, pick_up_detailed_location;
    private long fragment_start_time;
    private Boolean edited_pickup_location = false;
    private com.app.shippy.android.MainFragment.OnFragmentInteractionListener mListener;
    private final static boolean DEBUG = false;
    private User user;

    public MainFragment() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public long getFragmentStartTime() {
        return fragment_start_time;
    }

    public static com.app.shippy.android.MainFragment newInstance() {
        com.app.shippy.android.MainFragment fragment = new com.app.shippy.android.MainFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    void setUser(User user) {
        this.user = user;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            pick_up_detailed_location = (HashMap<String, String>)
                    savedInstanceState.getSerializable("pick_up_location");
            drop_off_detailed_location = (HashMap<String, String>)
                    savedInstanceState.getSerializable("drop_off_location");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Enter");

        view= inflater.inflate(R.layout.fragment_main, container, false);
        getSearchButton();
        getPostButton();

        if (savedInstanceState == null) {
            if (DEBUG) Log.i(LOG_TAG, "onCreateView - savedInstance is null");
            getPickUpDateButton();
            getNumberPackagesButton();
            getSizePackagesButton();
            getPickUpLocationButton();
            getDropOffLocationButton();
        } else {
            if (DEBUG) Log.i(LOG_TAG, "onCreateView - savedInstance is not null");
            restorePickUpDateButton(savedInstanceState.getString("pick_up_date"));
            restoreNumberPackagesButton(savedInstanceState.getString("number_packages"));
            restoreSizePackagesButton(savedInstanceState.getString("size_packages"));
            restorePickupLocation((HashMap<String, String>)
                    savedInstanceState.getSerializable("pick_up_location"));
            restoreDropOffLocation((HashMap<String, String>)
                    savedInstanceState.getSerializable("drop_off_location"));
        }


        String text = getString(R.string.explanation_main_activity);

        if (first_name.equals("")) {
            text += " "+getString(R.string.explanation_main_activity2);
            text += getString(R.string.explanation_main_activity3);
        } else {
            text += " "+first_name;
            text += getString(R.string.explanation_main_activity3);
        }
        getActivity().setTitle(text);
        return view;
    }


    public void setEdited_pickup_location(){
        edited_pickup_location = true;
    }

    void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public boolean getEditedPickupLocation() {
        return edited_pickup_location;
    }

    private void getSearchButton() {
        Button searchButton = (Button) view.findViewById(R.id.searchbutton_main_activity);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSearchButtonPressed();
            }
        });
    }

    private void getPostButton() {
        Button postButton= (Button) view.findViewById(R.id.postbutton_main_activity);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPostButtonPressed();
            }
        });
    }

    private void getPickUpDateButton() {
//        pickupdate_ui = (TextView) view.findViewById(R.id.dateofpickup_mainActivity);


        pickup_date = (TextView) view.findViewById(R.id.setdate_mainActivity);
        setDate(Utilities.getTomorrow("yyyy-MM-dd"));
        pickup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment()
                        .show(myContext.getSupportFragmentManager(), "datePicker");
            }
        });
    }
    private void restorePickUpDateButton(String val) {
//        pickupdate_ui = (TextView) view.findViewById(R.id.dateofpickup_mainActivity);

        pickup_date = (TextView) view.findViewById(R.id.setdate_mainActivity);
        setDate(val);
        pickup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(myContext.getSupportFragmentManager(), "datePicker");
            }
        });
//        pickup_date.setText(val);
    }

    private void getNumberPackagesButton(){
        number_packages = (Spinner) view.findViewById(R.id.setNumberPackages);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterNPackages = ArrayAdapter.createFromResource(myContext,
                R.array.number_packages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterNPackages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        number_packages.setAdapter(adapterNPackages);
    }
    private void restoreNumberPackagesButton(String val){
        number_packages = (Spinner) view.findViewById(R.id.setNumberPackages);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterNPackages = ArrayAdapter.createFromResource(myContext,
                R.array.number_packages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterNPackages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        number_packages.setAdapter(adapterNPackages);
        number_packages.setSelection(adapterNPackages.getPosition(val));
    }

    private void getSizePackagesButton(){
        size_package = (Spinner) view.findViewById(R.id.packagesize);
        ArrayAdapter<CharSequence> adapterSizePackage = ArrayAdapter.createFromResource(myContext,
                R.array.package_size, android.R.layout.simple_spinner_item);

        adapterSizePackage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_package.setAdapter(adapterSizePackage);
    }
    private void restoreSizePackagesButton(String val) {
        size_package = (Spinner) view.findViewById(R.id.packagesize);
        ArrayAdapter<CharSequence> adapterSizePackage = ArrayAdapter.createFromResource(myContext,
                R.array.package_size, android.R.layout.simple_spinner_item);

        adapterSizePackage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_package.setAdapter(adapterSizePackage);
        size_package.setSelection(adapterSizePackage.getPosition(val));
    }

    private void getPickUpLocationButton() {
        pickup_location = (TextView) view.findViewById(R.id.pickuplocation_main_activity);
        _setPickup_location();
        if (DEBUG) Log.i(LOG_TAG, "pickup_location = "+pickup_location.getText().toString());
        pickup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPickUpLocationPressed();
            }
        });
    }
    private void restorePickupLocation(HashMap<String, String> val){
        pickup_location = (TextView) view.findViewById(R.id.pickuplocation_main_activity);
        setPickup_location(val);
        if (DEBUG) Log.i(LOG_TAG, "restorePickupLocation - pickup_location = "+pickup_location.getText().toString());
        pickup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPickUpLocationPressed();
            }
        });

    }

    private void getDropOffLocationButton() {
        // Drop-off and pick up location
        drop_off_location = (TextView) view.findViewById(R.id.dropofflocation_mainactivity);

        if (DEBUG) setDrop_off_location(pick_up_detailed_location);

        drop_off_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDropOffLocationPressed();
            }
        });
    }
    private void restoreDropOffLocation(HashMap<String, String> val) {
        // Drop-off and pick up location
        drop_off_location = (TextView) view.findViewById(R.id.dropofflocation_mainactivity);
        setDrop_off_location(val);
        drop_off_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDropOffLocationPressed();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putString("pick_up_date", pickupdate);
        if (size_package != null) {
            bundle.putString("size_packages",size_package.getSelectedItem().toString());
        }
        if (number_packages != null) {
            bundle.putString("number_packages", number_packages.getSelectedItem().toString());
        }
        bundle.putSerializable("pick_up_location", getPickupLocation());
        bundle.putSerializable("drop_off_location", getDropOffLocation());
    }

    @Override
    public void onAttach(Context context) {
        myContext= (FragmentActivity) context;
        super.onAttach(context);
        if (context instanceof com.app.shippy.android.MainFragment.OnFragmentInteractionListener) {
            mListener = (com.app.shippy.android.MainFragment.OnFragmentInteractionListener) context;
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


    public void setDate(String date) {
        // Most important stuff first
        if (DEBUG) Log.i(LOG_TAG, "setDate - date = " + date);
        pickupdate = date;
        pickup_date.setText(getResources().getString(R.string.explain_usage_search3) + ": " +
                Utilities.DateToDate(pickupdate, "yyyy-MM-dd","MMM dd" ));
//        pickupdate_ui.setText(Utilities.DateToDate(pickupdate, "yyyy-MM-dd","MMM dd" ));
    }

    public HashMap<String, String> getPickupLocation(){
        if (DEBUG) Log.i(LOG_TAG, "getPickupLocation - pickup_location is NOT null");
        return pick_up_detailed_location;
    }

    public HashMap<String, String> getDropOffLocation(){
//        if (drop_off_location == null || drop_off_location.getText() == null){
//            return null;
//        }
        if (DEBUG) Log.i(LOG_TAG, "getDropOffLocation - drop_off_detailed_location = "+drop_off_detailed_location);
        return drop_off_detailed_location;
    }

    public String getDateForPickUp(){
        if (pickupdate == null) {
            return null;
        }
        return Utilities.Date2EpochMillis(pickupdate, "yyyy-MM-dd");
    }

    public String getNumberPackages(){
        if (number_packages.getSelectedItem() == null ){
            return null;
        }
        return number_packages.getSelectedItem().toString();
    }

    public String getSizePackage(){
        if (size_package.getSelectedItem() == null ){
            return null;
        }
        return size_package.getSelectedItem().toString();
    }

    public void setDrop_off_location(HashMap<String, String> locationAddress){
        if (locationAddress == null) return;
        if (locationAddress.get(getString(R.string.saved_user_city)) == null) return;
        drop_off_detailed_location = locationAddress;
        String tmp = locationAddress.get(getString(R.string.saved_user_city));

        if (tmp.equals("")) return;

        if (locationAddress.get(getString(R.string.saved_user_state)) == null ) {
            tmp += " ("+Utilities.CountryToCountryCode(
                    locationAddress.get(getString(R.string.saved_user_country))) +")";
        } else {
            if (locationAddress.get(getString(R.string.saved_user_state)).equals("") ) {
                tmp += " ("+Utilities.CountryToCountryCode(
                        locationAddress.get(getString(R.string.saved_user_country))) +")";
            } else {
                tmp += " (" + locationAddress.get(getString(R.string.saved_user_state)) + ")";
            }
        }
        drop_off_location.setText(getResources().getString(R.string.explain_usage_search5) + ": "
                + tmp);
    }

    void set_detailed_pickup_location(String city, String state, String country) {
        pick_up_detailed_location.put(getString(R.string.saved_user_city), city);
        pick_up_detailed_location.put(getString(R.string.saved_user_state), state);
        pick_up_detailed_location.put(getString(R.string.saved_user_country), country);
    }
    private void _setPickup_location() {
        if (pick_up_detailed_location == null || pickup_location == null) return;
        if (!pick_up_detailed_location.containsKey(getString(R.string.saved_user_city))) return;
        if (!pick_up_detailed_location.containsKey(getString(R.string.saved_user_state))) return;

        if (DEBUG) Log.i(LOG_TAG, "_setPickup_location - city = "+
                pick_up_detailed_location.get(getString(R.string.saved_user_city)));
        String tmp = pick_up_detailed_location.get(getString(R.string.saved_user_city));

        // We decided to not show "updating" if the last location is unknown as we might never get
        // an update
        if (tmp.equals("")) {
            return;
        }

        if (pick_up_detailed_location.get(getString(R.string.saved_user_state)) == null) {
            tmp += " (" + Utilities.CountryToCountryCode(
                    pick_up_detailed_location.get(getString(R.string.saved_user_country))) + ")";
        } else {
            if (!pick_up_detailed_location.get(getString(R.string.saved_user_state)).equals("")) {
                tmp += " (" + pick_up_detailed_location.get(getString(R.string.saved_user_state)) + ")";
            } else {
                tmp += " (" + Utilities.CountryToCountryCode(
                        pick_up_detailed_location.get(getString(R.string.saved_user_country))) + ")";
            }
        }
        if (DEBUG) Log.i(LOG_TAG, "_setPickup_location - tmp = "+tmp);

        pickup_location.setText(getResources().getString(R.string.explain_usage_search2) + ": "+tmp);
    }

    public void setPickup_location(HashMap<String, String> locationAddress){
        pick_up_detailed_location = locationAddress;
        this._setPickup_location();
    }

    /**
     * Function that reads all input fields and returns their value in a HashMap
     * @param action whether this method is called after a search or a publish offer
     * @return HashMap of all input field values
     */
    public HashMap<String, String> getTripDetails(String action, String nextFrag) {
        HashMap<String, String> out = new HashMap<>();
        Long end_time = Utilities.CurrentTimeMS();

        // Input fields
        out.put(getString(R.string.date_for_pickup), getDateForPickUp());
        out.put(getString(R.string.number_packages), getNumberPackages());
        out.put(getString(R.string.size_packages), getSizePackage());

        // Drop location
        HashMap<String, String> t = getDropOffLocation();
        out.put(getString(R.string.drop_off_latitude),
                t.get(getString(R.string.saved_user_latitude)));
        out.put(getString(R.string.drop_off_longitude),
                t.get(getString(R.string.saved_user_longitude)));
        out.put(getString(R.string.drop_off_address),
                t.get(getString(R.string.saved_user_address)));
        out.put(getString(R.string.drop_off_city),
                t.get(getString(R.string.saved_user_city)));
        out.put(getString(R.string.drop_off_country),
                t.get(getString(R.string.saved_user_country)));
        out.put(getString(R.string.drop_off_postalcode),
                t.get(getString(R.string.saved_user_postalcode)));
        out.putAll(getPickupLocation());

        // Pick up location
        out.put(getString(R.string.pickup_location_edited), Boolean.toString(edited_pickup_location));

        return out;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchButtonPressed();
        void onPostButtonPressed();
        void onDropOffLocationPressed();
        void onPickUpLocationPressed();
    }
}
