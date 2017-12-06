#import "CDVOverrideUrl.h"

NSString* callbackId;

@implementation CDVOverrideUrl

- (void)_ready:(CDVInvokedUrlCommand*)command
{
    _eventsCallbackId = command.callbackId;
}

- (void)fireUrlEvent:(NSString*)url
{
    if (_eventsCallbackId == nil) {
        NSLog(@"CDVOverrideUrl no callback set");
        return;
    }
    NSDictionary* payload = @{@"type": @"overrideUrl", @"url": url};
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:payload];
    [result setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:result callbackId:_eventsCallbackId];
}

- (void)setCallback:(CDVInvokedUrlCommand*)command
{
    NSLog(@"CDVOverrideUrl setting callback %@", command.callbackId);
    callbackId = command.callbackId;

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"callback set"];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (BOOL)shouldOverrideLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSLog(@"CDVOverrideUrl checking url %@", request.URL.absoluteString);
    NSRange range = [request.URL.absoluteString rangeOfString:@"(?!.*(pdf)).*(file:\/\/|squareupstaging.com\/(login|logout|payroll|mp/redirect)).*"
                                                      options:NSRegularExpressionSearch];

    if ((navigationType == UIWebViewNavigationTypeLinkClicked && range.location == NSNotFound) ||
        [request.URL.absoluteString containsString:@"squareupstaging.com/payroll/pdf"])
    {
        NSLog(@"CDVOverrideUrl overriding url %@", request.URL.absoluteString);
        [self fireUrlEvent];
        return NO;
    }
    return YES;
}

@end