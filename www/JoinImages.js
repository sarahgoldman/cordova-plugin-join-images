module.exports = {
	
	join: function(options){
		
		options = options || {};
		
		this.leftImage = options.leftImage; // left image file path (required)

		this.rightImage = options.rightImage; // right image file path (required)
		
		this.destination = options.destination; // destination file name (required)
		
		// make sure callbacks are functions or reset to null
		this.successCallback = (options.success && typeof(options.success) === 'function') ? options.success : null; 

		this.errorCallback = (options.error && typeof(options.error) === 'function') ? options.error : null;
		
		// make sure both images and the final file name are set	
		if (!this.leftImage || !this.rightImage || !this.destination) {
			if (this.errorCallback) {
				this.errorCallback({
					success: false,
					error: "Parameters 'leftImage', 'rightImage', and 'destination' are required."
				});
			}
			return false;
		}
		
		// arguments for ios method
		var args = [this.leftImage, this.rightImage, this.destination]; 
		
		// make the call
        cordova.exec(this.successCallback, this.errorCallback, 'JoinImages', 'joinImages', args);

	}
	
};