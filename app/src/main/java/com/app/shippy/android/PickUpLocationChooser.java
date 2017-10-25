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


public class PickUpLocationChooser extends Fragment {
    private long fragment_start_time;
    private final static boolean DEBUG = true;
    private final static String LOG_TAG = "PickUpLocationChooser";
    private OnPickUpChooserInteraction mListener;
    private View view;
    private FragmentActivity myContext;
    private TextView pickUpLocation;
    private Button next_button;
    private boolean hasEditedPickupLocation = false;
    final static int PICKUP_DATE_MISSING = 1;
    final static int PICKUP_LOCATION_MISSING = 2;
    final static int ALL_GOOD = 0;

    public PickUpLocationChooser() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public long getFragmentStartTime() {
        return fragment_start_time;
    }

    public static PickUpLocationChooser newInstance() {
        if (DEBUG) Log.i("PickUpLocationChooser","newInstsance - Enter");
        return new PickUpLocationChooser();
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

        view = inflater.inflate(R.layout.fragment_pickup_location_chooser, container, false);

        setNextButton();

        // Pickup button
        pickUpLocation = (TextView) view.findViewById(R.id.pickup_chooser_pickup_city);
        pickUpLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPickUpLocationPressed();
            }
        });
        setPickupLocation(mListener.getTripRequestDetailsToPickUpChooser().getPickupLocation());
        setPickupDate(mListener.getTripRequestDetailsToPickUpChooser().getPickup_date());

        setActivityTitle();
        setSendOrTravelText();

        return view;
    }

    private void setSendOrTravelText() {
        TextView send_or_travel = (TextView) view.findViewById(R.id.pickup_chooser_send_or_travel_text);
        if (mListener.getTripRequestDetailsToPickUpChooser().isSearching()) {
            send_or_travel.setText(R.string.main_i_am_sending);
        } else {
            send_or_travel.setText(R.string.main_i_am_travelling);
        }
    }

    private void setActivityTitle() {
        String text = getString(R.string.explanation_main_activity);

        if (mListener.getUserForPickupChooser().getFirstname().equals("")) {
            text += " "+getString(R.string.explanation_main_activity2);
            text += getString(R.string.explanation_main_activity3);
        } else {
            text += " "+mListener.getUserForPickupChooser().getFirstname();
            text += getString(R.string.explanation_main_activity3);
        }
        getActivity().setTitle(text);
    }

    private void setPickupLocation(LocationObject locationObject) {

        if (locationObject == null) return;

        if (locationObject.getCity().equals("")) {
            return;}

        pickUpLocation.setText(locationObject.getCity());
        setNextButtonColor();

    }

    private void setPickupDate(long date) {

        TextView pickupDate = (TextView) view.findViewById(R.id.pickup_chooser_pickup_date);
        pickupDate.setText(Utilities.Epoch2Date(date, "yyyy-MM-dd"));
        pickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment()
                        .show(myContext.getSupportFragmentManager(), "datePicker");
            }
        });
        setNextButtonColor();
    }

    private void setNextButton() {
        next_button = (Button) view.findViewById(R.id.pickup_chooser_next);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPickupNextButtonPressed();
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
        if (context instanceof OnPickUpChooserInteraction) {
            mListener = (OnPickUpChooserInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPickUpChooserInteraction");
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
        setPickupDate(mListener.getTripRequestDetailsToPickUpChooser().getPickup_date());
    }

    void updatePickupLocation() {
        setPickupLocation(mListener.getTripRequestDetailsToPickUpChooser().getPickupLocation());
    }


    int checkInputs() {

        if (pickUpLocation == null || pickUpLocation.getText().toString().equals("")) {
            return PICKUP_LOCATION_MISSING;
        }

        return ALL_GOOD;
    }

    interface OnPickUpChooserInteraction {
        void onPickupNextButtonPressed();
        void onPickUpLocationPressed();
        TripRequestDetails getTripRequestDetailsToPickUpChooser();
        User getUserForPickupChooser();
    }
}
