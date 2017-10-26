package com.app.shippy.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class TransportMethodChooser extends Fragment {

    private onTransportMethodChooserInteraction mListener;
    private long fragmentStartTime;
    private final static String LOG_TAG = "TransportMethodChooser";
    private View view;
    private ImageView car, plane, train;

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
        setCarButton();
        setPlaneButton();
        setTrainButton();
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
        if (car.getBackground() == null) {
            Log.i(LOG_TAG, "setCarColor - car color is null");
            car.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            car.setElevation(24);
        } else {
            Log.i(LOG_TAG, "setCarColor - car color is not null");
            car.setBackground(null);
            car.setElevation(0);
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
        if (plane.getBackground() == null) {
            Log.i(LOG_TAG, "setCarColor - plane color is null");
            plane.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            plane.setElevation(24);
        } else {
            Log.i(LOG_TAG, "setCarColor - plane color is not null");
            plane.setBackground(null);
            plane.setElevation(0);
        }
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
        if (train.getBackground() == null) {
            Log.i(LOG_TAG, "setCarColor - train color is null");
            train.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            train.setElevation(24);
        } else {
            Log.i(LOG_TAG, "setCarColor - train color is not null");
            train.setBackground(null);
            train.setElevation(0);
        }
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
    }
}
