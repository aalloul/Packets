package com.app.shippy.android;

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
    private EditText firstname, surname, phone_number;
    private TextView location, caption_user_picture ;
    private ImageButton picture;
    private View view;
    private RegistrationFragmentListener mListener;
    private Long fragment_start_time;
    private boolean edited_location = false;
    private final static boolean DEBUG = false;
    final static int FIRST_NAME_MISSING = 1;
    final static int SUR_NAME_MISSING = 2;
    final static int LOCATION_MISSING = 3;
    final static int PHONE_NUMBER_MISSING = 4;
    final static int ALL_GOOD = 0;

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
            Log.d(LOG_TAG, "onCreate - savedInstanceState not null");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (DEBUG) Log.i(LOG_TAG, "onSaveInstanceState - Enter");
        super.onSaveInstanceState(bundle);
    }

    long getFragmentStartTime() {
        return fragment_start_time;
    }

    void setFirstname() {
        firstname = (EditText) view.findViewById(R.id.user_first_name);
        String fname = mListener.getUserForRegistrationFragment().getFirstname();
        if (!fname.equals("")) {firstname.setText(fname);}
    }

    void setSurname() {
        surname = (EditText) view.findViewById(R.id.user_surname);
        String sname = mListener.getUserForRegistrationFragment().getSurname();
        if (!sname.equals("")) {surname.setText(sname);}
    }

    void setPhone_number() {
        phone_number = (EditText) view.findViewById(R.id.user_phone_number);
        String pnumber = mListener.getUserForRegistrationFragment().getPhoneNumber();
        if (!pnumber.equals("")) {phone_number.setText(pnumber);}
    }

    void setUserPicture() {
        if (DEBUG) Log.i(LOG_TAG, "setPicture - enter");
        picture = (ImageButton) view.findViewById(R.id.user_picture);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed();
            }
        });

        if (!mListener.getUserForRegistrationFragment().getPicture().equals("")) {
            picture.setImageBitmap(mListener.getUserForRegistrationFragment().getPictureBM());
        }

        caption_user_picture = (TextView) view.findViewById(R.id.caption_user_picture);
        if (mListener.getUserForRegistrationFragment().getPicture().equals("")) {
            caption_user_picture.setText(getString(R.string.add_profile_picture));
        } else {
            caption_user_picture.setText(null);
        }

//        picture_ui.setScaleX(1);
//        picture_ui.setScaleY(1);
    }

    void updateUserPicture() {
        picture.setImageBitmap(mListener.getUserForRegistrationFragment().getPictureBM());
        caption_user_picture.setText(null);
    }

    void setLocation() {

        if (DEBUG) Log.i(LOG_TAG, "set_user_detailed_location - Enter");

        location = (TextView) view.findViewById(R.id.user_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserLocationPressed();
            }
        });

        String tmp = mListener.getUserForRegistrationFragment().getLocationObject().getCity();
        if (tmp == null || tmp.equals("")) {return;}

        String tmp2 = mListener.getUserForRegistrationFragment().getLocationObject().getState();

        if (tmp2 == null) {
            tmp += " (" + Utilities.CountryToCountryCode(
                    mListener.getUserForRegistrationFragment().getLocationObject().getCountry())+")";
            location.setText(tmp);
            return;
        }

        tmp += " (" + tmp2 + ")";
        location.setText(tmp);
    }

    void updateLocation() {
        String tmp = mListener.getUserForRegistrationFragment().getLocationObject().getCity();
        if (tmp == null || tmp.equals("")) {return;}

        String tmp2 = mListener.getUserForRegistrationFragment().getLocationObject().getState();

        if (tmp2 == null) {
            tmp += " (" + Utilities.CountryToCountryCode(
                    mListener.getUserForRegistrationFragment().getLocationObject().getCountry())+")";
            location.setText(tmp);
            return;
        }

        tmp += " (" + tmp2 + ")";
        location.setText(tmp);
    }

    void setRegisterMeButton() {
        Button registerMe = (Button) view.findViewById(R.id.register_me);
        registerMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterMePressed();
            }
        });
    }

    void setRegisterLaterButton() {
        Button registerLater = (Button) view.findViewById(R.id.register_not_now);
        registerLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterLaterPressed();
            }
        });
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
        } else {
            if (DEBUG) Log.i(LOG_TAG,"onCreateView - savedInstanceState is NOT null");
        }

        // surname, first name and phone
        setFirstname();
        setSurname();
        setPhone_number();
        setUserPicture();
        setLocation();

        // register buttons
        setRegisterMeButton();
        setRegisterLaterButton();


        if (DEBUG) Log.i(LOG_TAG, "onCreateView - Exit");
        return view;
    }

    boolean hasEditedLocation() {
        return edited_location;
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

    public int checkInput() {
        if (firstname == null || firstname.getText() == null ||
                firstname.getText().toString().equals("")) {
            return FIRST_NAME_MISSING;
        }

        if (surname == null || surname.getText() == null || surname.getText().toString().equals("")) {
            return SUR_NAME_MISSING;
        }

        if (phone_number == null || phone_number.getText() == null ||
                phone_number.getText().toString().equals("")) {
            return PHONE_NUMBER_MISSING;
        }

        if (location == null || location.getText() == null ||
                location.getText().toString().equals("")) {
            return LOCATION_MISSING;
        }

        mListener.getUserForRegistrationFragment().setFirstname(firstname.getText().toString());
        mListener.getUserForRegistrationFragment().setSurname(surname.getText().toString());
        mListener.getUserForRegistrationFragment().setPhoneNumber(phone_number.getText().toString());

        return ALL_GOOD;
    }

    interface RegistrationFragmentListener {
        void onUserPicturePressed();
        void onUserLocationPressed();
        void onRegisterMePressed();
        void onRegisterLaterPressed();
        User getUserForRegistrationFragment();
    }
}
