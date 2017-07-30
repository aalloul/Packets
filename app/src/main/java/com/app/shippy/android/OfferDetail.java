package com.app.shippy.android;
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
import java.util.HashMap;


public class OfferDetail extends Fragment {
    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = OfferDetail.class.getSimpleName();
    private long fragment_start_time;
    private View view;
    private OnFragmentInteractionListener mListener;
    private final static boolean DEBUG = false;

    public OfferDetail() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public static OfferDetail newInstance() {
        if (DEBUG) Log.i(LOG_TAG, "newInstance - Start");
        OfferDetail fragment = new OfferDetail();

        if (DEBUG) Log.i(LOG_TAG, "newInstance - Exit");
        return fragment;
    }

    long getFragmentStartTime() {return fragment_start_time;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(LOG_TAG, "onCreate - Start");
    }

    private void setFirstName() {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        nameTextView.setText(mListener.getTripOfferForOfferDetail().getFirstName());
    }

    private void setPickupCity() {
        TextView pickupcity = (TextView) view.findViewById(R.id.going_from);
        pickupcity.setText(mListener.getTripOfferForOfferDetail().getPickupCity());
    }

    private void setDropoffCity() {
        TextView dropoffcity = (TextView) view.findViewById(R.id.going_to);
        dropoffcity.setText(mListener.getTripOfferForOfferDetail().getDropoffCity());
    }

    private void setPickupDate() {
        TextView pickup_date_ui = (TextView) view.findViewById(R.id.pickup_date_offer);
        pickup_date_ui.setText(Utilities.
                Epoch2Date(mListener.getTripOfferForOfferDetail().getPickupDate(), "dd MMM"));
    }

    private void setPicture() {
        ImageView picture_ui = (ImageView) view.findViewById(R.id.transporter_picture_detail);


        if (!mListener.getTripOfferForOfferDetail().getPicture_str().equals("")) {
            picture_ui.setImageBitmap(mListener.getTripOfferForOfferDetail().getPictureBM());
            picture_ui.setScaleX(1);
            picture_ui.setScaleY(1);
            picture_ui.setBackground(null);
            picture_ui.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }

    private void setComment() {
        TextView descriptionTextView = (TextView) view.findViewById(R.id.comment_transporter);
        if (mListener.getTripOfferForOfferDetail().getComment().equals("") ) {
            //TODO improve this bit to show something meaningful when no comment is available
            descriptionTextView.setText("");
        } else {
            descriptionTextView.setText(mListener.getTripOfferForOfferDetail().getComment());
        }
    }

    private void setPackageSize() {
        switch (mListener.getTripOfferForOfferDetail().getPackageSize()) {
            case 0:
                ((TextView) view.findViewById(R.id.size_package_detail)).setText("S");
                break;
            case 1:
                ((TextView) view.findViewById(R.id.size_package_detail)).setText("M");
                break;
            case 2:
                ((TextView) view.findViewById(R.id.size_package_detail)).setText("L");
                break;
        }
    }

    private void setNumberPackages() {
        String tmp = "x"+mListener.getTripOfferForOfferDetail().getNumberPackages();
        ((TextView) view.findViewById(R.id.number_packages_detail)).setText(tmp);

    }

    private void setTravelBy() {
        switch (mListener.getTripOfferForOfferDetail().getTravelBy()) {
            case 0:
                ((ImageView) view.findViewById(R.id.travel_by)).setImageResource(R.drawable.car);
                break;
            case 1:
                ((ImageView) view.findViewById(R.id.travel_by)).setImageResource(R.drawable.train);
                break;
            case 2:
                ((ImageView) view.findViewById(R.id.travel_by)).setImageResource(R.drawable.plane);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Start");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_offer_detail, container, false);

        if (mListener.getTripOfferForOfferDetail() == null) {return view;}

        setFirstName();
        setPickupCity();
        setDropoffCity();
        setPickupDate();
        setPicture();
        setComment();
        setPackageSize();
        setNumberPackages();
        setTravelBy();

        // Call action
        ImageButton callButton = (ImageButton) view.findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallButtonPress(mListener.getTripOfferForOfferDetail().getPhoneNumber());
            }
        });

        // Message action
        ImageButton sendMessage = (ImageButton) view.findViewById(R.id.messageButton);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMessageButtonPress(mListener.getTripOfferForOfferDetail().getPhoneNumber());
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
        TripOffer getTripOfferForOfferDetail();
    }
}
