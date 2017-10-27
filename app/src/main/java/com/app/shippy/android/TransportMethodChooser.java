package com.app.shippy.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class TransportMethodChooser extends Fragment {

    private onTransportMethodChooserInteraction mListener;
    private long fragmentStartTime;
    private final static String LOG_TAG = "TransportMethodChooser";
    private View view;
    private ImageView car, plane, train;
    private Button nextbutton;
    private boolean carChecked=false, planeChecked=false, trainChecked=false;

    public TransportMethodChooser() {
        // Required empty public constructor
        fragmentStartTime = Utilities.CurrentTimeMS();
    }


    public static TransportMethodChooser newInstance() {
        return new TransportMethodChooser();
    }

    long getFragmentStartTime() {
        return fragmentStartTime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transport_method_chooser, container, false);

        // The next button must be set first.
        setNextButton();
        setTrainButton();
        setCarButton();
        setPlaneButton();

        return view;
    }

    private void setCarButton() {
        car = view.findViewById(R.id.transport_chooser_car);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCarColor();
            }
        });
    }

    private void setCarColor() {
        Log.i(LOG_TAG, "setCarColor - Enter");

        // If user is travelling, he can only select 1 transport method.
        if (!mListener.getTripRequestDetailsForTransportChooser().isSendingAPackage()) {
            cancelAllColors();
        }
        if (carChecked) {
            Log.i(LOG_TAG, "setCarColor - car color is not null");
            car.setBackground(null);
            car.setElevation(0);
            carChecked = false;
        } else {
            Log.i(LOG_TAG, "setCarColor - car color is null");
            car.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
            car.setElevation(24);
            carChecked = true;
        }

        setNextButtonColor();
    }

    private void cancelAllColors() {
        if (carChecked) {
            car.setBackground(null);
            car.setElevation(0);
        }

        if (trainChecked) {
            train.setBackground(null);
            train.setElevation(0);
        }

        if (planeChecked) {
            plane.setBackground(null);
            plane.setElevation(0);
        }
    }

    private void setPlaneButton() {
        plane = view.findViewById(R.id.transport_chooser_plane);
        plane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlaneColor();
            }
        });
    }

    private void setPlaneColor() {
        Log.i(LOG_TAG, "setCarColor - Enter");

        if (!mListener.getTripRequestDetailsForTransportChooser().isSendingAPackage()) {
            cancelAllColors();
        }

        if (planeChecked) {
            Log.i(LOG_TAG, "setCarColor - plane color is not null");
            plane.setBackground(null);
            plane.setElevation(0);
            planeChecked = false;
        } else {
            Log.i(LOG_TAG, "setCarColor - plane color is null");
            plane.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
            plane.setElevation(24);
            planeChecked = true;
        }

        setNextButtonColor();
    }

    private void setTrainButton() {
        train = view.findViewById(R.id.transport_chooser_train);
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTrainColor();
            }
        });
    }

    private void setTrainColor() {
        Log.i(LOG_TAG, "setCarColor - Enter");
        if (!mListener.getTripRequestDetailsForTransportChooser().isSendingAPackage()) {
            cancelAllColors();
        }

        if (trainChecked) {
            Log.i(LOG_TAG, "setCarColor - train color is not null");
            train.setBackground(null);
            train.setElevation(0);
            trainChecked = false;

        } else {
            Log.i(LOG_TAG, "setCarColor - train color is null");
            train.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
            train.setElevation(24);
            trainChecked = true;
        }

        setNextButtonColor();
    }

    private void setNextButton() {
        nextbutton = view.findViewById(R.id.transport_chooser_next);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTransportMethodChooserNextButtonPressed();
            }
        });
        setNextButtonColor();
    }

    private void setNextButtonColor() {
        Log.i(LOG_TAG, "setNextButtonColor - Enter");
        if (isInputOk()) {
            Log.i(LOG_TAG, "setNextButtonColor - Input is ok");
            nextbutton.setBackgroundTintList(ContextCompat.getColorStateList(
                    getActivity(), R.color.colorSecondary));
        } else {
            nextbutton.setBackgroundTintList(ContextCompat.getColorStateList(
                    getActivity(), R.color.backgroundApp));
        }
    }

    boolean isInputOk() {
        Log.i(LOG_TAG, "isInputOk - " + (carChecked || trainChecked || planeChecked));
        return carChecked || trainChecked || planeChecked;
    }

    boolean isCarChecked() {
        return carChecked;
    }

    boolean isPlaneChecked() {
        return planeChecked;
    }

    boolean isTrainChecked() {
        return trainChecked;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onTransportMethodChooserInteraction) {
            mListener = (onTransportMethodChooserInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onTransportMethodChooserInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    interface onTransportMethodChooserInteraction {
        void onTransportMethodChooserNextButtonPressed();
        TripRequestDetails getTripRequestDetailsForTransportChooser();
    }
}
