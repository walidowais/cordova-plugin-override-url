#import <Cordova/CDV.h>

@interface CDVOverrideUrl : CDVPlugin

- (BOOL)shouldOverrideLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType;

@end