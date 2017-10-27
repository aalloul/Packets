package com.app.shippy.android;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.shippy.android.ItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_DROPOFFDATE;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_FIRSTNAME ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_PHONENUMBER ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_PICTURE ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_PICKUPCITY ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_PICKUPCOUNTRY ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_DROPOFFCITY ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_DROPOFFCOUNTRY ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_USERCOMMENT ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_SIZEPACKAGES ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_NUMBERPACKAGES ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_PICKUPDATE ;
import static com.app.shippy.android.DataBaseContracts.Postmen.COLUMN_NAME_TRAVELBY;


class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final static String LOG_TAG = MyItemRecyclerViewAdapter.class.getSimpleName();
    private final static boolean DEBUG = false;
    private final ArrayList<TripOffer> tripOffers = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;

    MyItemRecyclerViewAdapter(Cursor items, OnListFragmentInteractionListener listener) {
        if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter constructor Start");
        PopulateTheArrays(items);
        mListener = listener;
        if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter constructor End");
    }

    /**
     * This function allows "notifyDataSetChanged" to work as it updates the content of the arrays
     * used for the RecyclerView
     * @param cursor this is the new data
     */
    void updateCursor(Cursor cursor) {
        PopulateTheArrays(cursor);
    }

    /**
     * I moved the filling of the Arrays to a separate method to allow the @updateCursor method
     * to also call it
     * @param cursor contains the data from SqLite
     */
    private void PopulateTheArrays(Cursor cursor) {
        cursor.moveToFirst();
        tripOffers.clear();
        TripOffer tripOffer;

        while (!cursor.isAfterLast()) {
            tripOffer = new TripOffer();
            tripOffer.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRSTNAME)));
            tripOffer.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERCOMMENT)));
            tripOffer.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONENUMBER)));

            if (cursor.getColumnIndex(COLUMN_NAME_PICTURE) >= 0) {
                tripOffer.setPicture_str(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICTURE)));
                tripOffer.setPictureBM(Utilities.StringToBitMap(cursor.getString(
                        cursor.getColumnIndex(COLUMN_NAME_PICTURE))));
            } else {
                if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter - Picture not found");
                tripOffer.setPicture_str(null);
            }

            tripOffer.setDropoffCity(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DROPOFFCITY)));
            tripOffer.setDropoffCountry(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DROPOFFCOUNTRY)));
            tripOffer.setPickupCity(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICKUPCITY)));
            tripOffer.setPickupCountry(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICKUPCOUNTRY)));
            tripOffer.setNumberPackages(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_NUMBERPACKAGES)));

            if (cursor.getColumnIndex(COLUMN_NAME_DROPOFFDATE) >=0) {
                tripOffer.setDropoffDate(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DROPOFFDATE)));
            } else {
                if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter - COLUMN_NAME_DROPOFFDATE not found");
            }

            if (cursor.getColumnIndex(COLUMN_NAME_PICKUPDATE) >= 0) {
                if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter - COLUMN_NAME_PICKUPDATE found=" +
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICKUPDATE)));
                tripOffer.setPickupDate(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_PICKUPDATE)));
            } else {
                if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter - COLUMN_NAME_PICKUPDATE not found");
            }

            tripOffer.setPackageSize(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SIZEPACKAGES)));
            tripOffer.setTravelBy(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TRAVELBY)));

            tripOffers.add(tripOffer);
            cursor.moveToNext();
        }
        cursor.moveToFirst();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (DEBUG) Log.i(LOG_TAG, "onCreateViewHolder - Start");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        if (DEBUG) Log.i(LOG_TAG, "onCreateViewHolder - End");
        return new ViewHolder(view);
    }

    private void setFirstName(final ViewHolder holder, TripOffer tripOffer) {
        holder.firstName.setText(tripOffer.getFirstName());
    }

    private void setNumberPackages(final ViewHolder holder, TripOffer tripOffer) {
        String mystr = "x"+tripOffer.getNumberPackages();
        holder.number_packages.setText(mystr);
    }

    private void setPickupCity(final ViewHolder holder, TripOffer tripOffer) {
        holder.pickup_city.setText(tripOffer.getPickupCity());
    }

    private void setDropoffCity(final ViewHolder holder, TripOffer tripOffer) {
        holder.dropoff_city.setText(tripOffer.getDropoffCity());
    }

    private void setPickupDate(final ViewHolder holder, TripOffer tripOffer) {
        if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - setting date to "+
                Utilities.Epoch2Date(tripOffer.getPickupDate(),"dd MMM"));

        holder.pickup_date.setText(Utilities.Epoch2Date(tripOffer.getPickupDate(),"dd MMM"));
    }

    private void setTravelBy(final ViewHolder holder, TripOffer tripOffer) {
        int temp_int = tripOffer.getTravelBy();
        if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - Transport method = "+temp_int);
        switch (temp_int) {
            case 0:
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method = car");
                holder.transport_icon.setImageResource(R.mipmap.car);
                break;
            case 1:
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method = train");
                holder.transport_icon.setImageResource(R.mipmap.train);
                break;
            case 2:
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method = plane");
                holder.transport_icon.setImageResource(R.mipmap.plane);
                break;
            default:
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method unknown");
                holder.transport_icon.setImageResource(R.mipmap.car);
                break;
        }
    }

    private void setPackageSize(final ViewHolder holder, TripOffer tripOffer) {
        int temp_int = tripOffer.getPackageSize();
        switch (temp_int) {
            case 0:
                holder.package_size.setText("S");
                break;
            case 1:
                holder.package_size.setText("M");
                break;
            case 2:
                holder.package_size.setText("L");
                break;
        }
    }

    private void setPicture(final ViewHolder holder, TripOffer tripOffer) {
        // Set the picture, if available
        if (tripOffer.getPicture_str().equals("")) {
            if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - picture is not null");
            holder.transporter_pic.setImageResource(R.mipmap.user_icon_bevel);
        } else {
            if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - picture is not null");
            holder.transporter_pic.setImageBitmap(tripOffer.getPictureBM());

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - Start");

        TripOffer tripOffer = tripOffers.get(holder.getAdapterPosition());

        setFirstName(holder, tripOffer);
        setNumberPackages(holder, tripOffer);
        setPickupCity(holder, tripOffer);
        setDropoffCity(holder, tripOffer);
        setPickupDate(holder, tripOffer);
        setTravelBy(holder, tripOffer);
        setPackageSize(holder, tripOffer);
        setPicture(holder, tripOffer);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (DEBUG) Log.i(LOG_TAG, " onBindViewHolder - The click is on");
                    TripOffer tripOffer = tripOffers.get(holder.getAdapterPosition());
                    if (DEBUG) Log.i(LOG_TAG, " onBindViewHolder - "+tripOffer.getFirstName());
                    mListener.onOfferSelected(tripOffer, holder.getAdapterPosition());}
            }
        });
        if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - End");
    }

    @Override
    public int getItemCount() {
        if (DEBUG) Log.i(LOG_TAG, "getItemCount - Start");
        return tripOffers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView firstName;
        final TextView pickup_city;
        final TextView dropoff_city;
        final TextView pickup_date;
        final ImageView transport_icon;
        final ImageView transporter_pic;
        final TextView package_size;
        final TextView number_packages;

        ViewHolder(View view) {
            super(view);
            if (DEBUG) Log.i(LOG_TAG, "ViewHolder - Constructor Start");
            mView = view;
            firstName = (TextView) view.findViewById(R.id.first_name_sender);
            pickup_city = (TextView) view.findViewById(R.id.pickup_city);
            dropoff_city = (TextView) view.findViewById(R.id.dropoff_city);
            pickup_date = (TextView) view.findViewById(R.id.pickup_date);
            package_size = (TextView) view.findViewById(R.id.package_size);
            transport_icon = (ImageView) view.findViewById(R.id.transport_method);
            transporter_pic = (ImageView) view.findViewById(R.id.transporter_picture);
            number_packages = (TextView) view.findViewById(R.id.number_packages);
            if (DEBUG) Log.i(LOG_TAG, "ViewHolder - Constructor End");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + dropoff_city.getText() + "'";
        }
    }
}
