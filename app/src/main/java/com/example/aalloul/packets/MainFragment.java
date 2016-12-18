package com.example.aalloul.packets;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;


public class MainFragment extends Fragment {
    private static final String ARG_LATLNG = "latlng";
    private Button searchButton, postButton, pickup_date;
    private Spinner number_packages, size_package;
    private EditText pickup_location, drop_off_location;
    private TextView pickupdate, numberpackages;
    static String dateForPickUp;
    private final String LOG_TAG = MainFragment.class.getName();
    private FragmentActivity myContext;

    // TODO: Rename and change types of parameters
    private String mLatLng;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latlng Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(String latlng) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LATLNG, latlng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLatLng = getArguments().getString(ARG_LATLNG);
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

        pickupdate = (TextView) view.findViewById(R.id.dateofpickup_mainActivity);
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
        drop_off_location = (EditText) view.findViewById(R.id.dropofflocation_mainactivity);
        pickup_location = (EditText) view.findViewById(R.id.pickuplocation_main_activity);
        pickup_location.setText(null);

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
        dateForPickUp = date;
        pickupdate.setText(Utilities.DateToDate(dateForPickUp, "yyyy-MM-dd","MMM dd" ));

    }

    public String getPickupLocation(){
        if (pickup_location.getText() == null ) {
            return null;
        }
        return pickup_location.getText().toString();
    }

    public String getDropOffLocation(){
        if (drop_off_location.getText() == null){
            return null;
        }
        return drop_off_location.getText().toString();
    }

    public String getDateForPickUp(){
        if (dateForPickUp == null) {
            return null;
        }
        return dateForPickUp;
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
    }
}
