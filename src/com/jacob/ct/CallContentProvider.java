package com.jacob.ct;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class CallContentProvider extends ContentProvider {

  private static final String TAG = CallContentProvider.class.getSimpleName();

  public static final String AUTHORITY = "com.jacob.ct";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

  public static final String METHOD_SLEEP = "sleep";
  public static final String METHOD_INTERRUPT = "interrupt";
  public static final String METHOD_CRASH = "crash";

  public boolean onCreate() {
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getType(Uri uri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Uri insert(Uri uri, ContentValues contentValues) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int delete(Uri uri, String s, String[] strings) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bundle call(String method, String arg, Bundle extras) {
    Log.d(TAG, "Call with method: " + method);
    Log.d(TAG, "Thread "+ Thread.currentThread().getName() + " before isInterrupted()=" + Thread.currentThread().isInterrupted());

    if (METHOD_INTERRUPT.equals(method)) {
      final Thread callThread = Thread.currentThread();

      new Thread("interrupter") {
        @Override
        public void run() {
          callThread.interrupt();
        }
      }.start();
    } else if (METHOD_CRASH.equals(method)) {
      new Thread() {
        @Override
        public void run() {
          if (true) throw new RuntimeException("DIE");
        }
      }.start();
    } else if (METHOD_SLEEP.equals(method)) {
      // They all sleep
    } else {
      throw new UnsupportedOperationException("Unsupported method: " + method);
    }

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Log.w(TAG, e);
      Thread.currentThread().interrupt();
    }

    Log.d(TAG, "Thread "+ Thread.currentThread().getName() + " after isInterrupted()=" + Thread.currentThread().isInterrupted());

    return null;
  }


}
