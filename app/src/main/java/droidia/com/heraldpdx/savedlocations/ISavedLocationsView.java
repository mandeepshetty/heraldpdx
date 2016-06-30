package droidia.com.heraldpdx.savedlocations;

import java.util.List;

/**
 * Created by mandeep on 26/6/16.
 */
public interface ISavedLocationsView {

    void locationSaved();

    void displaySavedLocations(List<HeraldLocation> savedLocations);

    void locationRemoved();
}

