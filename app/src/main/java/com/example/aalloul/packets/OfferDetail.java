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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
class OfferDetail extends Fragment {
    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = OfferDetail.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAMEANDFIRSTNAME = "nameAndFirstName";
    private static final String ARG_SOURCE_CITY = "source_city";
    private static final String ARG_DESTINATION_CITY = "destination_city";
    private static final String ARG_N_PACKETS = "n_packets";
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_COMMENTS = "comments";
    private static final String ARG_SOURCE_COUNTRY = "source_country";
    private static final String ARG_DESTINATION_COUNTRY = "destination_country";

    private String nameAndFirstName;
    private String source_city;
    private String destination_city;
    private String n_packets;
    private String phone_number;
    private String comments;
    private String destination_country;
    private String source_country;

    private ImageButton callButton;
    private ImageButton sendMessage;


    private OnFragmentInteractionListener mListener;

    public OfferDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nameAndFirstName Parameter 1.
     * @param source_city Parameter 2.
     * @return A new instance of fragment OfferDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static OfferDetail newInstance(String nameAndFirstName, String source_city,
                                          String source_country, String destination_city,
                                          String destination_country,String n_packets,
                                          String phone_number, String comments) {
        Log.i(LOG_TAG, "newInstance - Start");
        OfferDetail fragment = new OfferDetail();
        Bundle args = new Bundle();
        Log.i(LOG_TAG, "newInstance - nameAndFirstName = " + nameAndFirstName);
        args.putString(ARG_NAMEANDFIRSTNAME, nameAndFirstName);
        Log.i(LOG_TAG, "newInstance - nameAndFirstName = " + source_city);
        args.putString(ARG_SOURCE_CITY, source_city);
        args.putString(ARG_DESTINATION_CITY, destination_city);
        args.putString(ARG_N_PACKETS, n_packets);
        args.putString(ARG_PHONE_NUMBER, phone_number);
        args.putString(ARG_COMMENTS, comments);
        args.putString(ARG_SOURCE_COUNTRY, source_country);
        args.putString(ARG_DESTINATION_COUNTRY, destination_country);

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
            source_city = getArguments().getString(ARG_SOURCE_CITY);
            destination_city = getArguments().getString(ARG_DESTINATION_CITY);
            n_packets = getArguments().getString(ARG_N_PACKETS);
            phone_number = getArguments().getString(ARG_PHONE_NUMBER);
            comments = getArguments().getString(ARG_COMMENTS);
            destination_country = getArguments().getString(ARG_DESTINATION_COUNTRY);
            source_country = getArguments().getString(ARG_SOURCE_COUNTRY);
        }
        Log.i(LOG_TAG, "onCreate - Exit");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(LOG_TAG, "onCreateView - Start");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);

        // Initialize the items of the view
//        final ImageView imageView = (ImageView) view.findViewById(R.id.comic_image);
        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView descriptionTextView = (TextView) view.findViewById(R.id.description);

        // get the arguments
        Log.i(LOG_TAG, "onCreateView - get Arguments");
        final Bundle args = getArguments();
//        imageView.setImageResource(args.getInt(ARGUMENT_IMAGE_RES_ID));

        Log.i(LOG_TAG, "onCreateView - set values for the view");

        // Display name
        nameTextView.setText(args.getString(nameAndFirstName));

        // Convert date to human readable
        String departure_date = "departure_date";
        String arrival_date = "arrival_date";
        // Build description
        String mystr = String.format("%s is going from %s (%s) to %s (%s). He will depart at " +
                "%s and should arrive around %s. \nTo call him, press the phone" +
                "icon below. \nYou can also send him a message.",
                args.getString(ARG_NAMEANDFIRSTNAME), args.getString(ARG_SOURCE_CITY),
                args.getString(ARG_SOURCE_COUNTRY), args.getString(ARG_DESTINATION_CITY),
                args.getString(ARG_DESTINATION_COUNTRY), departure_date, arrival_date);
        descriptionTextView.setText(mystr);

        // Call action
        callButton = (ImageButton) view.findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallButtonPress(args.getString(ARG_PHONE_NUMBER));
            }
        });

        // Message action
        sendMessage = (ImageButton) view.findViewById(R.id.messageButton);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMessageButtonPress(args.getString(ARG_PHONE_NUMBER));
            }
        });

        Log.i(LOG_TAG, "onCreateView - Exit");
        return view;
    }

//    public void onButtonPressed(Uri uri) {
//        Log.i(LOG_TAG, "onButtonPressed - Start");
////        if (mListener != null) {
////            mListener.onFragmentInteraction(uri);
////        }
//        Log.i(LOG_TAG, "onButtonPressed - Exit");
//    }

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
