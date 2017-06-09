package com.app.shippy.android;

/**
 * Created by adamalloul on 24/12/2016.
 */

import java.io.IOException;
import java.util.Arrays;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

class ExifUtil {
    /*
     * Kindly provided by 9re https://gist.github.com/9re/1990019
     */
    static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                DataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "ExifUtil", "rotateBitmap");
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            DataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "ExifUtil", "rotateBitmap");

            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        ExifInterface exif = new ExifInterface(src);
        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
    }
}