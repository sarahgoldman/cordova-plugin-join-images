Join Images plugin for Cordova
======================================================

This plugin can be used to join two images into one image and resize it, or just resize one image. It returns the final image as a base 64 encoded JPG.

## Usage

**Join two images:**
```
plugins.JoinImages.join(options);
```

The default options object
```
var options = {

	sourceType: null, 	// must be either 'url' or 'base64' (required)

	firstImage: null, 	// first image url string or base64 string (required)

	secondImage: null, 	// second image url string or base64 string (required)

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

	sourceType: null, 	// must be either 'url' or 'base64' (required)

	image: null, 		// image url string or base64 string (required)

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
	sourceType: 'url',
	image: 'http://www.myhoundhaven.org/images/Golden%20Retriever.jpg',
	size: 1,
	success: function(encodedImage){
		document.getElementById('result').src = 'data:image/jpeg;base64,'+encodedImage;
	},
	error: function(message){
		console.log(message);
	}
});
```
