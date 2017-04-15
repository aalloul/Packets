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

public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "1";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Cursor theCursor;
    private final static String LOG_TAG = ItemFragment.class.getSimpleName();
    private Postman postman;
    public static MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private long fragment_start_time;
    private int n_results;
    private final static boolean DEBUG = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
        fragment_start_time = Utilities.CurrentTimeMS();
    }

    public static ItemFragment newInstance(int columnCount) {
        Log.i(LOG_TAG, "newInstance -- Start");
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        if (DEBUG) Log.i(LOG_TAG, "newInstance -- End");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.i(LOG_TAG, "onCreateView -- Start");

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        getActivity().setTitle(getString(R.string.offers_towards));
        // Set the adapter
        if (view instanceof RecyclerView) {
            if (DEBUG) Log.i(LOG_TAG, "onCreateView - get Context");
            Context context = view.getContext();

            if (DEBUG) Log.i(LOG_TAG, "onCreateView - get recyclerView");
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                if (DEBUG) Log.i(LOG_TAG, "onCreateView -- Choosing Linear Layout Manager");
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                if (DEBUG) Log.i(LOG_TAG, "onCreateView -- Choosing Grid Layout Manager");
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if (DEBUG) Log.i(LOG_TAG, "onCreateView -- Postmen stuff");
            postman = new Postman(context);
            theCursor = postman.get_Data_For_ListView();
            n_results = theCursor.getCount();
            if (DEBUG) Log.i(LOG_TAG, "The query returned " + theCursor.getCount() + " elements");
            myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(theCursor, mListener);
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
        } else {
            if (DEBUG) Log.e(LOG_TAG, "view is not instance of recyclerView");
        }

        if (DEBUG) Log.i(LOG_TAG, "onCreateView -- End");
        return view;
    }


    public void updateData() {
        if (DEBUG) Log.i(LOG_TAG, "updateData - notifying change");
        theCursor = postman.get_Data_For_ListView();
        myItemRecyclerViewAdapter.updateCursor(theCursor);
        myItemRecyclerViewAdapter.notifyDataSetChanged();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (DEBUG) Log.i(LOG_TAG, "onPause - start");
        if (postman != null){
            if (DEBUG) Log.i(LOG_TAG, "onPause - database not null, closing it");
            postman.closeDatabase();
        }
        if (DEBUG) Log.i(LOG_TAG, "onPause - exit");

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
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String nameAndFirstName, String source_city,
                                       String source_country,  String destination_city,
                                       String destination_country, String n_packets,
                                       String phone_number, String comments, int thepos );
    }


}
