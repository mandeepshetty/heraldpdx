package droidia.com.heraldpdx.savedlocations;

/**
 * Created by mandeep on 26/6/16.
 */

public interface ISavedLocationsPresenter {

    void getSavedLocations();

    void saveLocation(HeraldLocation location);

    void removeLocation(HeraldLocation locationToSave);
}

