package droidia.com.heraldpdx.arrivalchecker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.Utils;
import droidia.com.heraldpdx.savedlocations.HeraldLocation;
import droidia.com.heraldpdx.savedlocations.ISavedLocationsPresenter;
import droidia.com.heraldpdx.savedlocations.ISavedLocationsView;
import droidia.com.heraldpdx.savedlocations.SavedLocationsPresenter;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import droidia.com.heraldpdx.trimetapis.arrivals.Location;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements IArrivalListingView,
        ISavedLocationsView {

    private IArrivalPresenter arrivalPresenter;
    private ISavedLocationsPresenter savedLocationsPresenter;
    private ArrivalsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrivalPresenter = new ArrivalPresenter(this);
        savedLocationsPresenter = new SavedLocationsPresenter(this);
        initViews();
//        AutoCompleter.initialize();
        String locid = getIntent().getStringExtra("locid");
        if (locid != null) {
            Timber.i("Received intent with location ID: %s", locid);
            arrivalPresenter.getArrivalsAtLocation(locid);
        }

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            arrivalPresenter.getArrivalsAtLocation(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        searchView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_in));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                arrivalPresenter.getArrivalsAtLocation(query);
//                hideSoftKeyboard();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        return true;
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void displayArrivals(ArrivalResults arrivals) {

        List<Location> location = arrivals.resultSet.location;
        clearFavoriteButton();
        locationCardLocID.setText(String.valueOf(location.get(0).id));
        locationCardLocDesc.setText(location.get(0).desc);

        locationCard.setX(Utils.getScreenWidth(this));
        locationCard.animate()
                .translationX(0)
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        locationCard.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        adapter = new ArrivalsRecyclerViewAdapter(getApplicationContext(), arrivals);
                        arrivalsRecyclerView.setAdapter(adapter);
                    }
                }).start();
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


    @BindView(R.id.arrivalsRecyclerView) RecyclerView arrivalsRecyclerView;
    @BindView(R.id.locationCard) CardView locationCard;
    @BindView(R.id.locationCardLocationDescription) TextView locationCardLocDesc;
    @BindView(R.id.locationCardLocationID) TextView locationCardLocID;
    @BindView(R.id.favoriteButton) MaterialFavoriteButton favoriteButton;
    private void initViews() {
        ButterKnife.bind(this);
        arrivalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> favoriteButtonClicked(favorite));
    }

    private void favoriteButtonClicked(boolean favorite) {

        HeraldLocation locationToSave = new
                HeraldLocation(locationCardLocID.getText().toString(),
                locationCardLocDesc.getText().toString());
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
    public void displaySavedLocations(List<HeraldLocation> savedLocations) {
    }

    @Override
    public void locationRemoved() {
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noSavedLocations() {

    }
}
