package com.example.aalloul.packets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NoResultsFound extends Fragment {

    private static final String LOG_TAG = "NoResultsFound";
    private OnNoResultsFoundInteraction mListener;

    public NoResultsFound() {
        // Required empty public constructor
    }

    public static NoResultsFound newInstance() {
        return new NoResultsFound();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView - Enter");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_results_found, container, false);
        TextView textView = (TextView) view.findViewById(R.id.share_button_noresults);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNoResultsShareButtonPressed();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoResultsFoundInteraction) {
            mListener = (OnNoResultsFoundInteraction) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnNoResultsFoundInteraction{
        // TODO: Update argument type and name
        void onNoResultsShareButtonPressed();
    }
}
