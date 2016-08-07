package droidia.com.heraldpdx.savedlocations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by mandeep on 26/6/16.
 */
public class SavedLocationsPresenter implements ISavedLocationsPresenter {

    private final ISavedLocationsView savedLocationsView;
    private static final SavedLocationsStore savedLocationStore = SavedLocationsStore.getInstance();

    public SavedLocationsPresenter(ISavedLocationsView savedLocationsView) {
        this.savedLocationsView = savedLocationsView;
    }

    @Override
    public void getSavedLocations() {

        List<HeraldLocation> savedLocations = savedLocationStore.getSavedLocations();

        if (savedLocations.isEmpty())
            savedLocationsView.noSavedLocations();
        else
            savedLocationsView.displaySavedLocations(savedLocations);

    }

    @Override
    public void saveLocation(HeraldLocation locationToSave) {

        savedLocationStore.saveLocation(locationToSave);
        Timber.d("Saved %s", locationToSave.toString());
        savedLocationsView.locationSaved();
    }

    @Override
    public void removeLocation(HeraldLocation locationToRemove) {

        savedLocationStore.removeLocation(locationToRemove);
        Timber.d("Removed %s", locationToRemove.toString());
        savedLocationsView.locationRemoved();
    }
}
