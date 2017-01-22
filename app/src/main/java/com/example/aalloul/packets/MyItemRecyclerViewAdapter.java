package com.example.aalloul.packets;

import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.aalloul.packets.ItemFragment.OnListFragmentInteractionListener;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;


class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

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
    private final ArrayList<String> thedates = new ArrayList<>();
    private final ArrayList<String> package_size = new ArrayList<>();
    private final ArrayList<String> transport_method = new ArrayList<>();
    private final ArrayList<String> theArrivaldates = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;


    public MyItemRecyclerViewAdapter(Cursor items, OnListFragmentInteractionListener listener) {
        Log.i(LOG_TAG, "MyItemRecyclerViewAdapter constructor Start");
        cursor = items;
        cursor.moveToFirst();
        Log.i(LOG_TAG, "Constructor - Filling arraylist");
        while (!cursor.isAfterLast()) {
            // TODO add the date
            nameAndFirstName.add(cursor.getString(cursor.getColumnIndex("firstname")));
            comments.add(cursor.getString(cursor.getColumnIndex("transport_comment")));
            phone_numbers.add(cursor.getString(cursor.getColumnIndex("phone_number")));
            destination_city.add(cursor.getString(cursor.getColumnIndex("destination_city")));
            destination_country.add(cursor.getString(cursor.getColumnIndex("destination_country")));
            source_city.add(cursor.getString(cursor.getColumnIndex("source_city")));
            source_country.add(cursor.getString(cursor.getColumnIndex("source_country")));
            n_packets.add(cursor.getString(cursor.getColumnIndex("number_packages")));
            thedates.add(cursor.getString(cursor.getColumnIndex("packet_take_by_date")));
            theArrivaldates.add(cursor.getString(cursor.getColumnIndex("packet_deliver_by_date")));
            package_size.add(cursor.getString(cursor.getColumnIndex("packet_size")));
            transport_method.add(cursor.getString(cursor.getColumnIndex("package_transport_method")));
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
        holder.firstName.setText(nameAndFirstName.get(position));
        String temp =n_packets.get(position);


        switch (Integer.parseInt(temp)) {
            case 1:
                holder.package_icon.setImageResource(R.drawable.packageicon1);
                break;
            case 2:
                holder.package_icon.setImageResource(R.drawable.packageicon2);
                break;
            case 3:
                holder.package_icon.setImageResource(R.drawable.packageicon3);
                break;
            case 4:
                holder.package_icon.setImageResource(R.drawable.packageicon4);
                break;
            case 5:
                holder.package_icon.setImageResource(R.drawable.packageicon5);
                break;
            default:
                holder.package_icon.setImageResource(R.drawable.packageicon);
        }

        holder.package_icon.setImageResource(R.drawable.packageicon1);
//        temp = source_city.get(position) +" ("+
//                Utilities.CountryToCountryCode(source_country.get(position)) + ")";
        temp = source_city.get(position);
        holder.sourceCityCountry.setText(temp);
        temp = destination_city.get(position) +" ("+
                Utilities.CountryToCountryCode(destination_country.get(position)) + ")";
        holder.destinationCityCountry.setText(temp);
        temp = thedates.get(position);
        temp = Utilities.Epoch2DateStringMillis(temp,"dd MMM");
        holder.departure_date.setText(temp);
        temp = theArrivaldates.get(position);
        temp = Utilities.Epoch2DateStringMillis(temp,"dd MMM");
//        holder.arrivalToDestination.setText(temp);
        temp = transport_method.get(position);

        Log.d(LOG_TAG, "onBindViewHolder - Transport method = " + temp);
        if (temp.equals("Car")) {
            Log.d(LOG_TAG, "onBindViewHolder - Transport method = car");
            holder.transport_icon.setImageResource(R.drawable.car);
        } else if (temp.equals("Plane")) {
            Log.d(LOG_TAG, "onBindViewHolder - Transport method = plane");
            holder.transport_icon.setImageResource(R.drawable.plane);
        } else if (temp.equals("Train")) {
            Log.d(LOG_TAG, "onBindViewHolder - Transport method = train");
            holder.transport_icon.setImageResource(R.drawable.train);
        } else {
            Log.d(LOG_TAG, "onBindViewHolder - Transport method unknown");
            holder.transport_icon.setImageResource(R.drawable.car);
        }


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
                            n_packets.get(thepos), phone_numbers.get(thepos),comments.get(thepos),
                            thepos
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
        public final TextView firstName;
        public final TextView sourceCityCountry;
        public final TextView destinationCityCountry;
        public final TextView departure_date;
//        public final TextView number_packages;
        public final ImageView transport_icon;
        public final ImageView transporter_pic;
//        public final TextView arrivalToDestination;
        public final ImageView package_icon;

        public ViewHolder(View view) {
            super(view);
            Log.i(LOG_TAG, "ViewHolder - Constructor Start");
            mView = view;
            firstName = (TextView) view.findViewById(R.id.first_name_sender);
            sourceCityCountry = (TextView) view.findViewById(R.id.source_city_country);
            destinationCityCountry = (TextView) view.findViewById(R.id.destination_city_country);
            departure_date = (TextView) view.findViewById(R.id.sender_departure_date);
            package_icon = (ImageView) view.findViewById(R.id.package_icom);
            transport_icon = (ImageView) view.findViewById(R.id.transport_method);
            transporter_pic = (ImageView) view.findViewById(R.id.transporter_picture);
//            arrivalToDestination = (TextView) view.findViewById(R.id.sender_arrival_date);

            Log.i(LOG_TAG, "ViewHolder - Constructor End");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + destinationCityCountry.getText() + "'";
        }
    }
}
