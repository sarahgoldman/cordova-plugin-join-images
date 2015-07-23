Join Images plugin for Cordova
======================================================

This plugin can be used to join two images into one image and resize it, or just resize one image. It returns the final image as a base64 encoded JPG.

## Usage

**Join two images:**
```
plugins.JoinImages.join(options);
```

The default options object
```
var options = {

	firstImage: null, 	// first image, base64 string (required)

	secondImage: null, 	// second image, base64 string (required)

	size: 5, 			// size of final image in MB

	success: null,		// success callback function, argument will be the final 
						// encoded string... success: function(encodedImage) { }

	error: null			// error callback function, argument will be the error 
						// message string... error: function(message) { }
};
```

**Resize one image:**
```
plugins.JoinImages.resize(options);
```

The default options object
```
var options = {

	image: null, 		// image, base64 string (required)

	size: 5, 			// size of final image in MB

	success: null,		// success callback function, argument will be the final 
						// encoded string... success: function(encodedImage) { }

	error: null			// error callback function, argument will be the error 
						// message string... error: function(message) { }
};
```

## Example:

```
plugins.JoinImages.resize({
	image: 'dataString',
	size: 1.5,
	success: function(encodedImage){
		document.getElementById('result').src = 'data:image/jpeg;base64,'+encodedImage;
	},
	error: function(message){
		console.log(message);
	}
});
```
