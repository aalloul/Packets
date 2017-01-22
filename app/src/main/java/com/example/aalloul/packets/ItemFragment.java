package com.example.aalloul.packets;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "1";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private HashMap<String, String> queryParams = new HashMap<>();
    Cursor theCursor;
    private final static String LOG_TAG = ItemFragment.class.getSimpleName();
    private Postman postman;
    public static MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private long fragment_start_time;
    private int n_results;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
        queryParams.put("packet_take_by_date","1470002400000");
        queryParams.put("packet_deliver_by_date","1501538400000");
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public static ItemFragment newInstance(int columnCount) {
        Log.i(LOG_TAG, "newInstance -- Start");
        ItemFragment fragment = new ItemFragment();
        Log.i(LOG_TAG, "newInstance- get bundle");
        Bundle args = new Bundle();
        Log.i(LOG_TAG, "newInstance - add column count");
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        Log.i(LOG_TAG, "newInstance - set args");
        fragment.setArguments(args);
        Log.i(LOG_TAG, "newInstance -- End");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG, "onCreate -- Start");

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        Log.i(LOG_TAG, "onCreate - End");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView -- Start");

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Log.i(LOG_TAG, "onCreateView - get Context");
            Context context = view.getContext();

            Log.i(LOG_TAG, "onCreateView - get recyclerView");
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                Log.i(LOG_TAG, "onCreateView -- Choosing Linear Layout Manager");
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                Log.i(LOG_TAG, "onCreateView -- Choosing Grid Layout Manager");
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            Log.i(LOG_TAG, "onCreateView -- Postmen stuff");
            postman = new Postman(context, queryParams);
            theCursor = postman.get_Data_For_ListView(queryParams);
            n_results = theCursor.getCount();
            Log.i(LOG_TAG, "The query returned " + theCursor.getCount() + " elements");
            myItemRecyclerViewAdapter =
                    new MyItemRecyclerViewAdapter(theCursor, mListener);
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
        } else {
            Log.i(LOG_TAG, "view is not instance of recyclerView");
        }

        Log.i(LOG_TAG, "onCreateView -- End");
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.i(LOG_TAG, "onAttach -- Start");

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        Log.i(LOG_TAG, "onAttach -- End");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause - start");
        if (postman != null){
            Log.i(LOG_TAG, "onPause - database not null, closing it");
            postman.closeDatabase();
        }
        Log.i(LOG_TAG, "onPause - exit");

    }

    public long getFragment_start_time() {
        return fragment_start_time;
    }

    public int getN_results(){
        return n_results;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String nameAndFirstName, String source_city,
                                       String source_country,  String destination_city,
                                       String destination_country, String n_packets,
                                       String phone_number, String comments, int thepos );
    }


}
