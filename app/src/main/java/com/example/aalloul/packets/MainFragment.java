package com.example.aalloul.packets;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
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
    private static final String ARG_CITY_STATE = "city_state";
    private Button searchButton, postButton, pickup_date;
    private Spinner number_packages, size_package;
    private TextView pickupdate_ui, numberpackages, drop_off_location, pickup_location;
    static String pickupdate;
    private final String LOG_TAG = MainFragment.class.getName();
    private FragmentActivity myContext;
    private HashMap<String, String> drop_off_detailed_location, pick_up_detailed_location;


    private HashMap<String, String> location;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inlocation Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(HashMap<String, String> inlocation) {
        Log.i("MainFragment","newInstsance - Enter");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        Log.i("MainFragment","newInstsance - "+ inlocation.toString());
        args.putSerializable(ARG_CITY_STATE, inlocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pick_up_detailed_location = (HashMap<String, String>)
                    getArguments().getSerializable(ARG_CITY_STATE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        searchButton = (Button) view.findViewById(R.id.searchbutton_main_activity);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSearchButtonPressed();
            }
        });


        postButton= (Button) view.findViewById(R.id.postbutton_main_activity);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPostButtonPressed();
            }
        });

        pickupdate_ui = (TextView) view.findViewById(R.id.dateofpickup_mainActivity);
        setDate(Utilities.getTomorrow("yyyy-MM-dd"));

        pickup_date = (Button) view.findViewById(R.id.setdate_mainActivity);
        pickup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(myContext.getSupportFragmentManager(), "datePicker");
            }
        });

        numberpackages = (TextView) view.findViewById(R.id.numberOfPackages_mainActivity);
        number_packages = (Spinner) view.findViewById(R.id.setNumberPackages);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterNPackages = ArrayAdapter.createFromResource(myContext,
                R.array.number_packages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterNPackages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        number_packages.setAdapter(adapterNPackages);

        size_package = (Spinner) view.findViewById(R.id.packagesize);
        ArrayAdapter<CharSequence> adapterSizePackage = ArrayAdapter.createFromResource(myContext,
                R.array.package_size, android.R.layout.simple_spinner_item);
        adapterSizePackage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_package.setAdapter(adapterSizePackage);

        // Drop-off and pick up location
        drop_off_location = (TextView) view.findViewById(R.id.dropofflocation_mainactivity);
        drop_off_location.setText("Amsterdam (NL)");
        drop_off_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDropOffLocationPressed();
            }
        });

        pickup_location = (TextView) view.findViewById(R.id.pickuplocation_main_activity);
        _setPickup_location();
        Log.i(LOG_TAG, "pickup_location = "+pickup_location.getText().toString());
        pickup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPickUpLocationPressed();
            }
        });
        return view;
    }




    @Override
    public void onAttach(Context context) {
        myContext= (FragmentActivity) context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        pickupdate = date;
        pickupdate_ui.setText(Utilities.DateToDate(pickupdate, "yyyy-MM-dd","MMM dd" ));
    }

    public HashMap<String, String> getPickupLocation(){
        if (pickup_location.getText() == null ) {
            return null;
        }
        return pick_up_detailed_location;
    }

    public HashMap<String, String> getDropOffLocation(){
        if (drop_off_location.getText() == null){
            return null;
        }
        return drop_off_detailed_location;
    }

    public String getDateForPickUp(){
        if (pickupdate == null) {
            return null;
        }
        return pickupdate;
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
        drop_off_detailed_location = locationAddress;
        String tmp = locationAddress.get(getString(R.string.saved_user_city));
        tmp += " ("+locationAddress.get(getString(R.string.saved_user_state)) +")";
        drop_off_location.setText(tmp);
        return;
    }

    private void _setPickup_location() {
        if (pick_up_detailed_location == null) return;
        if (!pick_up_detailed_location.containsKey(getString(R.string.saved_user_city))) return;
        if (!pick_up_detailed_location.containsKey(getString(R.string.saved_user_state))) return;

        Log.i(LOG_TAG, "_setPickup_location - city = "+
                pick_up_detailed_location.get(getString(R.string.saved_user_city)));
        String tmp = pick_up_detailed_location.get(getString(R.string.saved_user_city));

        if (tmp.equals("Updating")) {
            pickup_location.setText(getString(R.string.updating_location));
            return;
        }

        if (!pick_up_detailed_location.get(getString(R.string.saved_user_state)).equals("")) {
            tmp += " (" + pick_up_detailed_location.get(getString(R.string.saved_user_state)) + ")";
        } else {
            tmp += " (" + Utilities.CountryToCountryCode(
                    pick_up_detailed_location.get(getString(R.string.saved_user_country))) + ")";
        }

        Log.i(LOG_TAG, "_setPickup_location - tmp = "+tmp);

        pickup_location.setText(tmp);
    }

    public void setPickup_location(HashMap<String, String> locationAddress){
        pick_up_detailed_location = locationAddress;
        this._setPickup_location();
    }

    public HashMap<String, String> getDrop_off_detailed_location(){
        return drop_off_detailed_location;
    }

    public HashMap<String, String> getPick_up_detailed_location() {
        return pick_up_detailed_location;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchButtonPressed();
        void onPostButtonPressed();
        void onDropOffLocationPressed();
        void onPickUpLocationPressed();
    }
}
