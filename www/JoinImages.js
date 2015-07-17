module.exports = {
	
	join: function(options){
		
		options = options || {};
		
		// internals
		this.URL_TYPE = 'url';
		this.BASE64_TYPE = 'base64';
		this.URL_IOS_JOIN_METHOD = 'joinImagesWithURLs';
		this.BASE64_IOS_JOIN_METHOD = 'joinImagesWithData';
		this.URL_IOS_RESIZE_METHOD = 'resizeImageFromURL';
		this.BASE64_IOS_RESIZE_METHOD = 'resizeImageFromData';
		this.isJoin = false;
		
		// options
		this.type = options.type; // type, either url or base64 (required)
		
		this.firstImage = options.firstImage; // first image file path (required)

		this.secondImage = options.secondImage; // second image file path
		
		this.size = (options.size && options.size > 0) ? options.size : 5; // file output size in MB, default to 5MB
		
		// make sure callbacks are functions or reset to null
		this.successCallback = (options.success && typeof(options.success) === 'function') ? options.success : null; 

		this.errorCallback = (options.error && typeof(options.error) === 'function') ? options.error : null;
		
		// make sure both images and the final file name are set	
		if (!this.type || !this.firstImage) {
			if (this.errorCallback) {
				this.errorCallback({
					success: false,
					error: "Parameters 'type' and 'firstImage' are required."
				});
			}
			return false;
		}
		
		// make sure type is one of the two defined types
		if (this.type !== this.URL_TYPE && this.type !== this.BASE64_TYPE) {
			if (this.errorCallback) {
				this.errorCallback({
					success: false,
					error: "Parameter 'type' must be 'url' or 'base64'."
				});
			}
			return false;
		}
		
		var method, args;
		
		// if there's a second image...
		if (this.secondImage) {
			// use one of the join methods
			method = (this.type === this.URL_TYPE) ? this.URL_IOS_JOIN_METHOD : this.BASE64_IOS_JOIN_METHOD;
			// pass both images and the size
			args = [this.firstImage, this.secondImage, this.size];
		} else {
			// use one of the resize methods
			method = (this.type === this.URL_TYPE) ? this.URL_IOS_RESIZE_METHOD : this.BASE64_IOS_RESIZE_METHOD;
			// pass one image and the size
			args = [this.firstImage, this.size];
		}
		
		// make the call
        cordova.exec(this.successCallback, this.errorCallback, 'JoinImages', method, args);

	}
	
};