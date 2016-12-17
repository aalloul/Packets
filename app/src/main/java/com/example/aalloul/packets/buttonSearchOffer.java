package com.example.aalloul.packets;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link buttonSearchOffer.onSearchButtonInteractionListener} interface
 * to handle interaction events.
 * Use the {@link buttonSearchOffer#newInstance} factory method to
 * create an instance of this fragment.
 */
class buttonSearchOffer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private FloatingActionButton button_search_offer,button_publish_new_offer;
    private onSearchButtonInteractionListener mListener;

    public buttonSearchOffer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * @return A new instance of fragment buttonSearchOffer.
     */
    // TODO: Rename and change types and number of parameters
    public static buttonSearchOffer newInstance() {
        buttonSearchOffer fragment = new buttonSearchOffer();
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
        final View view = inflater.inflate(R.layout.fragment_button_search_offer, container, false);
        // Create the button logic
        button_search_offer = (FloatingActionButton) view.findViewById(R.id.search_button);
        button_search_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSearchButtonPressed();
            }
        });
        button_publish_new_offer = (FloatingActionButton) view.findViewById(R.id.publishNewOffer);
        button_publish_new_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mListener.onPostNewOfferPressed();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onSearchButtonInteractionListener) {
            mListener = (onSearchButtonInteractionListener) context;
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
    public interface onSearchButtonInteractionListener {
        // TODO: Update argument type and name
        void onSearchButtonPressed();
        void onPostNewOfferPressed();
    }
}
