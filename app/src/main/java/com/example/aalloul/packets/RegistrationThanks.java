package com.example.aalloul.packets;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class RegistrationThanks extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";

    private String mname;
    private TextView thankyoumessage;

    public RegistrationThanks() {
        // Required empty public constructor
    }

    public static RegistrationThanks newInstance(String name) {
        RegistrationThanks fragment = new RegistrationThanks();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mname = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_thanks, container, false);

        thankyoumessage = (TextView) view.findViewById(R.id.thank_you_registering);
        String t = getString(R.string.thankyou_registration_pt1) + " ";
        t += mname+" ";
        t += getString(R.string.thankyou_registration_pt2);
        thankyoumessage.setText(t);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
