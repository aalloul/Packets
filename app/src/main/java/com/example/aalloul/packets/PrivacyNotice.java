package com.example.aalloul.packets;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PrivacyNotice extends Fragment {

    private OnPrivacyFragmentListener mListener;
    private Button okay;

    public PrivacyNotice() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PrivacyNotice newInstance() {
        PrivacyNotice fragment = new PrivacyNotice();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_notice, container, false);

        okay = (Button) view.findViewById(R.id.okay_privacy);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOkayPrivacyPressed();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPrivacyFragmentListener) {
            mListener = (OnPrivacyFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPrivacyFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnPrivacyFragmentListener {
        // TODO: Update argument type and name
        void onOkayPrivacyPressed();
    }
}
