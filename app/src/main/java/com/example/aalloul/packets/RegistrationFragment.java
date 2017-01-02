package com.example.aalloul.packets;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private final static String LOG_TAG = RegistrationFragment.class.getSimpleName();
    private ImageButton picture_ui;
    private String picture_bitmap;
    private EditText firstname_ui, surname_ui, phone_number_ui;
    private TextView location_ui, caption_user_picture, privacy_button;
    private String firstname, surname, phone_number, location;
    private Button registerMe, registerLater;
    private HashMap<String, String> user_detailed_location = new HashMap<>();

    private RegistrationFragmentListener mListener;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate - called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView - Enter");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_page, container, false);

        // User picture
        picture_ui = (ImageButton) view.findViewById(R.id.user_picture);
        if (picture_bitmap != null) picture_ui.setImageBitmap(
                Utilities.StringToBitMap(picture_bitmap));

        picture_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed();
            }
        });
        
        // surname, first name and phone 
        firstname_ui = (EditText) view.findViewById(R.id.user_first_name);
        if (firstname != null) firstname_ui.setText(firstname);
        surname_ui = (EditText) view.findViewById(R.id.user_surname);
        if (surname != null) surname_ui.setText(surname);
        phone_number_ui = (EditText) view.findViewById(R.id.user_phone_number);
        if (phone_number != null) phone_number_ui.setText(phone_number);

        // Location
        location_ui = (TextView) view.findViewById(R.id.user_location);
        location_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserLocationPressed();
            }
        });
        set_user_detailed_location();
        
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

        // Text to encourage selecting a profile picture
        caption_user_picture = (TextView) view.findViewById(R.id.caption_user_picture);

        privacy_button = (TextView) view.findViewById(R.id.privacyButton);
        privacy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnPrivacyButtonPressed();
            }
        });

        Log.i(LOG_TAG, "onCreateView - Exit");
        return view;
    }
    

    public void setUserPicture(Bitmap imageBitmap) {
        Log.i(LOG_TAG, "setUserPicture - enter");
        picture_bitmap = Utilities.BitMapToString(imageBitmap);
//        Log.i(LOG_TAG, "setUserPicture picture_ui_bitmap = " + picture_bitmap);
        picture_ui.setImageBitmap(imageBitmap);
    }


    public void unsetCaption_user_picture() {
        caption_user_picture.setText("");
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public void set_user_detailed_location() {
        if (user_detailed_location == null) return;
        if (!user_detailed_location.containsKey(getString(R.string.saved_user_city))) return;

        set_user_detailed_location(user_detailed_location);
    }
    public void set_user_detailed_location(HashMap<String, String> details) {

        Log.i(LOG_TAG, "set_user_detailed_location - Enter");

        user_detailed_location = details;
        if (details == null) {
            location_ui.setText(getString(R.string.updating_location));
            return;
        }

        Log.i(LOG_TAG, "set_user_detailed_location - details = " + details.toString());
        String tmp = details.get(getString(R.string.saved_user_city));
        Log.i(LOG_TAG, "set_user_detailed_location - city = " + tmp);

        if (tmp == null) {
            location_ui.setText(getString(R.string.updating_location));
            return;
        }

        if (tmp.equals("")) {
            location_ui.setText(getString(R.string.updating_location));
            return;
        }

        String tmp2 = details.get(getString(R.string.saved_user_state));

        Log.i(LOG_TAG, "set_user_detailed_location - State = " +tmp2);

        if (tmp2 == null) {
            tmp += " (" + Utilities.CountryToCountryCode(
                    details.get(getString(R.string.saved_user_country))) + ")";
        } else {
            tmp += " (" + tmp2 + ")";
        }

        setLocation(tmp);
        location_ui.setText(tmp);
    }

    public String get_user_location() {
        setLocation(location_ui.getText().toString());
        return location_ui.getText().toString();
    }

    public HashMap<String, String> get_user_detailed_location() {
        return user_detailed_location;
    }

    public String get_user_phone_number() {
        setPhone_number(phone_number_ui.getText().toString());
        Log.i(LOG_TAG, "get_user_phone_number = "+phone_number_ui.getText().toString());
        return phone_number_ui.getText().toString();
    }

    public String get_user_surname(){
        setSurname(surname_ui.getText().toString());
        Log.i(LOG_TAG, "get_user_surname = "+surname_ui.getText().toString());
        return surname_ui.getText().toString();
    }

    public String get_user_firstname(){
        setFirstname(firstname_ui.getText().toString());
        Log.i(LOG_TAG, "get_user_firstname = "+firstname_ui.getText().toString());
        return firstname_ui.getText().toString();
    }

    public String get_user_picture() {
        return picture_bitmap;
    }

    public int isInputOk() {
        if (firstname_ui == null ) return 0;
        if (firstname_ui.getText() == null || get_user_firstname().equals("")) return 0;
        if (surname_ui == null) return 0;
        if (surname_ui.getText() == null|| get_user_surname().equals("")) return 0;
        if (phone_number_ui == null) return 0;
        if (phone_number_ui.getText() == null || get_user_phone_number().equals("")) return 0;
        if (location_ui == null) return 0;
        if (location_ui.getText() == null || get_user_location().equals("")) return 1;

        return 2;
    }

    public HashMap<String, String> getBlob() {
        HashMap<String, String> t = new HashMap<>();
        t.put(getString(R.string.saved_user_firstname),
                get_user_firstname());
        t.put(getString(R.string.saved_user_surname),
                get_user_surname());
        t.put(getString(R.string.saved_user_picture),
                get_user_picture());
        t.put(getString(R.string.saved_user_phonenumber),
                get_user_phone_number());
        t.put(getString(R.string.saved_user_phonenumber),
                get_user_phone_number());
        t.putAll(get_user_detailed_location());
        return t;
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

        void OnPrivacyButtonPressed();
    }
}
