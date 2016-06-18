package droidia.com.heraldpdx;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by mandeep on 18/6/16.
 */

public class HeraldPDX extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
