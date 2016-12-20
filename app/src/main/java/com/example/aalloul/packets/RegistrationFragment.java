package com.example.aalloul.packets;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;


public class RegistrationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "long";

    private double lat, lng;
    private ImageButton picture;
    private EditText firstname, surname, phone_number;
    private TextView location;
    private Button registerMe, registerLater;
    private HashMap<String, String> user_detailed_location = new HashMap<>();

    private RegistrationFragmentListener mListener;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latlng Parameter 1.
     * @return A new instance of fragment RegistrationPage.
     */
    public static RegistrationFragment newInstance(LatLng latlng) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        if (latlng != null) {
            args.putDouble(ARG_LAT, latlng.latitude);
            args.putDouble(ARG_LNG, latlng.longitude);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_LAT);
            lng = getArguments().getDouble(ARG_LNG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_page, container, false);

        // User picture
        picture = (ImageButton) view.findViewById(R.id.user_picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed();
            }
        });
        
        // surname, first name and phone 
        firstname = (EditText) view.findViewById(R.id.user_first_name);
        surname = (EditText) view.findViewById(R.id.user_surname);
        phone_number = (EditText) view.findViewById(R.id.user_phone_number);
        
        // Location
        location = (TextView) view.findViewById(R.id.user_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserLocationPressed();
            }
        });
        
        // register buttons
        registerMe = (Button) view.findViewById(R.id.register_me);
        registerMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterMePressed();
            }
        });
        
        registerLater = (Button) view.findViewById(R.id.register_not_now);
        registerLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterLaterPressed();
            }
        });

        return view;
    }
    

    public void setUserPicture() {
        //TODO
        //picture.set
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationFragmentListener) {
            mListener = (RegistrationFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegistrationPageListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void set_user_detailed_location(HashMap<String, String> details) {
        user_detailed_location = details;
        String tmp = details.get("city") +", "+details.get("postalCode");
        location.setText(tmp);
    }

    public String get_user_location() {
        return location.getText().toString();
    }

    public HashMap<String, String> get_user_detailed_location() {
        return user_detailed_location;
    }

    public String get_user_phone_number() {
        return phone_number.getText().toString();
    }

    public String get_user_surname(){
        return surname.getText().toString();
    }

    public String get_user_firstname(){
        return firstname.getText().toString();
    }

    public String get_user_picture() {
        return "erw";
    }

    public boolean isInputOk() {
        if (firstname.getText() == null || get_user_firstname().equals("")) return false;
        if (surname.getText() == null|| get_user_surname().equals("")) return false;
        if (phone_number.getText() == null || get_user_phone_number().equals("")) return false;
        if (location.getText() == null || get_user_location().equals("")) return false;

        return true;
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
    public interface RegistrationFragmentListener {
        // TODO: Update argument type and name
        void onUserPicturePressed();

        void onUserLocationPressed();

        void onRegisterMePressed();

        void onRegisterLaterPressed();
    }
}
