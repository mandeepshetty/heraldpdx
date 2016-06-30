package droidia.com.heraldpdx.arrivalchecker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.savedlocations.HeraldLocation;
import droidia.com.heraldpdx.savedlocations.ISavedLocationsPresenter;
import droidia.com.heraldpdx.savedlocations.ISavedLocationsView;
import droidia.com.heraldpdx.savedlocations.SavedLocationsPresenter;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import droidia.com.heraldpdx.trimetapis.arrivals.Location;

public class MainActivity extends AppCompatActivity implements IArrivalListingView,
        ISavedLocationsView, View.OnKeyListener {

    private IArrivalPresenter IArrivalPresenter;
    private ISavedLocationsPresenter savedLocationsPresenter;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IArrivalPresenter = new ArrivalPresenter(this);
        savedLocationsPresenter = new SavedLocationsPresenter(this);
        initViews();
    }

    @Override
    public void displayArrivals(ArrivalResults arrivals) {

        displayLocationCard(arrivals.resultSet.location);
        adapter = new RecyclerViewAdapter(this, arrivals);
        arrivalsRecyclerView.setAdapter(adapter);
    }

    private void displayLocationCard(List<Location> location) {

        if (location.isEmpty())
            return;
        clearFavoriteButton();
        locationCardLocID.setText(String.valueOf(location.get(0).id));
        locationCardLocDesc.setText(location.get(0).desc);
        locationCard.setVisibility(View.VISIBLE);
    }


    // Unset and set listener to prevent triggering of the listener.
    private void clearFavoriteButton() {
        favoriteButton.setOnFavoriteChangeListener(null);
        favoriteButton.setFavorite(false);
        favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> favoriteButtonClicked(favorite));
    }

    @Override
    public void fetchingArrivalsFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER) {
            String locationID = this.locationID.getText().toString().trim();
            processSearch(locationID);
        }
        return false;
    }

    private void processSearch(final String locationID) {
        if (!TextUtils.isDigitsOnly(locationID)) {
            this.locationID.setError("Stop ID should be digits only.");
            return;
        }
        IArrivalPresenter.getArrivalsAtLocation(locationID);
    }

    @BindView(R.id.locationID)
    EditText locationID;
    @BindView(R.id.arrivalsRecyclerView)
    RecyclerView arrivalsRecyclerView;
    @BindView(R.id.locationCard)
    CardView locationCard;
    @BindView(R.id.locationCardLocationDescription)
    TextView locationCardLocDesc;
    @BindView(R.id.locationCardLocationID)
    TextView locationCardLocID;
    @BindView(R.id.favoriteButton)
    MaterialFavoriteButton favoriteButton;

    private void initViews() {
        ButterKnife.bind(this);
        locationID.setOnKeyListener(this);
        arrivalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> favoriteButtonClicked(favorite));
    }

    private void favoriteButtonClicked(boolean favorite) {

        HeraldLocation locationToSave = new
                HeraldLocation(locationCardLocID.getText().toString(), locationCardLocDesc.getText().toString());
        if (favorite)
            savedLocationsPresenter.saveLocation(locationToSave);
        else
            savedLocationsPresenter.removeLocation(locationToSave);
    }

    @Override
    public void locationSaved() {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displaySavedLocations(List<HeraldLocation> savedLocations) {}

    @Override
    public void locationRemoved() {
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
    }
}
