module.exports = {
	
	// internals
	URL: 'url',
	BASE64: 'base64',
	IOS_CLASS: 'JoinImages',
	IOS_JOIN_METHOD_URL: 'joinImagesWithURLs',
	IOS_JOIN_METHOD_BASE64: 'joinImagesWithData',
	IOS_RESIZE_METHOD_URL: 'resizeImageFromURL',
	IOS_RESIZE_METHOD_BASE64: 'resizeImageFromData',
	
	join: function(options){
		
		options = options || {};
		
		// options
		this.sourceType = options.sourceType; // source type, either url or base64 (required)
		
		this.firstImage = options.firstImage; // first image data or url string (required)

		this.secondImage = options.secondImage; // second image data or url string (required)
		
		this.size = (options.size && options.size > 0) ? options.size : 5; // file output size (MB), default to 5
		
		// make sure callbacks are functions or reset to null
		this.successCallback = (options.success && typeof(options.success) === 'function') ? options.success : null; 

		this.errorCallback = (options.error && typeof(options.error) === 'function') ? options.error : null;
		
		// make sure the source type and images are set	
		if (!this.sourceType || !this.firstImage || !this.secondImage) {
			if (this.errorCallback) {
				this.errorCallback("Parameters 'sourceType', 'firstImage' and 'secondImage' are required.");
			}
			return false;
		}
		
		// make sure source type is one of the two defined types
		if (this.sourceType !== this.URL && this.sourceType !== this.BASE64) {
			if (this.errorCallback) {
				this.errorCallback("Parameter 'sourceType' must be 'url' or 'base64'.");
			}
			return false;
		}
		
		// use one of the join methods based on the source type
		var	method = (this.sourceType === this.URL) ? this.IOS_JOIN_METHOD_URL : this.IOS_JOIN_METHOD_BASE64;
		// pass both images and the size
		var	args = [this.firstImage, this.secondImage, this.size];
		
		// make the call
        cordova.exec(this.successCallback, this.errorCallback, this.IOS_CLASS, method, args);

	},
	
	resize: function(options){
		
		options = options || {};
		
		// options
		this.sourceType = options.sourceType; // source type, either url or base64 (required)
		
		this.image = options.image; // image data or url string (required)
		
		this.size = (options.size && options.size > 0) ? options.size : 5; // file output size (MB), default to 5
		
		// make sure callbacks are functions or reset to null
		this.successCallback = (options.success && typeof(options.success) === 'function') ? options.success : null; 

		this.errorCallback = (options.error && typeof(options.error) === 'function') ? options.error : null;
		
		// make sure the source type and image are set	
		if (!this.sourceType || !this.image) {
			if (this.errorCallback) {
				this.errorCallback("Parameters 'sourceType' and 'image' are required.");
			}
			return false;
		}
		
		// make sure source type is one of the two defined types
		if (this.sourceType !== this.URL && this.sourceType !== this.BASE64) {
			if (this.errorCallback) {
				this.errorCallback("Parameter 'sourceType' must be 'url' or 'base64'.");
			}
			return false;
		}
		
		// use one of the resize methods based on the source type
		var	method = (this.sourceType === this.URL) ? this.IOS_RESIZE_METHOD_URL : this.IOS_RESIZE_METHOD_BASE64;
		// pass first image and the size
		var	args = [this.image, this.size];
		
		// make the call
        cordova.exec(this.successCallback, this.errorCallback, this.IOS_CLASS, method, args);

	}
	
};