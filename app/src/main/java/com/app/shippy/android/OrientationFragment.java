package com.app.shippy.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class OrientationFragment extends Fragment {

    private OnOrientationInteraction mListener;

    private long fragment_start_time;

    public OrientationFragment() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }


    public static OrientationFragment newInstance() {
        OrientationFragment fragment = new OrientationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_orientation_fragment, container, false);

        // Initiate buttons
        searchButton(view);
        publishButton(view);

        return view;
    }


    private void searchButton(View view) {
        Button createButton = (Button) view.findViewById(R.id.orientation_search_offers );
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOrientationSearchOffersPressed();
            }
        });
    }

    private void publishButton(View view) {
        Button createButton = (Button) view.findViewById(R.id.orientation_publish_offer);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOrientationPublishOfferPressed();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrientationInteraction) {
            mListener = (OnOrientationInteraction) context;
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

    public long getFragmentStartTime() {
        return fragment_start_time;
    }


    interface OnOrientationInteraction {
        void onOrientationSearchOffersPressed();
        void onOrientationPublishOfferPressed();
    }
}
