package droidia.com.heraldpdx.homescreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.arrivalchecker.MainActivity;
import droidia.com.heraldpdx.savedlocations.HeraldLocation;
import droidia.com.heraldpdx.savedlocations.ILocationListClickListener;
import droidia.com.heraldpdx.savedlocations.ISavedLocationsPresenter;
import droidia.com.heraldpdx.savedlocations.ISavedLocationsView;
import droidia.com.heraldpdx.savedlocations.LocationListRecyclerViewAdapter;
import droidia.com.heraldpdx.savedlocations.SavedLocationsPresenter;
import timber.log.Timber;

public class SavedLocationsFragment extends Fragment implements ISavedLocationsView, ILocationListClickListener {

    ISavedLocationsPresenter presenter;
    private Context context;


    public SavedLocationsFragment() {}

    @BindView(R.id.stopListRecyclerView) RecyclerView stopsList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_saved_locations, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stopsList.setLayoutManager(new LinearLayoutManager(context));
        presenter = new SavedLocationsPresenter(this);
        presenter.getSavedLocations();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override public void locationSaved() { }

    @Override
    public void displaySavedLocations(List<HeraldLocation> savedLocations) {
        stopsList.setAdapter(new LocationListRecyclerViewAdapter(savedLocations, this, context));
    }

    @Override public void locationRemoved() { }

    @Override
    public void locationClicked(String locationID) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("locid", locationID);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.i("onResume");
        presenter.getSavedLocations();
    }
}
