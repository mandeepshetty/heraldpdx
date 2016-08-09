package droidia.com.heraldpdx.autocompleter;

import android.content.Context;

import java.io.File;
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

    private int size = 0;
    private Realm allStopsRealm = Realm.getInstance(new RealmConfiguration.Builder(HeraldPDX.getContext())
            .name("allstops.realm")
            .build());

    private Realm timeStampsRealm = Realm.getInstance(new RealmConfiguration.Builder(HeraldPDX.getContext())
            .name("timestamps.realm")
            .build());

    public static AllStopsStore getInstance() {

        return new AllStopsStore();
    }

    private AllStopsStore (){}

    void save(Collection<RealmHeraldLocation> locations) {

        Timber.e("Saving %d items", locations.size());
        size = locations.size();
        if (locations.isEmpty()) return;

        allStopsRealm.executeTransaction(realm -> {
            realm.deleteAll();
            allStopsRealm.copyToRealm(locations);
        });

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
        ArrayList<RealmHeraldLocation> realmHeraldLocations = new ArrayList<>(allStops);
        Timber.i("lis findall : %d", realmHeraldLocations.size());
        return realmHeraldLocations;

    }

    public List<RealmHeraldLocation> getForStopID(String stopID){

        RealmResults<RealmHeraldLocation> allStops =
                allStopsRealm.where(RealmHeraldLocation.class).findAll();
        Timber.i("findAll: %d", allStops.size());
        return new ArrayList<>(allStops);

    }

    public long getRealmfileSize() {

        File realmFile = new File(allStopsRealm.getPath());
        return realmFile.length();
    }
}
