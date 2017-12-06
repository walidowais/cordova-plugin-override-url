#import "CDVOverrideUrl.h"

NSString* callbackId;

@implementation CDVOverrideUrl

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
        if (callbackId)
        {
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:request.URL.absoluteString];
            [pluginResult setKeepCallbackAsBool:YES];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
        } else {
            NSLog(@"CDVOverrideUrl no callback set");
        }

        [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:CDVPluginHandleOpenURLNotification object:request.URL]];
        [[UIApplication sharedApplication] openURL:request.URL];
        return NO;
    }
    return YES;
}

static void playBeep(int count) {
    SystemSoundID completeSound;
    NSInteger cbDataCount = count;
    NSURL* audioPath = [[NSBundle mainBundle] URLForResource:@"CDVNotification.bundle/beep" withExtension:@"wav"];
    #if __has_feature(objc_arc)
        AudioServicesCreateSystemSoundID((__bridge CFURLRef)audioPath, &completeSound);
    #else
        AudioServicesCreateSystemSoundID((CFURLRef)audioPath, &completeSound);
    #endif
    AudioServicesAddSystemSoundCompletion(completeSound, NULL, NULL, soundCompletionCallback, (void*)(cbDataCount-1));
    AudioServicesPlaySystemSound(completeSound);
}

static void soundCompletionCallback(SystemSoundID  ssid, void* data) {
    int count = (int)data;
    AudioServicesRemoveSystemSoundCompletion (ssid);
    AudioServicesDisposeSystemSoundID(ssid);
    if (count > 0) {
        playBeep(count);
    }
}

- (void)beep:(CDVInvokedUrlCommand*)command
{
    NSNumber* count = [command argumentAtIndex:0 withDefault:[NSNumber numberWithInt:1]];
    playBeep([count intValue]);
}

@end