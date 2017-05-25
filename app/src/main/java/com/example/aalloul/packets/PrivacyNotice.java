package com.example.aalloul.packets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PrivacyNotice extends Fragment {
    private ContactUsListener mListener;
    private long fragment_start_time;


    public PrivacyNotice() {
        // Required empty public constructor
    }


    public static PrivacyNotice newInstance() {
        return new PrivacyNotice();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public long getFragmentStartTime(){
        return fragment_start_time;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_notice, container, false);

        TextView contact_us = (TextView) view.findViewById(R.id.contact_us_2);
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onContactUsPressed();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (ContactUsListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    interface ContactUsListener {
        void onContactUsPressed();
    }

}
