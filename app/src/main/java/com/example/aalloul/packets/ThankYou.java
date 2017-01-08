package com.example.aalloul.packets;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ThankYou extends Fragment {
    private final static String LOG_TAG = ThankYou.class.getSimpleName();
    private TextView thank_you;
    private static final String ARG_FIRST_NAME = "first_name";
    private String first_name;

    public ThankYou() {
        // Required empty public constructor
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
        Log.i(LOG_TAG, "onCreateView - enter");
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);

        thank_you = (TextView) view.findViewById(R.id.thank_you_text);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/banana.ttf");
        thank_you.setTypeface(tf);
        thank_you.setText(getString(R.string.thank_you_firstname) + " "+first_name);

        TextView share = (TextView) view.findViewById(R.id.spread_the_word);
        share.setTypeface(tf);

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
