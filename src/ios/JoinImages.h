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
 * Called by Cordova. Takes one or two photos as encoded strings, 
 * and merges them into one photo if needed, scales it to the
 * given size limit and returns it as an encoded string.
 **/
-(void)joinImagesFromData:(CDVInvokedUrlCommand*)command;
-(void)resizeImageFromData:(CDVInvokedUrlCommand*)command;

@end
