package com.cordova.plugins.overrideurl;

import org.apache.cordova.*;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;



import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.EditText;
import android.widget.TextView;



public class OverrideUrl extends CordovaPlugin {
    protected static final String LOG_TAG = "OverrideUrl";

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback context used when calling back into JavaScript.
     * @return                  True when the action was valid, false otherwise.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        /*
         * Don't run any of these if the current activity is finishing
         * in order to avoid android.view.WindowManager$BadTokenException
         * crashing the app. Just return true here since false should only
         * be returned in the event of an invalid action.
         */
        if(this.cordova.getActivity().isFinishing()) return true;

        if (action.equals("beep")) {
            this.beep(args.getLong(0));
        }
        else if (action.equals("setCallback")) {
            this.setCallback(args.getLong(0));
        }
        else {
            return false;
        }

        // Only alert and confirm are async.
        callbackContext.success();
        return true;
    }

    /**
     * Beep plays the default notification ringtone.
     *
     * @param count     Number of times to play notification
     */
    public void beep(final long count) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone notification = RingtoneManager.getRingtone(cordova.getActivity().getBaseContext(), ringtone);

                // If phone is not set to silent mode
                if (notification != null) {
                    for (long i = 0; i < count; ++i) {
                        notification.play();
                        long timeout = 5000;
                        while (notification.isPlaying() && (timeout > 0)) {
                            timeout = timeout - 100;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setCallback(final long count) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone notification = RingtoneManager.getRingtone(cordova.getActivity().getBaseContext(), ringtone);

                // If phone is not set to silent mode
                if (notification != null) {
                    for (long i = 0; i < count; ++i) {
                        notification.play();
                        long timeout = 5000;
                        while (notification.isPlaying() && (timeout > 0)) {
                            timeout = timeout - 100;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOverrideUrlLoading(String url) {
        LOG.d(LOG_TAG, "OverrideUrl#onOverrideUrlLoading: " + url);
        if (url.contains("squareupstaging.com/payroll/pdf")) {
            openExternal(url);
            return true;
        }
        return false;
    }

    @Override
    public Boolean shouldAllowNavigation(String url) {
        LOG.d(LOG_TAG, "OverrideUrl#shouldAllowNavigation: " + url);
        if (!url.matches("(?!.*(pdf)).*(file:\\/\\/|squareupstaging.com\\/(login|logout|payroll|mp/redirect)).*") || url.contains("squareupstaging.com/payroll/pdf")) {
            openExternal(url);
            return false;
        }
        return true;
    }

    /**
     * Display a new browser with the specified URL.
     *
     * @param url the url to load.
     * @return "" if ok, or error message.
     */
    public String openExternal(String url) {
        try {
            Intent intent = null;
            intent = new Intent(Intent.ACTION_VIEW);
            // Omitting the MIME type for file: URLs causes "No Activity found to handle Intent".
            // Adding the MIME type to http: URLs causes them to not be handled by the downloader.
            Uri uri = Uri.parse(url);
            if ("file".equals(uri.getScheme())) {
                intent.setDataAndType(uri, webView.getResourceApi().getMimeType(uri));
            } else {
                intent.setData(uri);
            }
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, cordova.getActivity().getPackageName());
            this.cordova.getActivity().startActivity(intent);
            return "";
        // not catching FileUriExposedException explicitly because buildtools<24 doesn't know about it
        } catch (java.lang.RuntimeException e) {
            LOG.d(LOG_TAG, "OverrideUrl: Error loading url "+url+":"+ e.toString());
            return e.toString();
        }
    }
}
