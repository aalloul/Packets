package com.example.aalloul.packets;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.HashMap;


public class RegistrationFragment extends Fragment {
    private final static String LOG_TAG = RegistrationFragment.class.getSimpleName();
    private ImageButton picture_ui;
    private String picture_bitmap;
    private EditText firstname_ui, surname_ui, phone_number_ui;
    private TextView location_ui, caption_user_picture;
    private String firstname, surname, phone_number;
    private View view;
    private HashMap<String, String> user_detailed_location = new HashMap<>();
    private RegistrationFragmentListener mListener;
    private Long fragment_start_time;
    private boolean edited_location = false;
    private final static boolean DEBUG = false;

    public RegistrationFragment() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(LOG_TAG, "onCreate - called");

        if (savedInstanceState != null) {
            user_detailed_location = (HashMap<String, String>)
                    savedInstanceState.getSerializable("user_location");
            picture_bitmap = savedInstanceState.getString("user_picture");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (DEBUG) Log.i(LOG_TAG, "onSaveInstanceState - Enter");
        HashMap<String, String> tmp = get_user_detailed_location();
        if (DEBUG) Log.i(LOG_TAG, "onSaveInstanceState - user_location = " + tmp);
        bundle.putSerializable("user_location", tmp);
        if (picture_bitmap == null ) return;
        if (DEBUG) Log.i(LOG_TAG, "onSaveInstanceState - picture_ui  not null");
        bundle.putString("user_picture", picture_bitmap);
        super.onSaveInstanceState(bundle);
    }

