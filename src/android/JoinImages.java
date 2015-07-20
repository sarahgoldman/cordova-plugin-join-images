package com.sgoldm.plugin;
 
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import com.sgoldm.plugin.ImageService;

public class JoinImages extends CordovaPlugin {
	
	public static final String ACTION_JOIN_IMAGES_FROM_DATA = "joinImagesFromData";
	public static final String ACTION_JOIN_IMAGES_FROM_URLS = "joinImagesFromURLs";
	public static final String ACTION_RESIZE_IMAGE_FROM_DATA = "resizeImageFromData";
	public static final String ACTION_RESIZE_IMAGE_FROM_URL = "resizeImageFromURL";
	
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	

        if (action.equals(ACTION_JOIN_IMAGES_FROM_DATA)) {

			String encodedImage = joinImagesFromData(args);
			callbackContext.success(encodedImage);
			
            return true;

        } else if (action.equals(ACTION_JOIN_IMAGES_FROM_URLS)) {
	
			String encodedImage = joinImagesFromURLs(args);
			callbackContext.success(encodedImage);
			
			return true;

		} else if (action.equals(ACTION_RESIZE_IMAGE_FROM_DATA)) {

			String encodedImage = resizeImageFromData(args);
			callbackContext.success(encodedImage);
			
			return true;

		} else if (action.equals(ACTION_RESIZE_IMAGE_FROM_URL)) {

			String encodedImage = resizeImageFromURL(args);
			callbackContext.success(encodedImage);

			return true;

		} else {

			callbackContext.error("Invalid action");
			return false;

        }
    }

	private String joinImagesFromData(args) {
		// args = [<first image data string>, <second image data string>, <size double>]
		String firstImageDataString = args.getString(0);
		String secondImageDataString = args.getString(1);
		double sizeLimit = args.getDouble(2);
		
		// decode the images from the two data strings, join, resize, and return base 64 encoded
		return 'encodedString';
	}
	
	private String joinImagesFromURLs(args) {
		// args = [<first image url string>, <second image url string>, <size double>]
		String firstImageUrlString = args.getString(0);
		String secondImageUrlString = args.getString(1);
		double sizeLimit = args.getDouble(2);
		
		// get the images from the two url strings, join, resize, and return base 64 encoded
		return 'encodedString';
	}
	
	private String resizeImageFromData(args) {
		// args = [<image data string>, <size double>]
		String imageDataString = args.getString(0);
		double sizeLimit = args.getDouble(1);
		
		// decode the image from the data string, resize, and return base 64 encoded
		return 'encodedString';
	}
	
	private String resizeImageFromURL(args) {
		// args = [<image url string>, <size double>]
		String imageUrlString = args.getString(0);
		double sizeLimit = args.getDouble(1);
		
		// get the image from the url string, resize, and return base 64 encoded
		return 'encodedString';
	}
	
}