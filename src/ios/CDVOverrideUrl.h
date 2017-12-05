#import <Cordova/CDV.h>

@interface CDVOverrideUrl : CDVPlugin {
  NSString* callbackId;
}

- (void)setCallback:(CDVInvokedUrlCommand*)command;
- (BOOL)shouldOverrideLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType;

@end