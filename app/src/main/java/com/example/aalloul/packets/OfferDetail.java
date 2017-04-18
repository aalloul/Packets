package com.example.aalloul.packets;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;

import java.util.HashMap;


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
    private static final String ARG_PACKAGE_SIZE = "package_size";
    private static final String ARG_PICTURE = "picture";
    private static final String ARG_PICKUP_DATE = "pickup_date";
    private static final String ARG_TRANSPORT_METHOD = "transport_method";
    private String nameAndFirstName;
    private String pickup_city;
    private String dropoff_city;
    private String n_packets;
    private String phone_number;
    private String comments;
    private String dropoff_country;
    private String pickup_country;
    private String picture;
    private String package_size;
    private String pickup_date;
    private long fragment_start_time;
    private OnFragmentInteractionListener mListener;
    private final static boolean DEBUG = false;
    private String transport_method;

    public OfferDetail() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public static OfferDetail newInstance(String nameAndFirstName, String pickup_city,
                                          String pickup_country, String pickup_date,
                                          String dropoff_city, String dropoff_country,
                                          String n_packets, String package_size, String picture,
                                          String phone_number, String transport_methods,
                                          String comments) {
        if (DEBUG) Log.i(LOG_TAG, "newInstance - Start");
        OfferDetail fragment = new OfferDetail();
        Bundle args = new Bundle();
        if (DEBUG) Log.i(LOG_TAG, "newInstance - nameAndFirstName = " + nameAndFirstName);
        args.putString(ARG_NAMEANDFIRSTNAME, nameAndFirstName);
        if (DEBUG) Log.i(LOG_TAG, "newInstance - nameAndFirstName = " + pickup_city);
        args.putString(ARG_PICKUP_CITY, pickup_city);
        args.putString(ARG_DROPOFF_CITY, dropoff_city);
        args.putString(ARG_N_PACKETS, n_packets);
        args.putString(ARG_PHONE_NUMBER, phone_number);
        args.putString(ARG_COMMENTS, comments);
        args.putString(ARG_PICKUP_COUNTRY, pickup_country);
        args.putString(ARG_DROPOFF_COUNTRY, dropoff_country);
        args.putString(ARG_PACKAGE_SIZE, package_size);
        args.putString(ARG_PICTURE, picture);
        args.putString(ARG_PICKUP_DATE, pickup_date);
        args.putString(ARG_TRANSPORT_METHOD, transport_methods);

        fragment.setArguments(args);
        if (DEBUG) Log.i(LOG_TAG, "newInstance - Exit");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(LOG_TAG, "onCreate - Start");
        if (getArguments() != null) {
            if (DEBUG) Log.i(LOG_TAG, "onCreate - getArguments is not null");
            nameAndFirstName = getArguments().getString(ARG_NAMEANDFIRSTNAME);
            if (DEBUG) Log.i(LOG_TAG, "onCreate - nameAndFirstName = "+ nameAndFirstName);
            pickup_city = getArguments().getString(ARG_PICKUP_CITY);
            dropoff_city = getArguments().getString(ARG_DROPOFF_CITY);
            n_packets = getArguments().getString(ARG_N_PACKETS);
            phone_number = getArguments().getString(ARG_PHONE_NUMBER);
            comments = getArguments().getString(ARG_COMMENTS);
            dropoff_country = getArguments().getString(ARG_DROPOFF_COUNTRY);
            pickup_country = getArguments().getString(ARG_PICKUP_COUNTRY);
            package_size = getArguments().getString(ARG_PACKAGE_SIZE);
            picture = getArguments().getString(ARG_PICTURE);
            pickup_date = getArguments().getString(ARG_PICKUP_DATE);
            transport_method = getArguments().getString(ARG_TRANSPORT_METHOD);
        }
        if (DEBUG) Log.i(LOG_TAG, "onCreate - Exit");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Start");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);

        // Initialize the items of the view
        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        // Display name
        nameTextView.setText(nameAndFirstName);

        final TextView dropoffcity = (TextView) view.findViewById(R.id.going_to);
        dropoffcity.setText(dropoff_city);

        final TextView pickup_date_ui = (TextView) view.findViewById(R.id.pickup_date_offer);
        pickup_date_ui.setText(Utilities.Epoch2Date(pickup_date, "dd MMM"));

        final ImageView picture_ui = (ImageView) view.findViewById(R.id.transporter_picture_detail);

        if (picture != null && !picture.equals("")) {
            picture_ui.setImageBitmap(Utilities.StringToBitMap(picture));
            picture_ui.setScaleX(1);
            picture_ui.setScaleY(1);
            picture_ui.setBackground(null);
            picture_ui.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        final TextView descriptionTextView = (TextView) view.findViewById(R.id.comment_transporter);
        if (comments == null || comments.equals("") ) {
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
        } else {
            descriptionTextView.setText(comments);
        }

        switch (package_size) {
            case "Small":
                ((TextView) view.findViewById(R.id.size_package_detail)).setText("S");
                break;
            case "Medium":
                ((TextView) view.findViewById(R.id.size_package_detail)).setText("M");
                break;
            case "Large":
                ((TextView) view.findViewById(R.id.size_package_detail)).setText("L");
                break;
        }

        ((TextView) view.findViewById(R.id.number_packages_detail)).setText("x"+n_packets);

        switch (transport_method) {
            case "Car":
                ((ImageView) view.findViewById(R.id.travel_by)).setImageResource(R.drawable.car);
                break;
            case "Train":
                ((ImageView) view.findViewById(R.id.travel_by)).setImageResource(R.drawable.train);
                break;
            case "Plane":
                ((ImageView) view.findViewById(R.id.travel_by)).setImageResource(R.drawable.plane);
                break;
        }

        // Call action
        ImageButton callButton = (ImageButton) view.findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallButtonPress(phone_number);
            }
        });

        // Message action
        ImageButton sendMessage = (ImageButton) view.findViewById(R.id.messageButton);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMessageButtonPress(phone_number);
            }
        });

        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Exit");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (DEBUG) Log.i(LOG_TAG, "onAttach - Start");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (DEBUG) Log.i(LOG_TAG, "onAttach - Exit");
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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCallButtonPress(String phoneNumber);
        void onMessageButtonPress(String phonenumber);
    }
}
