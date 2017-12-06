#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioServices.h>
#import <Cordova/CDVPlugin.h>

@interface CDVOverrideUrl : CDVPlugin {
    NSString* _eventsCallbackId;
}

- (void)_ready:(CDVInvokedUrlCommand*)command;
- (void)fireUrlEvent:(NSString*)url;
- (void)setCallback:(CDVInvokedUrlCommand*)command;
- (BOOL)shouldOverrideLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType;

@end