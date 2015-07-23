package com.sgoldm.plugin;

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
        String firstImageDataString = null;
        String secondImageDataString = null;
        double sizeLimitInBytes = 0;
        try {
            firstImageDataString = args.getString(0);
            secondImageDataString = args.getString(1);
            sizeLimitInBytes = args.getDouble(2);
            sizeLimitInBytes *= MEGABYTES_MULTIPLIER;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        byte[] decodedByteArrayFirstArg = Base64.decode(firstImageDataString, Base64.DEFAULT);
        byte[] decodedByteArraySecondArg = Base64.decode(secondImageDataString, Base64.DEFAULT);
        Bitmap bitmapFirstArg = BitmapFactory.decodeByteArray(decodedByteArrayFirstArg, 0, decodedByteArrayFirstArg.length);
        Bitmap bitmapSecondArg = BitmapFactory.decodeByteArray(decodedByteArraySecondArg, 0, decodedByteArraySecondArg.length);
        
        int height = (bitmapFirstArg.getHeight() > bitmapSecondArg.getHeight()) ? bitmapFirstArg.getHeight():bitmapSecondArg.getHeight();
        int width = bitmapFirstArg.getWidth() + bitmapSecondArg.getWidth();
        
        Bitmap bmOverlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int sizeOfBitmapInBytes = width * height * 3;
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmapFirstArg, 0f, 0f, null);
        canvas.drawBitmap(bitmapSecondArg, bitmapFirstArg.getWidth(), 0f, null);
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int quality = 90;
        while(sizeOfBitmapInBytes > sizeLimitInBytes) {
            bmOverlay = getResizedBitmap(bmOverlay, (int)(bmOverlay.getHeight() * SCALE_FACTOR), (int)(bmOverlay.getWidth() * SCALE_FACTOR));
            sizeOfBitmapInBytes = bmOverlay.getRowBytes() * bmOverlay.getHeight();
        }
        bmOverlay.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] byteArray = stream.toByteArray();
        
        String decodedImageAsAString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        bmOverlay.recycle();
        
        return decodedImageAsAString;
    }
    
    /**
     * Takes one image, resizes it with params and returns it as a base 64 string.
     *
     * @param args one image as a string and a size limit
     * @return the encoded string as a Base64
     */
    private String resizeImageFromData(JSONArray args) {
        String imageDataString = null;
        double sizeLimitInBytes = 0;
        try {
            imageDataString = args.getString(0);
            sizeLimitInBytes = args.getDouble(1);
            sizeLimitInBytes *= MEGABYTES_MULTIPLIER;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        byte[] decodedByteArray = Base64.decode(imageDataString, Base64.DEFAULT);
        Bitmap bitmapOfImage = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        
        
        Bitmap bmOverlay = Bitmap.createBitmap(bitmapOfImage.getWidth(), bitmapOfImage.getHeight(), Bitmap.Config.ARGB_8888);
        int sizeOfBitmapInBytes = bitmapOfImage.getWidth() * bitmapOfImage.getHeight() * 3;
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmapOfImage, 0f, 0f, null);
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int quality = 90;
        while(sizeOfBitmapInBytes > sizeLimitInBytes) {
            bmOverlay = getResizedBitmap(bmOverlay, (int) (bmOverlay.getHeight() * SCALE_FACTOR), (int) (bmOverlay.getWidth() * SCALE_FACTOR));
            sizeOfBitmapInBytes = bmOverlay.getRowBytes() * bmOverlay.getHeight();
        }
        bmOverlay.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] byteArray = stream.toByteArray();
        
        String decodedImageAsAString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        bmOverlay.recycle();
        
        return decodedImageAsAString;
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
}
