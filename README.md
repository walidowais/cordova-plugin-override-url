# Cordova Hello World Plugin

Plugin that allows overriding opening specific urls in the web view
Supports iOS and Android

## Using

Install the plugin

    $ cordova plugin add https://github.com/walidowais/cordova-override-url.git


Base plugin class functions overridden:
 - iOS
    `- (BOOL)shouldOverrideLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType`
- Android
    `public boolean onOverrideUrlLoading(String url)`
    `public Boolean shouldAllowNavigation(String url)`
