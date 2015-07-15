//
//  ImageService.h
//  PictureStitch
//
//  Created by Scott Magee on 6/12/15.
//  Copyright (c) 2015 Scott Magee. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ImageService : NSObject

/**
 * Saves an image at the given path with the given name as JPEG format
 * @param image     image to save
 * @param fileName  name for the image
 * @param filePath  path to save the image
 * @return the combined fileName and filePath
 **/
+ (NSString*) saveImage:(UIImage*)image withName:(NSString*)fileName andPath:(NSString*)filePath;

/**
 * Merges two UIImages. Returns one image with image1 to the left of image2.
 * @param image1    first image
 * @param image2    second image
 * @return combined image
 **/
+ (UIImage*)mergeImage:(UIImage*)image1 withImage:(UIImage*)image2;

/**
 * Resizes an image until it is under a certain number of MegaBytes
 * @param image image to be resized
 * @param size  size specified in MB
 * @return image shrunk until size is less than specified MB
 **/
+ (UIImage*) resizeImage:(UIImage*) image withSizeInMB:(double)size;

@end
