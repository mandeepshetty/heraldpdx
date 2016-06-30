package droidia.com.heraldpdx.savedlocations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by mandeep on 26/6/16.
 */
public class SavedLocationsPresenter implements ISavedLocationsPresenter {

    private final ISavedLocationsView savedLocationsView;
    private static Set<HeraldLocation> savedLocations;

    public SavedLocationsPresenter(ISavedLocationsView savedLocationsView) {
        savedLocations = new HashSet<>();
        this.savedLocationsView = savedLocationsView;
    }

    @Override
    public void getSavedLocations() {
        if (savedLocationsView != null)
            savedLocationsView.displaySavedLocations(new ArrayList<>(savedLocations));
    }

    @Override
    public void saveLocation(HeraldLocation locationToSave) {
        savedLocations.add(locationToSave);
        Timber.d("Saved %s", locationToSave.toString());
        savedLocationsView.locationSaved();
    }

    @Override
    public void removeLocation(HeraldLocation locationToRemove) {
        savedLocations.remove(locationToRemove);
        Timber.d("Removed %s", locationToRemove.toString());
        savedLocationsView.locationRemoved();
    }
}
