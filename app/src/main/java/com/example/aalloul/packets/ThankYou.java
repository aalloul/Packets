package com.example.aalloul.packets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ThankYou extends Fragment {
    private final static String LOG_TAG = ThankYou.class.getSimpleName();
    private static final String ARG_FIRST_NAME = "first_name";
    private String first_name;
    private ShareFragmentListener mListener;
    static long fragment_start_time;
    private final static boolean DEBUG = false;


    public ThankYou() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    static long getStartTime() {
        return fragment_start_time;
    }

    public static ThankYou newInstance(String first_name) {
        ThankYou fragment = new ThankYou();
        Bundle args = new Bundle();
        args.putString(ARG_FIRST_NAME, first_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            first_name = getArguments().getString(ARG_FIRST_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (DEBUG) Log.i(LOG_TAG, "onCreateView - enter");
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);
        TextView thank_you = (TextView) view.findViewById(R.id.thank_you_text);
        thank_you.setText(getString(R.string.thank_you) + " "+first_name +"?");

        TextView sharebutton = (TextView) view.findViewById(R.id.share_button);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShareButtonPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationFragment.RegistrationFragmentListener) {
            mListener = (ThankYou.ShareFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegistrationPageListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    interface ShareFragmentListener {
        void onShareButtonPressed();
    }
}
