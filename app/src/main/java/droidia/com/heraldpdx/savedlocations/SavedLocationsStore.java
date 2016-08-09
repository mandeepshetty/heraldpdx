package droidia.com.heraldpdx.savedlocations;

import java.util.ArrayList;
import java.util.List;

import droidia.com.heraldpdx.HeraldPDX;
import droidia.com.heraldpdx.storage.RealmHeraldLocation;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by mandeep on 7/31/16.
 */

class SavedLocationsStore {

    private static SavedLocationsStore instance;
    private static Realm savedLocationsRealm;

    private SavedLocationsStore(){}

    public static SavedLocationsStore getInstance() {
        if (instance == null) {
            instance = new SavedLocationsStore();
            savedLocationsRealm = Realm.getInstance(new RealmConfiguration.Builder(HeraldPDX.getContext())
                    .name("savedlocations.realm")
                    .build());
        }
        return instance;
    }

    public void saveLocation(HeraldLocation location){

        Timber.e("Saving location in thread: %d", Thread.currentThread().getId());
        savedLocationsRealm.executeTransaction(realm -> {

            RealmHeraldLocation rhl = realm.createObject(RealmHeraldLocation.class);

            rhl.setLocationID(location.locationID);
            rhl.setLocationName(location.locationName);
            rhl.setTransportType(location.transportType);
            rhl.setLatittude(location.lattitude);
            rhl.setLongitude(location.longitude);
        });
    }

    public void removeLocation(HeraldLocation location) {

        savedLocationsRealm.executeTransaction(realm -> {
            RealmResults<RealmHeraldLocation> locationID = realm
                    .where(RealmHeraldLocation.class)
                    .equalTo("locationID", location.locationID)
                    .findAll();
            locationID.deleteAllFromRealm();
        });
    }

    public List<HeraldLocation> getSavedLocations() {

        RealmResults<RealmHeraldLocation> realmSaved =
                savedLocationsRealm.where(RealmHeraldLocation.class).findAll();

        List<HeraldLocation> savedLocations = new ArrayList<>(realmSaved.size());

        for (RealmHeraldLocation rhl: realmSaved) {

            HeraldLocation hl = new HeraldLocation(rhl.getLocationID(), rhl.getLocationName());
            savedLocations.add(hl);
        }
        return savedLocations;

    }
}
