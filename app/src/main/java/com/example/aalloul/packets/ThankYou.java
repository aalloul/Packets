package com.example.aalloul.packets;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ThankYou extends Fragment {

    private OnThankYouListener mListener;
    private TextView thank_you;
    private Button back_to_mainFragment;
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
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);

        thank_you = (TextView) view.findViewById(R.id.thank_you_text);
        back_to_mainFragment = (Button) view.findViewById(R.id.back_to_mainFragment);
        back_to_mainFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBackToMainFragment();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnThankYouListener) {
            mListener = (OnThankYouListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnThankYouListener {
        // TODO: Update argument type and name
        void onBackToMainFragment();
    }
}
