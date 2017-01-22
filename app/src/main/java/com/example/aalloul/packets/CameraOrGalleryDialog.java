package com.example.aalloul.packets;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by adamalloul on 25/12/2016.
 */

public class CameraOrGalleryDialog extends DialogFragment {


    CameraOrGalleryInterface mListener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.add_profile_picture))
                .setItems(R.array.camera_or_gallery, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onCameraOrGallerySelected(dialog, which);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CameraOrGalleryInterface) {
            mListener = (CameraOrGalleryInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegistrationPageListener");
        }


    }


    public interface CameraOrGalleryInterface {
        void onCameraOrGallerySelected(DialogInterface dialog, int which);
    }
}
