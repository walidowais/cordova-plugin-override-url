#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioServices.h>
#import <Cordova/CDVPlugin.h>

@interface CDVOverrideUrl : CDVPlugin

extern NSString* callbackId;

- (void)beep:(CDVInvokedUrlCommand*)command;
- (void)setCallback:(CDVInvokedUrlCommand*)command;
- (BOOL)shouldOverrideLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType;

@end