package sq.payroll.employee;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

public class OverrideUrl extends CordovaPlugin {

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
