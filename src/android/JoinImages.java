package com.sgoldm.plugin.joinImages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Base64;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.ByteArrayOutputStream;

/**
 * Class that joins two images together using an Android Canvas and Bitmap.
 *
 * @author ocarty
 */
public class JoinImages extends CordovaPlugin {
    
    public static final String ACTION_JOIN_IMAGES_FROM_DATA = "joinImagesFromData";
    public static final String ACTION_RESIZE_IMAGE_FROM_DATA = "resizeImageFromData";
    public static final int MEGABYTES_MULTIPLIER = 1048576;
    public static final double SCALE_FACTOR = 0.8;
    public static final double DEFAULT_MB_LIMIT = 5.0;
    public static final int QUALITY = 90;
    public static final Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    
    /**
     * Method that connects the javascript code to the native android code.
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return true if action can be successfully completed, false otherwise.
     * @throws JSONException if JSON args cannot be read.
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        if (action.equals(ACTION_JOIN_IMAGES_FROM_DATA)) {
            String encodedImage = joinImagesFromData(args);
            callbackContext.success(encodedImage);
            return true;
        }
        else if (action.equals(ACTION_RESIZE_IMAGE_FROM_DATA)) {
            String encodedImage = resizeImageFromData(args);
            callbackContext.success(encodedImage);
            return true;
            
        }
        else {
            callbackContext.error("Invalid action");
            return false;
        }
    }
    
    /**
     * Joins the image from two data strings.
     *
     * @param args the first image, second image, and size limit
     * @return the encoded string as a Base64
     */
    private String joinImagesFromData(JSONArray args) {
        String firstImageDataString = args.optString(0, "");
        String secondImageDataString = args.optString(1, "");
        double sizeLimitInBytes = args.optDouble(2, DEFAULT_MB_LIMIT) * MEGABYTES_MULTIPLIER;

        // get the images as bitmaps
        Bitmap bitmapFirstArg = getBitmapFromEncodedString(firstImageDataString);
        Bitmap bitmapSecondArg = getBitmapFromEncodedString(secondImageDataString);

        // use the height of the taller image
        int height = (bitmapFirstArg.getHeight() > bitmapSecondArg.getHeight()) ? bitmapFirstArg.getHeight():bitmapSecondArg.getHeight();
        // and combine the widths because they will be joined horizontally
        int width = bitmapFirstArg.getWidth() + bitmapSecondArg.getWidth();

        // create a new bitmap of the joined images
        Bitmap bmOverlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);
		// place the first image at 0,0
        canvas.drawBitmap(bitmapFirstArg, 0f, 0f, null);
		// place the second image right next to it (firstWidth, 0)
        canvas.drawBitmap(bitmapSecondArg, bitmapFirstArg.getWidth(), 0f, null);

        // scale it down and encode it, return that string
        return scaleAndEncodeBitmap(bmOverlay, sizeLimitInBytes);
    }
    
    /**
     * Takes one image, resizes it with params and returns it as a base 64 string.
     *
     * @param args one image as a string and a size limit
     * @return the encoded string as a Base64
     */
    private String resizeImageFromData(JSONArray args) {

        String imageDataString = args.optString(0, "");
        double sizeLimitInBytes = args.optDouble(1, DEFAULT_MB_LIMIT) * MEGABYTES_MULTIPLIER;

        // get the image as bitmap
        Bitmap bitmapOfImage = getBitmapFromEncodedString(imageDataString);

        // scale it down and encode it, return that string
        return scaleAndEncodeBitmap(bitmapOfImage, sizeLimitInBytes);
    }

    /**
     * Method that scales down a bitmap and returns it as a base64 encoded string
     *
     * @param bitmap the bitmap passed in
     * @param sizeLimitInBytes the size limit in bytes
     * @return the scaled down bitmap as an encoded string
     */
    private String scaleAndEncodeBitmap(Bitmap bitmap, double sizeLimitInBytes) {
        // get the current size in bytes
        int sizeOfBitmapInBytes = bitmap.getRowBytes() * bitmap.getHeight();
        // create output stream
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // while its outside of the size limit
        while(sizeOfBitmapInBytes > sizeLimitInBytes) {
            // scale it down and check the size again
            bitmap = getResizedBitmap(bitmap, (int) (bitmap.getHeight() * SCALE_FACTOR), (int) (bitmap.getWidth() * SCALE_FACTOR));
            sizeOfBitmapInBytes = bitmap.getRowBytes() * bitmap.getHeight();
        }
        // compress it and get byte array
        bitmap.compress(COMPRESS_FORMAT, QUALITY, stream);
        byte[] byteArray = stream.toByteArray();

        // encode it to base64 string, recycle the bitmap and return the string
        String encodedImageAsAString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        bitmap.recycle();

        return encodedImageAsAString;

    }

    /**
     * Method that returns a resized bitmap based on the height and width passed in.
     *
     * @param bitmap the bitmap passed in
     * @param newHeight the new height that the bitmap should be scaled to
     * @param newWidth the new width that the bitmap should be scaled to
     * @return the scaled bitmap
     */
    private Bitmap getResizedBitmap(Bitmap bitmap, int newHeight, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    /**
     * Method that returns a bitmap from a base64 string
     *
     * @param encodedString the encoded data passed in
     * @return bitmap Of image
     */
    private Bitmap getBitmapFromEncodedString(String encodedString) {
        byte[] decodedByteArray = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap bitmapOfImage = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        return bitmapOfImage;
    }
}
