package com.app.shippy.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TransportMethodChooser extends Fragment {

    private onTransportMethodChooserInteraction mListener;
    private long fragmentStartTime;
    private final static String LOG_TAG = "TransportMethodChooser";
    private View view;

    public TransportMethodChooser() {
        // Required empty public constructor
        fragmentStartTime = Utilities.CurrentTimeMS();
    }


    public static TransportMethodChooser newInstance(String param1, String param2) {
        return new TransportMethodChooser();
    }

    long getFragmentStartTime() {
        return fragmentStartTime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transport_method_chooser, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onTransportMethodChooserInteraction) {
            mListener = (onTransportMethodChooserInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onTransportMethodChooserInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    interface onTransportMethodChooserInteraction {
        void onTransportMethodChooserNextButtonPressed();
    }
}
