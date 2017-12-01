#import "CDVOverrideUrl.h"

@implementation CDVOverrideUrl

- (BOOL)shouldOverrideLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSRange range = [request.URL.absoluteString rangeOfString:@"(?!.*(pdf)).*(file:\/\/|squareupstaging.com\/(login|logout|payroll|mp/redirect)).*"
                                                      options:NSRegularExpressionSearch];

    if ((navigationType == UIWebViewNavigationTypeLinkClicked && range.location == NSNotFound) ||
        [request.URL.absoluteString containsString:@"squareupstaging.com/payroll/pdf"])
    {
        [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:CDVPluginHandleOpenURLNotification object:request.URL]];
        [[UIApplication sharedApplication] openURL:request.URL];
        return NO;
    }
    return YES;
}

@end