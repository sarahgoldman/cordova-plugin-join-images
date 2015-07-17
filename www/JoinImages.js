module.exports = {
	
	join: function(options){
		
		options = options || {};
		
		this.URL_TYPE: 'url',
		this.BASE64_TYPE: 'base64',
		this.URL_IOS_METHOD: 'joinImagesWithURLs',
		this.BASE64_IOS_METHOD: 'joinImagesWithData',
		
		this.type = options.type; // type, either url or base64 (required)
		
		this.leftImage = options.leftImage; // left image file path (required)

		this.rightImage = options.rightImage; // right image file path (required)
		
		// make sure callbacks are functions or reset to null
		this.successCallback = (options.success && typeof(options.success) === 'function') ? options.success : null; 

		this.errorCallback = (options.error && typeof(options.error) === 'function') ? options.error : null;
		
		// make sure both images and the final file name are set	
		if (!this.type || !this.leftImage || !this.rightImage) {
			if (this.errorCallback) {
				this.errorCallback({
					success: false,
					error: "Parameters 'type', 'leftImage', and 'rightImage' are required."
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
		
		// arguments for ios method
		var args = [this.leftImage, this.rightImage]; 
		
		// depending on the type of data, set the appropriate ios method to call
		var method = (this.type === this.URL_TYPE) ? this.URL_IOS_METHOD : this.BASE64_IOS_METHOD;
		
		// make the call
        cordova.exec(this.successCallback, this.errorCallback, 'JoinImages', method, args);

	}
	
};