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

@property (nonatomic, copy) UIImage *leftImage;
@property (nonatomic, copy) UIImage *rightImage;

@property (readwrite, assign) BOOL hasPendingOperation;

-(UIImage *)getImageWithURLString:(NSString*)urlString;
-(UIImage *)getImageWithDataString:(NSString*)dataString;
-(void)joinImages:(CDVInvokedUrlCommand*)command;

@end

@implementation JoinImages

@synthesize leftImage, rightImage;

-(void)joinImagesWithData:(CDVInvokedUrlCommand *)command
{
    //Fetch images from data string arguments...
    self.leftImage = [self getImageWithDataString:[command.arguments objectAtIndex:0]];
    self.rightImage = [self getImageWithDataString:[command.arguments objectAtIndex:1]];
    [self joinImages:command];
}

-(void)joinImagesWithURLs:(CDVInvokedUrlCommand *)command
{
    //Fetch images from url string arguments...
    self.leftImage = [self getImageWithURLString:[command.arguments objectAtIndex:0]];
    self.rightImage = [self getImageWithURLString:[command.arguments objectAtIndex:1]];
    [self joinImages:command];
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
    
    //Check for success of loading images.
    if (self.leftImage == nil || self.rightImage == nil) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"At least one image could not load."] callbackId:command.callbackId];
        
        // Unset the self.hasPendingOperation property
        self.hasPendingOperation = NO;
        
        return;
    }
    
    //Stitch images together...
    UIImage* merged = [ImageService mergeImage:self.leftImage withImage:self.rightImage];
    
    //Resize the image to be under 5MB
    merged = [ImageService resizeImage:merged withSizeInMB:5.0];
    
    NSData *mergedData = UIImageJPEGRepresentation(merged, 1.0);
    NSString *encodedMerged = [mergedData base64Encoding];
    
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:encodedMerged] callbackId:command.callbackId];
    
    // Unset the self.hasPendingOperation property
    self.hasPendingOperation = NO;
    
    return;

}


@end
