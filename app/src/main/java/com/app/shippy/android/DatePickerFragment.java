package com.app.shippy.android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = DatePickerFragment.class.getSimpleName();
    private final static boolean DEBUG = false;
    private long min_date;
    private TheListener listener;

    interface TheListener{
        void returnDate(String date);
    }

    public DatePickerFragment() {
        min_date = Utilities.CurrentTimeMS();
    }


    void setMin_date(long min_date) {
        this.min_date = min_date;
    }

    public void onAttach(Context context){
        super.onAttach(context);
        try {
            if (DEBUG) Log.i(LOG_TAG, "IS instance");
            listener =  (TheListener) context;
        } catch (ClassCastException e){
            if (DEBUG) Log.i(LOG_TAG, "is not instance");
            throw new ClassCastException(context.toString() + "must implement TheListener");
        }

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
//        listener = (TheListener) getParentFragment();
        if (DEBUG) Log.i(LOG_TAG, "listener = "+listener);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new
                DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(min_date);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (DEBUG) Log.i(LOG_TAG, "onDateSet - start");
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        if (DEBUG) Log.i(LOG_TAG, "onDateSet - formattedDate = "+formattedDate);

        if (listener != null)
        {
            if (DEBUG) Log.i(LOG_TAG, "onDateSet - Listener not null");
            listener.returnDate(formattedDate);

        }
        if (DEBUG) Log.i(LOG_TAG, "onDateSet - exit");
    }
}
