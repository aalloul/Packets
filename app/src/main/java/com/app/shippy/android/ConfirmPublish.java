package com.app.shippy.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.HashMap;


public class ConfirmPublish extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final static String LOG_TAG = ConfirmPublish.class.getSimpleName();
    private final static boolean DEBUG = false;
    private TextView caption_confirm_user_picture;
    private ImageButton user_picture;
    private OnConfirmPublishListener mListener;
    private Spinner travelling_by;
    private FragmentActivity myContext;
    private View view;
    private Long fragment_start_time;
    private boolean changed_profile_picture=false;
    private EditText firstname, surname, phone, comment;

    public ConfirmPublish() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }


    public static ConfirmPublish newInstance() {
        return new ConfirmPublish();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }
    }


    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private void setFirstname() {
        firstname = (EditText) view.findViewById(R.id.confirm_first_name);
        firstname.setText(mListener.getUserForConfirmPublish().getFirstname());
    }

    private void setSurname() {
        surname = (EditText) view.findViewById(R.id.confirm_surname);
        surname.setText(mListener.getUserForConfirmPublish().getSurname());
    }

    private void setPhone() {
        phone = (EditText) view.findViewById(R.id.confirm_phone_number);
        phone.setText(mListener.getUserForConfirmPublish().getPhoneNumber());
    }

    private void setComment() {
        comment = (EditText) view.findViewById(R.id.comment_transporter);
        comment.setText(mListener.getTripDetailsForConfirmPublish().getComment());
    }

    private void setActivityTitle() {
        getActivity().setTitle(getString(R.string.please_register));
    }

    private void setTravellingBy() {
        travelling_by = (Spinner) view.findViewById(R.id.travelling_by);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTravellingBy= ArrayAdapter.createFromResource(myContext,
                R.array.travelling_by, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTravellingBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        travelling_by.setAdapter(adapterTravellingBy);
    }

    void setUserPicture(){
        if (DEBUG) Log.i(LOG_TAG, "setPicture - enter");
        caption_confirm_user_picture = (TextView) view.findViewById(
                R.id.caption_confirm_user_picture);

        user_picture = (ImageButton) view.findViewById(R.id.confirm_user_picture);
        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed_Confirm();
            }
        });

        if (!mListener.getUserForConfirmPublish().getPicture().equals("")) {
            user_picture.setImageBitmap(mListener.getUserForConfirmPublish().getPictureBM());
            user_picture.setScaleX(1);
            user_picture.setScaleY(1);
            caption_confirm_user_picture.setText(null);
        }
    }

    void updateUserPicture() {
        if (!mListener.getUserForConfirmPublish().getPicture().equals("")) {
            user_picture.setImageBitmap(mListener.getUserForConfirmPublish().getPictureBM());
            user_picture.setScaleX(1);
            user_picture.setScaleY(1);
            caption_confirm_user_picture.setText(null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.i(LOG_TAG, "onCreateView - start");
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_confirm_publish, container, false);

        Button confirmAction = (Button) view.findViewById(R.id.confirm_publish);
        confirmAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirmPublish();
            }
        });

        setFirstname();
        setSurname();
        setPhone();
        setComment();
        setActivityTitle();
        setUserPicture();

        if (savedInstanceState != null) {
        } else {
//            getPicture();
        }



        setTravellingBy();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        myContext= (FragmentActivity) context;
        super.onAttach(context);
        if (context instanceof OnConfirmPublishListener) {
            mListener = (OnConfirmPublishListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCofirmPublishListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    boolean hasEditedPicture() {return changed_profile_picture;}

    public boolean checkInputs() {

        if (DEBUG) Log.i(LOG_TAG, "checkInputs - Enter");

        if (firstname == null || firstname.getText() == null ||
                firstname.getText().toString().equals("")) {
            return false;
        }
        if (surname == null || surname.getText() == null || surname.getText().toString().equals("")) {
            return false;
        }
        if (phone == null || phone.getText() == null || phone.getText().toString().equals("")) {
            return false;
        }
        if (comment == null || comment.getText() == null || comment.getText().toString().equals("")) {
            return false;
        }

        mListener.getUserForConfirmPublish().setFirstname(firstname.getText().toString());
        mListener.getUserForConfirmPublish().setSurname(surname.getText().toString());
        mListener.getUserForConfirmPublish().setPhoneNumber(phone.getText().toString());
        mListener.getTripDetailsForConfirmPublish().setComment(comment.getText().toString());
        mListener.getTripDetailsForConfirmPublish().setTravelBy(travelling_by.getSelectedItemPosition());
        return true;
    }

    public long getFragmentStartTime() {return fragment_start_time;}

    interface OnConfirmPublishListener {
        void onConfirmPublish();
        void onUserPicturePressed_Confirm();
        User getUserForConfirmPublish();
        TripRequestDetails getTripDetailsForConfirmPublish();
    }
}
