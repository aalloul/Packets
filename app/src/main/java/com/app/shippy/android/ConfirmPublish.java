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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfirmPublish.OnCofirmPublishListener} interface
 * to handle interaction events.
 * Use the {@link ConfirmPublish#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPublish extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PERSONAL_DETAILS = "pers_details";
    private static final String ARG_TRIP_DETAILS= "trip_details";
    private final static String LOG_TAG = ConfirmPublish.class.getSimpleName();
    private HashMap<String, String> mpers_details, mtrip_details;
    private final static boolean DEBUG = false;

    private EditText firstname_edit, surname_edit, phone_edit, comment_user ;
    private TextView caption_confirm_user_picture;
    private ImageButton user_picture;
    private OnCofirmPublishListener mListener;
    private Spinner travelling_by;
    private FragmentActivity myContext;
    private String user_picture_string = "";
    private String user_picture_path;
    private boolean changed_profile_picture=false;
    private View view;
    private Long fragment_start_time;

    public ConfirmPublish() {
        // Required empty public constructor
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pers_details Parameter 1.
     * @return A new instance of fragment ConfirmPublish.
     */
    public static ConfirmPublish newInstance(HashMap<String, String> pers_details,
                                             HashMap<String, String> trip_details) {
        ConfirmPublish fragment = new ConfirmPublish();
//        if (DEBUG) Log.i(LOG_TAG, "newInstance - pers_details = " + pers_details.toString());
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSONAL_DETAILS, pers_details);
        args.putSerializable(ARG_TRIP_DETAILS, trip_details);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mpers_details = (HashMap<String, String>)
                    getArguments().getSerializable(ARG_PERSONAL_DETAILS);
            mtrip_details = (HashMap<String, String>)
                    getArguments().getSerializable(ARG_TRIP_DETAILS);
        }

        if (savedInstanceState != null) {
            user_picture_string = savedInstanceState.getString("user_picture");
            user_picture_path   = savedInstanceState.getString("user_picture_path");
        }
    }

    private void restoreUserPicture(String val, String val2) {
        user_picture_string = val;
        user_picture_path = val2;
        user_picture = (ImageButton) view.findViewById(R.id.confirm_user_picture);
        if (!user_picture_string.equals("")) {
            caption_confirm_user_picture.setText(null);
            user_picture.setImageBitmap(Utilities.StringToBitMap(
                    mpers_details.get(getString(R.string.saved_user_picture))));
            user_picture.setScaleY(1);
            user_picture.setScaleX(1);

        }
        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed_Confirm();
            }
        });
    }

    private void getUserPicture(){
        user_picture = (ImageButton) view.findViewById(R.id.confirm_user_picture);

        if (!user_picture_string.equals("")) {
            caption_confirm_user_picture.setText(null);
            user_picture.setImageBitmap(Utilities.StringToBitMap(
                    mpers_details.get(getString(R.string.saved_user_picture))));
            user_picture.setScaleY(1);
            user_picture.setScaleX(1);

        }
        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserPicturePressed_Confirm();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("user_picture", user_picture_string);
        bundle.putString("user_picture_path", user_picture_path);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.i(LOG_TAG, "onCreateView - start");
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_confirm_publish, container, false);
        caption_confirm_user_picture = (TextView) view.findViewById(
                R.id.caption_confirm_user_picture);

        Button confirmAction = (Button) view.findViewById(R.id.confirm_publish);
        confirmAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirmPublish();
            }
        });

        firstname_edit = (EditText) view.findViewById(R.id.confirm_first_name);
        surname_edit = (EditText) view.findViewById(R.id.confirm_surname);
        phone_edit = (EditText) view.findViewById(R.id.confirm_phone_number);
        comment_user = (EditText) view.findViewById(R.id.comment_transporter);

        if (!mpers_details.get(getString(R.string.saved_user_firstname)).equals("")) {
//            if(DEBUG) Log.i(LOG_TAG, "onCreateView - mpers_details = "+mpers_details.toString());
            getActivity().setTitle(getString(R.string.please_confirm));
            firstname_edit.setText(mpers_details.get(getString(R.string.saved_user_firstname)));
            surname_edit.setText(mpers_details.get(getString(R.string.saved_user_surname)));
            phone_edit.setText(mpers_details.get(getString(R.string.saved_user_phonenumber)));
            user_picture_string = mpers_details.get(getString(R.string.saved_user_picture));
            user_picture_path = mpers_details.get(getString(R.string.saved_user_picture_path));

            Log.i(LOG_TAG, "onCreateView - user_picture_path = "+user_picture_path);
//            if (DEBUG) Log.i(LOG_TAG, "onCreateView - user_picture_path = "+user_picture_string);
        } else {
            getActivity().setTitle(getString(R.string.please_register));
        }

        if (savedInstanceState != null) {
            restoreUserPicture(savedInstanceState.getString("user_picture"),
                    savedInstanceState.getString("user_picture_path"));
        } else {
            getUserPicture();
        }



        travelling_by = (Spinner) view.findViewById(R.id.travelling_by);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTravellingBy= ArrayAdapter.createFromResource(myContext,
                R.array.travelling_by, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTravellingBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        travelling_by.setAdapter(adapterTravellingBy);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        myContext= (FragmentActivity) context;
        super.onAttach(context);
        if (context instanceof OnCofirmPublishListener) {
            mListener = (OnCofirmPublishListener) context;
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

    public HashMap<String, String> getAllDetails() {
        HashMap<String, String> out = new HashMap<>();
        out.put(getString(R.string.saved_user_surname), surname_edit.getText().toString());
        out.put(getString(R.string.saved_user_firstname), firstname_edit.getText().toString());
        out.put(getString(R.string.saved_user_phonenumber), phone_edit.getText().toString());
        out.put(getString(R.string.travels_by), travelling_by.getSelectedItem().toString());
        out.put(getString(R.string.user_comment), comment_user.getText().toString());
        out.put(getString(R.string.saved_user_picture), user_picture_string);
        out.putAll(mtrip_details);
//        if (DEBUG) Log.i(LOG_TAG, "getAllDetails - out" + out.toString());
        return out;
    }

    public void setUserPicture(Bitmap imageBitmap){
        if (DEBUG) Log.i(LOG_TAG, "setUserPicture - enter");

        // This method is only called when the user chooses to edit his profile picture
        // before publishing a new offer
        changed_profile_picture=true;
        user_picture_string = Utilities.BitMapToString(imageBitmap);
//        if (DEBUG) Log.i(LOG_TAG, "setUserPicture picture_ui_bitmap = " + picture_bitmap);
        user_picture.setImageBitmap(imageBitmap);
        user_picture.setScaleX(1);
        user_picture.setScaleY(1);
        caption_confirm_user_picture.setText(null);
    }

    public boolean checkInputs() {

        if (DEBUG) Log.i(LOG_TAG, "checkInputs - Enter");

        if (travelling_by.getSelectedItem().toString().equals("Travelling by")) {
            return false;
        }

        if (firstname_edit.getText() == null || firstname_edit.getText().toString().equals("")) {
            return false;
        }
        if (surname_edit.getText() == null || surname_edit.getText().toString().equals("")) {
            return false;
        }
        if (phone_edit.getText() == null || phone_edit.getText().toString().equals("")) {
            return false;
        }
        if (comment_user.getText() == null || comment_user.getText().toString().equals("")) {
            return false;
        }


        return true;
    }

    public HashMap<String, String> getBlob(String nextFrag){
        HashMap<String, String> t = new HashMap<>();
        long end_time = Utilities.CurrentTimeMS();

        t.put(getString(R.string.fStart), Long.toString(fragment_start_time));
        t.put(getString(R.string.fEnd), Long.toString(end_time));
        t.put(getString(R.string.fDuration), Long.toString(end_time - fragment_start_time));

        if (!nextFrag.equals("")) {
            //Used for data reporting when switching to another fragment
            t.put(getString(R.string.nextF), nextFrag);
        }

        t.put(getString(R.string.changed_prof_pic), Boolean.toString(changed_profile_picture));

        return t;
    }

    public HashMap<String, String> getBlob(String err_field, String err_msg) {
        HashMap<String, String> t = new HashMap<>();
        long end_time = Utilities.CurrentTimeMS();
        t.put(getString(R.string.fStart), Long.toString(fragment_start_time));
        t.put(getString(R.string.fEnd), Long.toString(end_time));
        t.put(getString(R.string.fDuration), Long.toString(end_time - fragment_start_time));
        t.put(getString(R.string.changed_prof_pic), Boolean.toString(changed_profile_picture));
        t.put(err_field,err_msg);

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
    interface OnCofirmPublishListener {
        void onConfirmPublish();
        void onUserPicturePressed_Confirm();
    }
}