    private void getUserPicture() {
        if (DEBUG) Log.i(LOG_TAG, "getUserPicture - Enter");
        picture_ui = (ImageButton) view.findViewById(R.id.user_picture);
        if (picture_bitmap != null) {
            if (DEBUG) Log.i(LOG_TAG, "picture_bitmap not null");
            picture_ui.setImageBitmap(
                    Utilities.StringToBitMap(picture_bitmap));
        }
        if (DEBUG) Log.i(LOG_TAG, "picture_bitmap IS null");
        picture_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed();
            }
        });
        // Text to encourage selecting a profile picture
        caption_user_picture = (TextView) view.findViewById(R.id.caption_user_picture);
    }

    private void restoreUserPicture(String bnp) {
        if (DEBUG) Log.i(LOG_TAG, "restoreUserPicture - Enter");
        picture_ui = (ImageButton) view.findViewById(R.id.user_picture);

        if (bnp != null)  {
            if (DEBUG) Log.i(LOG_TAG, "restoreUserPicture - bnp  not null");
            picture_ui.setImageBitmap(Utilities.StringToBitMap(bnp));
            picture_bitmap = bnp;
            caption_user_picture = (TextView) view.findViewById(R.id.caption_user_picture);
            caption_user_picture.setText("");
        } else {
            if (DEBUG) Log.i(LOG_TAG, "restoreUserPicture - bnp is null");
            caption_user_picture = (TextView) view.findViewById(R.id.caption_user_picture);
            caption_user_picture.setText(getString(R.string.add_profile_picture));
        }

        picture_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed();
            }
        });

    }

    private void getUserLocation() {
        location_ui = (TextView) view.findViewById(R.id.user_location);
        location_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserLocationPressed();
            }
        });
        set_user_detailed_location();
    }
    private void restoreUserLocation(HashMap<String, String> val) {
        location_ui = (TextView) view.findViewById(R.id.user_location);
        location_ui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserLocationPressed();
            }
        });
        if (DEBUG) Log.i(LOG_TAG, "restoreUserLocation - val = "+val);
        set_user_detailed_location(val);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Enter");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registration_page, container, false);

        getActivity().setTitle(getString(R.string.join_the_community));

        if (savedInstanceState == null){
            if (DEBUG) Log.i(LOG_TAG,"onCreateView - savedInstanceState is null");
            // User picture
            getUserPicture();
            // Location
            getUserLocation();
        } else {
            if (DEBUG) Log.i(LOG_TAG,"onCreateView - savedInstanceState is NOT null");
            restoreUserPicture(savedInstanceState.getString("user_picture"));
            restoreUserLocation((HashMap<String, String>) savedInstanceState.getSerializable("user_location"));
        }

        // surname, first name and phone
        firstname_ui = (EditText) view.findViewById(R.id.user_first_name);
        if (firstname != null) firstname_ui.setText(firstname);
        surname_ui = (EditText) view.findViewById(R.id.user_surname);
        if (surname != null) surname_ui.setText(surname);
        phone_number_ui = (EditText) view.findViewById(R.id.user_phone_number);
        if (phone_number != null) phone_number_ui.setText(phone_number);

        // register buttons
        Button registerMe = (Button) view.findViewById(R.id.register_me);
        registerMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterMePressed();
            }
        });
        
        Button registerLater = (Button) view.findViewById(R.id.register_not_now);
        registerLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterLaterPressed();
            }
        });


        TextView privacy_button = (TextView) view.findViewById(R.id.privacyButton);
        privacy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnPrivacyButtonPressed();
            }
        });

        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Exit");
        return view;
    }
    

    public void setUserPicture(Bitmap imageBitmap) {
        if (DEBUG) Log.i(LOG_TAG, "setUserPicture - enter");
        picture_bitmap = Utilities.BitMapToString(imageBitmap);
//        if (DEBUG) Log.i(LOG_TAG, "setUserPicture picture_ui_bitmap = " + picture_bitmap);
        picture_ui.setImageBitmap(imageBitmap);
        picture_ui.setScaleX(1);
        picture_ui.setScaleY(1);
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

    public void setEdited_location() {
        edited_location = true;
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

        if (DEBUG) Log.i(LOG_TAG, "set_user_detailed_location - Enter");

        user_detailed_location = details;
        if (details == null) {
            location_ui.setText(getString(R.string.updating_location));
            return;
        }

        if (DEBUG) Log.i(LOG_TAG, "set_user_detailed_location - details = " + details.toString());
        String tmp = details.get(getString(R.string.saved_user_city));
        if (DEBUG) Log.i(LOG_TAG, "set_user_detailed_location - city = " + tmp);

        if (tmp == null) {
            location_ui.setText(getString(R.string.updating_location));
            return;
        }

        if (tmp.equals("")) {
            location_ui.setText(getString(R.string.updating_location));
            return;
        }

        String tmp2 = details.get(getString(R.string.saved_user_state));

        if (DEBUG) Log.i(LOG_TAG, "set_user_detailed_location - State = " +tmp2);

        if (tmp2 == null) {
            tmp += " (" + Utilities.CountryToCountryCode(
                    details.get(getString(R.string.saved_user_country))) + ")";
        } else {
            tmp += " (" + tmp2 + ")";
        }

//        setLocation(tmp);
        location_ui.setText(tmp);
    }

    public String get_user_location() {
//        setLocation(location_ui.getText().toString());
        return location_ui.getText().toString();
    }

    public HashMap<String, String> get_user_detailed_location() {
        if (DEBUG) Log.i(LOG_TAG, "get_user_detailed_location - Enter");
        if (DEBUG) Log.i(LOG_TAG, "get_user_detailed_location - user_detailed_location = " + user_detailed_location);
        return user_detailed_location;
    }

    public String get_user_phone_number() {
        setPhone_number(phone_number_ui.getText().toString());
        if (DEBUG) Log.i(LOG_TAG, "get_user_phone_number = "+phone_number_ui.getText().toString());
        return phone_number_ui.getText().toString();
    }

    public String get_user_surname(){
        setSurname(surname_ui.getText().toString());
        if (DEBUG) Log.i(LOG_TAG, "get_user_surname = "+surname_ui.getText().toString());
        return surname_ui.getText().toString();
    }

    public String get_user_firstname(){
        setFirstname(firstname_ui.getText().toString());
        if (DEBUG) Log.i(LOG_TAG, "get_user_firstname = "+firstname_ui.getText().toString());
        return firstname_ui.getText().toString();
    }

    public String get_user_picture() {
        if (picture_bitmap == null) return "";
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

    public HashMap<String, String> getBlob(String nextFrag) {
        HashMap<String, String> t = new HashMap<>();
        long end_time = Utilities.CurrentTimeMS();
        t.put(getString(R.string.fStart), Long.toString(fragment_start_time));
        t.put(getString(R.string.fEnd), Long.toString(end_time));
        t.put(getString(R.string.fDuration), Long.toString(end_time - fragment_start_time));

        if (!nextFrag.equals("")) {
            //Used for data reporting when switching to another fragment
            t.put(getString(R.string.nextF), nextFrag);
        }

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

        t.put(getString(R.string.user_location_edited), Boolean.toString(edited_location));

        t.putAll(get_user_detailed_location());
        return t;
    }

    public HashMap<String, String> getBlob(String err_field, String err_msg) {
        HashMap<String, String> t = new HashMap<>();
        long end_time = Utilities.CurrentTimeMS();
        t.put(getString(R.string.fStart), Long.toString(fragment_start_time));
        t.put(getString(R.string.fEnd), Long.toString(end_time));
        t.put(getString(R.string.fDuration), Long.toString(end_time - fragment_start_time));
        t.put(err_field,err_msg);

        return t;
    }

    interface RegistrationFragmentListener {
        // TODO: Update argument type and name
        void onUserPicturePressed();
        void onUserLocationPressed();
        void onRegisterMePressed();
        void onRegisterLaterPressed();
        void OnPrivacyButtonPressed();
    }
}
