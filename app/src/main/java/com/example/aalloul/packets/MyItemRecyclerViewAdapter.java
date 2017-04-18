package com.example.aalloul.packets;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aalloul.packets.ItemFragment.OnListFragmentInteractionListener;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_DROPOFFDATE;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_FIRSTNAME ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_PHONENUMBER ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_PICTURE ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_PICKUPCITY ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_PICKUPCOUNTRY ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_DROPOFFCITY ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_DROPOFFCOUNTRY ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_USERCOMMENT ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_SIZEPACKAGES ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_NUMBERPACKAGES ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_PICKUPDATE ;
import static com.example.aalloul.packets.DataBaseContracts.Postmen.COLUMN_NAME_TRAVELBY;


class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final static String LOG_TAG = MyItemRecyclerViewAdapter.class.getSimpleName();
    private final static boolean DEBUG = false;

//    private Cursor cursor;
    private final ArrayList<String> firstnames = new ArrayList<>();
    private final ArrayList<String> dropoff_cities = new ArrayList<>();
    private final ArrayList<String> dropoff_countries = new ArrayList<>();
    private final ArrayList<String> pickup_cities = new ArrayList<>();
    private final ArrayList<String> pickup_countries = new ArrayList<>();
    private final ArrayList<String> n_packets = new ArrayList<>();
    private final ArrayList<String> comments = new ArrayList<>();
    private final ArrayList<String> phone_numbers = new ArrayList<>();
    private final ArrayList<String> package_sizes = new ArrayList<>();
    private final ArrayList<String> transport_methods = new ArrayList<>();
    private final ArrayList<String> dropoff_dates = new ArrayList<>();
    private final ArrayList<String> pickup_dates = new ArrayList<>();
    private final ArrayList<String> pictures = new ArrayList<>();
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
        firstnames.clear();
        comments.clear();
        phone_numbers.clear();
        pictures.clear();
        dropoff_cities.clear();
        dropoff_countries.clear();
        pickup_cities.clear();
        pickup_countries.clear();
        n_packets.clear();
        dropoff_dates.clear();
        pickup_dates.clear();
        package_sizes.clear();
        transport_methods.clear();

        while (!cursor.isAfterLast()) {
            firstnames.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRSTNAME)));
            comments.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERCOMMENT)));
            phone_numbers.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONENUMBER)));

            if (cursor.getColumnIndex(COLUMN_NAME_PICTURE) >= 0) {
                pictures.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICTURE)));
            } else {
                if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter - Picture not found");
                pictures.add(null);
            }

            dropoff_cities.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DROPOFFCITY)));
            dropoff_countries.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DROPOFFCOUNTRY)));

            pickup_cities.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICKUPCITY)));
            pickup_countries.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICKUPCOUNTRY)));

            n_packets.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NUMBERPACKAGES)));
            if (cursor.getColumnIndex(COLUMN_NAME_DROPOFFDATE) >=0) {
                dropoff_dates.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DROPOFFDATE)));
            } else {
                if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter - COLUMN_NAME_DROPOFFDATE not found");
            }

            if (cursor.getColumnIndex(COLUMN_NAME_PICKUPDATE) >= 0) {
                pickup_dates.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PICKUPDATE)));
            } else {
                if (DEBUG) Log.i(LOG_TAG, "MyItemRecyclerViewAdapter - COLUMN_NAME_PICKUPDATE not found");
            }
            package_sizes.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SIZEPACKAGES)));
            transport_methods.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TRAVELBY)));
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - Start");
        String temp;

        holder.firstName.setText(firstnames.get(holder.getAdapterPosition()));
//        String temp =n_packets.get(holder.getAdapterPosition());
        holder.number_packages.setText("x"+n_packets.get(holder.getAdapterPosition()));

        holder.pickup_city.setText(pickup_cities.get(holder.getAdapterPosition()));
        holder.dropoff_city.setText(dropoff_cities.get(holder.getAdapterPosition()));

        holder.pickup_date.setText(
                Utilities.Epoch2Date(pickup_dates.get(holder.getAdapterPosition()),"dd MMM"));

        temp = transport_methods.get(holder.getAdapterPosition());
        Log.i(LOG_TAG, "onBindViewHolder - Transport method = "+temp);
        switch (temp) {
            case "Car":
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method = car");
                holder.transport_icon.setImageResource(R.drawable.car);
                break;
            case "Plane":
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method = plane");
                holder.transport_icon.setImageResource(R.drawable.plane);
                break;
            case "Train":
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method = train");
                holder.transport_icon.setImageResource(R.drawable.train);
                break;
            default:
                if (DEBUG) Log.d(LOG_TAG, "onBindViewHolder - Transport method unknown");
                holder.transport_icon.setImageResource(R.drawable.car);
                break;
        }

        temp = package_sizes.get(holder.getAdapterPosition());
        switch (temp) {
            case "Small":
                holder.package_size.setText("S");
                break;
            case "Medium":
                holder.package_size.setText("M");
                break;
            case "Large":
                holder.package_size.setText("L");
                break;
        }

        // Set the picture, if available
        if (pictures.get(holder.getAdapterPosition()) == null ||
                pictures.get(holder.getAdapterPosition()).equals("")
                ) {
            if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - picture is not null");
            holder.transporter_pic.setImageResource(R.mipmap.user_icon_bevel);
        } else {
            if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - picture is not null");
            holder.transporter_pic.setImageBitmap(
                    Utilities.StringToBitMap(
                            pictures.get(holder.getAdapterPosition())
                    )
            );

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (DEBUG) Log.i(LOG_TAG, " onBindViewHolder - The click is on");
                    if (DEBUG) Log.i(LOG_TAG, " onBindViewHolder - "+firstnames.get(holder.getAdapterPosition()));
                    mListener.onOfferSelected(
                            firstnames.get(holder.getAdapterPosition()),
                            pickup_cities.get(holder.getAdapterPosition()),
                            pickup_countries.get(holder.getAdapterPosition()),
                            pickup_dates.get(holder.getAdapterPosition()),
                            dropoff_cities.get(holder.getAdapterPosition()),
                            dropoff_countries.get(holder.getAdapterPosition()),
                            n_packets.get(holder.getAdapterPosition()),
                            package_sizes.get(holder.getAdapterPosition()),
                            pictures.get(holder.getAdapterPosition()),
                            phone_numbers.get(holder.getAdapterPosition()),
                            transport_methods.get(holder.getAdapterPosition()),
                            comments.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            }
        });
        if (DEBUG) Log.i(LOG_TAG, "onBindViewHolder - End");
    }

    @Override
    public int getItemCount() {
        if (DEBUG) Log.i(LOG_TAG, "getItemCount - Start");
        return firstnames.size();
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
