package com.app.shippy.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DropOffLocationChooser extends Fragment {
    private long fragment_start_time;
    private final static boolean DEBUG = true;
    private final static String LOG_TAG = "DropOffLocationChooser";
    private OnDropOffChooserInteraction mListener;
    private View view;
    private FragmentActivity myContext;
    private TextView dropOffLocation;
    private Button next_button;
    private boolean hasEditeddropOffLocation = false;
    final static int DROPOFF_LCATION_MISSING = 1;
    final static int ALL_GOOD = 0;

    public DropOffLocationChooser() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public long getFragmentStartTime() {
        return fragment_start_time;
    }

    public static DropOffLocationChooser newInstance() {
        if (DEBUG) Log.i("dropOffLocationChooser","newInstsance - Enter");
        return new DropOffLocationChooser();
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

        view = inflater.inflate(R.layout.fragment_drop_off_location_chooser, container, false);

        setNextButton();

        // Pickup button
        dropOffLocation = (TextView) view.findViewById(R.id.dropoff_chooser_dropoff_city);
        dropOffLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDropoffLocationPressed();
            }
        });
        setdropOffLocation(mListener.getTripRequestDetailsToDropOffChooser().getDropoffLocation());
        setDropOffDate(mListener.getTripRequestDetailsToDropOffChooser().getPickup_date());

        setActivityTitle();
        setSendOrTravelText();
        setPickupPlace();
        return view;
    }

    private void setSendOrTravelText() {
        TextView send_or_travel = (TextView) view.findViewById(R.id.dropoff_chooser_send_or_travel_text);
        if (mListener.getTripRequestDetailsToDropOffChooser().isSearching()) {
            send_or_travel.setText(R.string.main_i_am_sending);
        } else {
            send_or_travel.setText(R.string.main_i_am_travelling);
        }
    }

    private void setPickupPlace() {
        TextView pickup_location = (TextView) view.findViewById(R.id.dropoff_chooser_details_text);
        if (mListener == null || mListener.getTripRequestDetailsToDropOffChooser() == null || mListener.getTripRequestDetailsToDropOffChooser().getPickupLocation() == null) {
            return;
        }

        if (mListener.getTripRequestDetailsToDropOffChooser().getPickupLocation().getCity().equals("")) {
            return;
        }

        pickup_location.setText(
                mListener.getTripRequestDetailsToDropOffChooser().getPickupLocation().getCity());
    }

    private void setActivityTitle() {
        String text = getString(R.string.explanation_main_activity);

        if (mListener.getUserForDropOff().getFirstname().equals("")) {
            text += " "+getString(R.string.explanation_main_activity2);
            text += getString(R.string.explanation_main_activity3);
        } else {
            text += " "+mListener.getUserForDropOff().getFirstname();
            text += getString(R.string.explanation_main_activity3);
        }
        getActivity().setTitle(text);
    }

    private void setdropOffLocation(LocationObject locationObject) {

        Log.i(LOG_TAG, "setdropOffLocation - Enter");
        if (locationObject == null || locationObject.getCity() == null) {
            Log.i(LOG_TAG, "setdropOffLocation - locationObject is null");
            return;
        }

        if (locationObject.getCity().equals("")) {
            Log.i(LOG_TAG, "setdropOffLocation - city is empty");
            return;
        }
        Log.i(LOG_TAG, "setdropOffLocation - city = "+locationObject.getCity());

        dropOffLocation.setText(locationObject.getCity());
        setNextButtonColor();

    }

    private void setDropOffDate(long date) {
        TextView dropOffDate = (TextView) view.findViewById(R.id.dropoff_chooser_dropoff_date);
        dropOffDate.setText(Utilities.Epoch2Date(date, "yyyy-MM-dd"));
        dropOffDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment()
                        .show(myContext.getSupportFragmentManager(), "datePicker");
            }
        });
        setNextButtonColor();
    }

    private void setNextButton() {
        next_button = (Button) view.findViewById(R.id.dropoff_chooser_next);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDropOffNextButtonPressed();
            }
        });
        setNextButtonColor();
    }

    private void setNextButtonColor() {
        if (checkInputs() == ALL_GOOD) {
            next_button.setBackgroundTintList(ContextCompat.getColorStateList(
                    getActivity(), R.color.colorSecondary));
        } else {
            next_button.setBackgroundTintList(ContextCompat.getColorStateList(
                    getActivity(), R.color.backgroundApp));
        }
    }

    @Override
    public void onAttach(Context context) {
        myContext= (FragmentActivity) context;
        super.onAttach(context);
        if (context instanceof OnDropOffChooserInteraction) {
            mListener = (OnDropOffChooserInteraction) context;
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

    boolean hasEditeddropOffLocation() {
        return hasEditeddropOffLocation;
    }

    void setHasEditeddropOffLocation(boolean bool) {
        hasEditeddropOffLocation = bool;
    }

    void updatePickuppDate() {
        setDropOffDate(mListener.getTripRequestDetailsToDropOffChooser().getPickup_date());
    }

    void updatedropOffLocation() {
        setdropOffLocation(mListener.getTripRequestDetailsToDropOffChooser().getDropoffLocation());
    }


    int checkInputs() {

        if (dropOffLocation == null || dropOffLocation.getText().toString().equals("")) {
            return DROPOFF_LCATION_MISSING;
        }

        return ALL_GOOD;
    }

    interface OnDropOffChooserInteraction {
        void onDropOffNextButtonPressed();
        void onDropoffLocationPressed();
        TripRequestDetails getTripRequestDetailsToDropOffChooser();
        User getUserForDropOff();
    }
}
