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

-(UIImage *)getImageWithDataString:(NSString*)dataString;
-(NSString *)resizeAndEncodeImage:(UIImage *)image withSize:(float)size;

@end

@implementation JoinImages

-(void)joinImagesFromData:(CDVInvokedUrlCommand *)command
{
    
    self.hasPendingOperation = YES;
    
    //Fetch images from data string arguments...
    UIImage *leftImage = [self getImageWithDataString:[command.arguments objectAtIndex:0]];
    UIImage *rightImage = [self getImageWithDataString:[command.arguments objectAtIndex:1]];
    float size = [[command.arguments objectAtIndex:2] floatValue];
    
    //Stitch images together...
    UIImage* joined = [ImageService mergeImage:leftImage withImage:rightImage];
    
    //Resize and encode
    NSString *encodedImage = [self resizeAndEncodeImage:joined withSize:size];
    
    // return encoded joinedImage
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:encodedImage] callbackId:command.callbackId];
    
    // Unset the self.hasPendingOperation property
    self.hasPendingOperation = NO;
    
}

-(void)resizeImageFromData:(CDVInvokedUrlCommand *)command
{
    self.hasPendingOperation = YES;
    
    //Fetch image from url string arguments...
    UIImage *image = [self getImageWithDataString:[command.arguments objectAtIndex:0]];
    float size = [[command.arguments objectAtIndex:1] floatValue];
    
    //Resize and encode
    NSString *encodedImage = [self resizeAndEncodeImage:image withSize:size];
    
    // return encoded image
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:encodedImage] callbackId:command.callbackId];
    
    // Unset the self.hasPendingOperation property
    self.hasPendingOperation = NO;
}

#pragma mark - Internal Methods

-(UIImage *)getImageWithDataString:(NSString*)dataString
{
    NSData *imageData = [NSData dataFromBase64String:dataString];
    UIImage *image = [UIImage imageWithData:imageData];
    return image;
}

-(NSString *)resizeAndEncodeImage:(UIImage *)image withSize:(float)size
{
    //Resize the image
    UIImage *sizedImage = [ImageService resizeImage:image withSizeInMB:size];
    
    NSData *imageData = UIImageJPEGRepresentation(sizedImage, 1.0);
    NSString *encodedImage = [imageData base64Encoding];
    
    return encodedImage;
}

@end
