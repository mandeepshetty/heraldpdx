package droidia.com.heraldpdx;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import timber.log.Timber;

/**
 * Created by mandeep on 18/6/16.
 */

public class HeraldPDX extends Application {

    private static HeraldPDX instance;

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {

        instance = this;
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        );
    }
}
