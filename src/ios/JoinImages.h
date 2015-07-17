//
//  JoinImages.h
//  JoinImages
//
//  Created by Scott Magee on 6/12/15.
//
//

#import <Cordova/CDVPlugin.h>

@interface JoinImages : CDVPlugin

#pragma mark - Plugin Method
/**
 * Called by Cordova. Takes two photos provided via two file paths, and merges them into one photo saved in the documents directory with the filename provided as the third argument.
 **/
-(void)joinImagesWithData:(CDVInvokedUrlCommand*)command;
-(void)joinImagesWithURLs:(CDVInvokedUrlCommand*)command;
-(void)resizeImageFromURL:(CDVInvokedUrlCommand*)command;
-(void)resizeImageFromData:(CDVInvokedUrlCommand*)command;

@end
