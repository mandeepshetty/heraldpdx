package droidia.com.heraldpdx.homescreen;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import droidia.com.heraldpdx.R;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class SavedLocationsFragment extends Fragment {

    public SavedLocationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.e("yes!");
        return inflater.inflate(R.layout.fragment_saved_locations, container, false);
    }
}
