package com.example.aalloul.packets;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferDetail extends Fragment {
    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = OfferDetail.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAMEANDFIRSTNAME = "nameAndFirstName";
    private static final String ARG_PICKUP_CITY = "pickup_city";
    private static final String ARG_DROPOFF_CITY = "dropoff_city";
    private static final String ARG_N_PACKETS = "n_packets";
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_COMMENTS = "comments";
    private static final String ARG_PICKUP_COUNTRY = "pickup_country";
    private static final String ARG_DROPOFF_COUNTRY = "dropoff_country";

    private String nameAndFirstName;
    private String pickup_city;
    private String dropoff_city;
    private String n_packets;
    private String phone_number;
    private String comments;
    private String dropoff_country;
    private String pickup_country;
    private long fragment_start_time;

    private ImageButton callButton;
    private ImageButton sendMessage;


    private OnFragmentInteractionListener mListener;

    public OfferDetail() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nameAndFirstName Parameter 1.
     * @param pickup_city Parameter 2.
     * @return A new instance of fragment OfferDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static OfferDetail newInstance(String nameAndFirstName, String pickup_city,
                                          String pickup_country, String dropoff_city,
                                          String dropoff_country,String n_packets,
                                          String phone_number, String comments) {
        Log.i(LOG_TAG, "newInstance - Start");
        OfferDetail fragment = new OfferDetail();
        Bundle args = new Bundle();
        Log.i(LOG_TAG, "newInstance - nameAndFirstName = " + nameAndFirstName);
        args.putString(ARG_NAMEANDFIRSTNAME, nameAndFirstName);
        Log.i(LOG_TAG, "newInstance - nameAndFirstName = " + pickup_city);
        args.putString(ARG_PICKUP_CITY, pickup_city);
        args.putString(ARG_DROPOFF_CITY, dropoff_city);
        args.putString(ARG_N_PACKETS, n_packets);
        args.putString(ARG_PHONE_NUMBER, phone_number);
        args.putString(ARG_COMMENTS, comments);
        args.putString(ARG_PICKUP_COUNTRY, pickup_country);
        args.putString(ARG_DROPOFF_COUNTRY, dropoff_country);

        fragment.setArguments(args);
        Log.i(LOG_TAG, "newInstance - Exit");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate - Start");
        if (getArguments() != null) {
            Log.i(LOG_TAG, "onCreate - getArguments is not null");
            nameAndFirstName = getArguments().getString(ARG_NAMEANDFIRSTNAME);
            Log.i(LOG_TAG, "onCreate - nameAndFirstName = "+ nameAndFirstName);
            pickup_city = getArguments().getString(ARG_PICKUP_CITY);
            dropoff_city = getArguments().getString(ARG_DROPOFF_CITY);
            n_packets = getArguments().getString(ARG_N_PACKETS);
            phone_number = getArguments().getString(ARG_PHONE_NUMBER);
            comments = getArguments().getString(ARG_COMMENTS);
            dropoff_country = getArguments().getString(ARG_DROPOFF_COUNTRY);
            pickup_country = getArguments().getString(ARG_PICKUP_COUNTRY);
        }
        Log.i(LOG_TAG, "onCreate - Exit");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(LOG_TAG, "onCreateView - Start");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);

        // Initialize the items of the view
        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        // Display name
        nameTextView.setText(nameAndFirstName);

        final TextView dropoffcity = (TextView) view.findViewById(R.id.going_to);
        dropoffcity.setText(dropoff_city);

        final TextView pickup_date = (TextView) view.findViewById(R.id.pickup_date_offer);
        pickup_date.setText("Feb 20");


        final TextView descriptionTextView = (TextView) view.findViewById(R.id.description);
        // Convert date to human readable
        String departure_date = "departure_date";
        String arrival_date = "arrival_date";
        // Build description
        String mystr = String.format("%s is going from %s (%s) to %s (%s). He will depart at " +
                "%s and should arrive around %s. \nTo call him, press the phone" +
                "icon below. \nYou can also send him a message.",
                nameAndFirstName, pickup_city, pickup_country, dropoff_city, dropoff_country,
                departure_date, arrival_date);
        descriptionTextView.setText(mystr);

        // Call action
        callButton = (ImageButton) view.findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallButtonPress(phone_number);
            }
        });

        // Message action
        sendMessage = (ImageButton) view.findViewById(R.id.messageButton);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMessageButtonPress(phone_number);
            }
        });

        Log.i(LOG_TAG, "onCreateView - Exit");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.i(LOG_TAG, "onAttach - Start");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.i(LOG_TAG, "onAttach - Exit");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public HashMap<String, String> getBlob(String nextFrag) {
        HashMap<String, String> t = new HashMap<>();

        long end_time = Utilities.CurrentTimeMS();

        t.put(getString(R.string.fStart), Long.toString(fragment_start_time));
        t.put(getString(R.string.fEnd), Long.toString(end_time));
        t.put(getString(R.string.fDuration), Long.toString(end_time - fragment_start_time));

        if (!nextFrag.equals("")) {
            //Used for data reporting when switching to another fragment
            t.put(getString(R.string.nextF), nextFrag);
        }

        return t;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCallButtonPress(String phoneNumber);
        void onMessageButtonPress(String phonenumber);
    }
}
