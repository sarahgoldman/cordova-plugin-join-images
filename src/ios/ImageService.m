//
//  ImageService.m
//  PictureStitch
//
//  Created by Scott Magee on 6/12/15.
//  Copyright (c) 2015 Scott Magee. All rights reserved.
//

#import "ImageService.h"
#import <MobileCoreServices/MobileCoreServices.h>

@implementation ImageService

#pragma mark - Image Rotation

+ (UIImage*)mergeImage:(UIImage*)image1 withImage:(UIImage*)image2 {
    //Find larger heigh
    double height = 0.0;
    if (image1.size.height > image2.size.height) {
        height = image1.size.height;
    } else {
        height = image2.size.height;
    }
    //Create a rect to draw in
    CGRect rect = CGRectMake(0,0,image1.size.width+image2.size.width,height);
    
    //Begin the graphics drawing context
    UIGraphicsBeginImageContext( rect.size );
    
    //Draw the first image to the left
    [image1 drawInRect:CGRectMake(0, 0, image1.size.width, image1.size.height)];
    
    //Draw the second image to the right
    [image2 drawInRect:CGRectMake(image1.size.width, 0, image2.size.width, image2.size.height)];
    
    //Get the combined image from the context
    UIImage *picture1 = UIGraphicsGetImageFromCurrentImageContext();
    
    //End the context
    UIGraphicsEndImageContext();
    
    return picture1;
}

+ (NSString*) saveImage:(UIImage *) image withName:(NSString*) fileName andPath:(NSString*) filePath {
    //Get a jpeg representation of the image
    NSData *jpegData = UIImageJPEGRepresentation(image, 1);
    
    //Combine the path components
    NSString* fullPath = [filePath stringByAppendingPathComponent:fileName];
    
    //Write the file
    BOOL success = [jpegData writeToFile:fullPath atomically:YES];
    
    //Check for success
    if (!success) {
        NSLog(@"Failed to save image...");
        return nil;
    }
    
    return fullPath;
}

+ (UIImage*) resizeImage:(UIImage *)image withSizeInMB:(double)size{
    //Convert the size given in MB to bytes
    NSData* jpegImage = [self resizeImage:image withSizeInBytes:(size*1048576)];//1048576 is 2^20
    return [UIImage imageWithData:jpegImage];
}

//TODO: Test this, could be too memory intensive...
+ (NSData*) resizeImage:(UIImage*) image withSizeInBytes:(double)Optimalsize{
    NSData  *imageData    = UIImageJPEGRepresentation(image, 1);
    double   factor       = 1.0;
    double   adjustment   = 1.0 / sqrt(2.0);  // or use 0.8 or whatever you want
    CGSize   size         = image.size;
    CGSize   currentSize  = size;
    UIImage *currentImage = image;
    
    while (imageData.length >= Optimalsize)
    {
        factor      *= adjustment;
        currentSize  = CGSizeMake(roundf(size.width * factor), roundf(size.height * factor));
        currentImage = [self resizedImage:currentImage ofSize:currentSize];
        imageData    = UIImageJPEGRepresentation(currentImage, 1);
    }
    
    return imageData;
}

+ (UIImage*) resizedImage:(UIImage*)image ofSize:(CGSize)size {
    UIGraphicsBeginImageContext(size);
    [image drawInRect:CGRectMake(0, 0, size.width, size.height)];
    UIImage *picture1 = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return picture1;
}


@end
