//
//  JoinImages.m
//  JoinImages
//
//  Created by Scott Magee on 6/12/15.
//
//

#import "NSData+Base64.h"
#import "JoinImages.h"
#import "ImageService.h"

@interface JoinImages ()

@property (readwrite, assign) BOOL hasPendingOperation;

-(void)joinImages:(CDVInvokedUrlCommand*)command;

-(UIImage *)getImageWithURLString:(NSString*)urlString;
-(UIImage *)getImageWithDataString:(NSString*)dataString;

@end

@implementation JoinImages

-(void)joinImagesWithData:(CDVInvokedUrlCommand *)command
{
    
}

-(void)joinImagesWithURLs:(CDVInvokedUrlCommand *)command
{
    
}

-(UIImage *)getImageWithURLString:(NSString*)urlString
{
    NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:urlString]];
    UIImage *image = [UIImage imageWithData:imageData];
    return image;
}

-(UIImage *)getImageWithDataString:(NSString*)dataString
{
    NSData *imageData = [NSData dataFromBase64String:dataString];
    UIImage *image = [UIImage imageWithData:imageData];
    return image;
}

-(void)joinImages:(CDVInvokedUrlCommand*)command
{
    // Set the hasPendingOperation field to prevent the webview from crashing
    self.hasPendingOperation = YES;
    
    if (command.arguments.count < 3) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Need 2 images and a filename"] callbackId:command.callbackId];
        
        // Unset the self.hasPendingOperation property
        self.hasPendingOperation = NO;
        
        return;
    }
    
    //Get Documents Directory
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    
    //Fetch images from arguments...
    UIImage *leftImage = [self getImageWithURLString:[command.arguments objectAtIndex:0]];
    UIImage *rightImage = [self getImageWithURLString:[command.arguments objectAtIndex:1]];
    
    //Check for success of loading images.
    if (leftImage == nil || rightImage == nil) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"At least one image could not load."] callbackId:command.callbackId];
        
        // Unset the self.hasPendingOperation property
        self.hasPendingOperation = NO;
        
        return;
    }
    
    //Stitch images together...
    UIImage* merged = [ImageService mergeImage:leftImage withImage:rightImage];
    
    //Resize the image to be under 5MB
    merged = [ImageService resizeImage:merged withSizeInMB:5.0];
    
    //Create fileName and find path
    NSString* fileName = [command.arguments objectAtIndex:2];
    
    //Save completed image
    NSString* imagePath = [ImageService saveImage:merged withName:fileName andPath:documentsDirectory];
    
    //Check for success of saving the image
    if (imagePath == nil) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Could not save merged photo."] callbackId:command.callbackId];
        
        // Unset the self.hasPendingOperation property
        self.hasPendingOperation = NO;
        
        return;
    }
    
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NSString stringWithFormat:@"%@/%@", @"file://", imagePath]] callbackId:command.callbackId];
    
    // Unset the self.hasPendingOperation property
    self.hasPendingOperation = NO;
}

@end
