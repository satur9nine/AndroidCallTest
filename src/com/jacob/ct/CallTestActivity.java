package com.jacob.ct;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CallTestActivity extends Activity {

  private static final String TAG = CallTestActivity.class.getSimpleName();

  private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }

  public void onCallIntClick(View view) {
    Log.d(TAG, "+onCallIntClick");
    exec.execute(new Runnable() {
      @Override
      public void run() {
        getContentResolver().call(CallContentProvider.CONTENT_URI, CallContentProvider.METHOD_INTERRUPT, null, null);
      }
    });
    Log.d(TAG, "-onCallIntClick");
  }

  public void onCallClick(View view) {
    Log.d(TAG, "+onCallClick");
    getContentResolver().call(CallContentProvider.CONTENT_URI, CallContentProvider.METHOD_SLEEP, null, null);
    Log.d(TAG, "-onCallClick");
  }

  public void onCrashClick(View view) {
    Log.d(TAG, "+onCrashClick");
    if (true) throw new RuntimeException("DIE");
    Log.d(TAG, "-onCrashClick");
  }

  public void onCrashProviderWithClientClick(View view) {
    Log.d(TAG, "+onCrashProviderWithClientClick");

    ContentProviderClient cpc = null;
    try {
      cpc = getContentResolver().acquireContentProviderClient(CallContentProvider.CONTENT_URI);
      cpc.call(CallContentProvider.METHOD_CRASH, null, null);
    } catch (Exception e) {
      Log.w(TAG, e);
    } finally {
      if (cpc != null) {
        cpc.release();
      }
    }

    Log.d(TAG, "-onCrashProviderWithClientClick");
  }

}
