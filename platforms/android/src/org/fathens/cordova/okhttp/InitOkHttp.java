package org.fathens.cordova.okhttp;

import java.net.URL;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

public class InitOkHttp extends CordovaPlugin {
    private static final String TAG = "InitOkHttp";
    private static boolean isInitialized = false;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
	super.initialize(cordova, webView);

        if (!isInitialized) {
            initHttp();
            isInitialized = true;
        }
    }

    public void initHttp() {
	final OkHttpClient okHttpClient = new OkHttpClient();

	// adapted from https://github.com/mapbox/mapbox-android-sdk/pull/244
	try {
	    final SSLContext sslContext = SSLContext.getInstance("TLS");
	    sslContext.init(null, null, null);
	    okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
	} catch (GeneralSecurityException e) {
	    // The system has no TLS. Just give up.
	    throw new AssertionError(e.getLocalizedMessage());
	}

	Log.w(TAG, "Initializing an OkHttpClient instance as the URL stream handler factory forevermore by OkHttp Plugin.");
	URL.setURLStreamHandlerFactory(okHttpClient);
    }
}
