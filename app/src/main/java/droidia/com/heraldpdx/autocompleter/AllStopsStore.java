package droidia.com.heraldpdx.autocompleter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import droidia.com.heraldpdx.HeraldPDX;
import droidia.com.heraldpdx.savedlocations.HeraldLocation;
import droidia.com.heraldpdx.storage.RealmHeraldLocation;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import timber.log.Timber;

/**
 * Created by mandeep on 7/31/16.
 */

public class AllStopsStore {

    private Realm allStopsRealm = Realm.getInstance(new RealmConfiguration.Builder(HeraldPDX.getContext())
            .name("allstops.realm")
            .build());

    private Realm timeStampsRealm = Realm.getInstance(new RealmConfiguration.Builder(HeraldPDX.getContext())
            .name("timestamps.realm")
            .build());

    static AllStopsStore getInstance() {

        return new AllStopsStore();
    }

    private AllStopsStore (){}

    void save(Collection<RealmHeraldLocation> locations) {

        Timber.e("Saving %d items", locations.size());
        if (locations.isEmpty()) return;

        allStopsRealm.executeTransaction(realm -> realm.deleteAll());

        for (RealmHeraldLocation rhl: locations){
            try {
                allStopsRealm.beginTransaction();
                allStopsRealm.copyToRealmOrUpdate(rhl);
                allStopsRealm.commitTransaction();
            }
            catch (RealmException rexp) {
                rexp.printStackTrace();
            }

        }


    }

    void destroyInstance() {
        allStopsRealm.close();
        allStopsRealm = null;
    }

    void nuke(){
        allStopsRealm.close();
        Realm.deleteRealm(allStopsRealm.getConfiguration());
    }

    List<RealmHeraldLocation> getAll(){

        RealmResults<RealmHeraldLocation> allStops =
                allStopsRealm.where(RealmHeraldLocation.class).findAll();
        Timber.i("findAll: %d", allStops.size());
        return new ArrayList<>(allStops);

    }
}
