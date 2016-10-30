package com.example.aalloul.packets;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aalloul.packets.ItemFragment.OnListFragmentInteractionListener;
import java.util.ArrayList;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final static String LOG_TAG = MyItemRecyclerViewAdapter.class.getSimpleName();
    private Cursor cursor;
    private final ArrayList<String> nameAndFirstName = new ArrayList<>();
    private final ArrayList<String> destination_city = new ArrayList<>();
    private final ArrayList<String> destination_country = new ArrayList<>();
    private final ArrayList<String> source_city = new ArrayList<>();
    private final ArrayList<String> source_country = new ArrayList<>();
    private final ArrayList<String> n_packets = new ArrayList<>();
    private final ArrayList<String> comments = new ArrayList<>();
    private final ArrayList<String> phone_numbers = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;


    public MyItemRecyclerViewAdapter(Cursor items, OnListFragmentInteractionListener listener) {
        Log.i(LOG_TAG, "MyItemRecyclerViewAdapter constructor Start");
        cursor = items;
        cursor.moveToFirst();
        Log.i(LOG_TAG, "Constructor - Filling arraylist");
        while (!cursor.isAfterLast()) {
            nameAndFirstName.add(cursor.getString(cursor.getColumnIndex("firstname")));
            comments.add(cursor.getString(cursor.getColumnIndex("transport_comment")));
            phone_numbers.add(cursor.getString(cursor.getColumnIndex("phone_number")));
            destination_city.add(cursor.getString(cursor.getColumnIndex("destination_city")));
            destination_country.add(cursor.getString(cursor.getColumnIndex("destination_country")));
            source_city.add(cursor.getString(cursor.getColumnIndex("source_city")));
            source_country.add(cursor.getString(cursor.getColumnIndex("source_country")));
            n_packets.add(cursor.getString(cursor.getColumnIndex("number_packages")));
            cursor.moveToNext();
        }
        cursor.moveToFirst();

        Log.i(LOG_TAG, "Constructor - nameAndFirstName contains "+ nameAndFirstName.size() +" elements");
        Log.i(LOG_TAG, "Constructor - destination contains "+ destination_city.size() +" elements");

        mListener = listener;
        Log.i(LOG_TAG, "MyItemRecyclerViewAdapter constructor End");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG, "onCreateViewHolder - Start");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        Log.i(LOG_TAG, "onCreateViewHolder - End");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i(LOG_TAG, "onBindViewHolder - Start");
        holder.nameAndFirstName.setText(nameAndFirstName.get(position));
        final String description_text = "is going from "+source_city.get(position)
                +" to "+destination_city.get(position);
        holder.destination.setText(description_text);
        final int thepos = position;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Log.i(LOG_TAG, " onBindViewHolder - The click is on");
                    Log.i(LOG_TAG, " onBindViewHolder - "+nameAndFirstName.get(thepos));
                    mListener.onListFragmentInteraction(nameAndFirstName.get(thepos),
                            source_city.get(thepos), source_country.get(thepos),
                            destination_city.get(thepos), destination_country.get(thepos),
                            n_packets.get(thepos), phone_numbers.get(thepos),comments.get(thepos)
                            );
                }
            }
        });
        Log.i(LOG_TAG, "onBindViewHolder - End");
    }

    @Override
    public int getItemCount() {
        Log.i(LOG_TAG, "getItemCount - Start");
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameAndFirstName;
        public final TextView destination;

        public ViewHolder(View view) {
            super(view);
            Log.i(LOG_TAG, "ViewHolder - Constructor Start");
            mView = view;
            nameAndFirstName = (TextView) view.findViewById(R.id.id);
            destination = (TextView) view.findViewById(R.id.content);
            Log.i(LOG_TAG, "ViewHolder - Constructor End");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + destination.getText() + "'";
        }
    }
}
