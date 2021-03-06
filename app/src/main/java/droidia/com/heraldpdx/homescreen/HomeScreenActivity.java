package droidia.com.heraldpdx.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.Utils;
import droidia.com.heraldpdx.arrivalchecker.MainActivity;
import droidia.com.heraldpdx.autocompleter.AutoCompleter;
import droidia.com.heraldpdx.savedlocations.HeraldLocation;
import droidia.com.heraldpdx.savedlocations.*;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class HomeScreenActivity extends AppCompatActivity implements ISavedLocationsView,
        ILocationListClickListener,
        FloatingSearchView.OnSearchListener,
        FloatingSearchView.OnMenuItemClickListener {

    private static final long ANIM_DURATION_TOOLBAR = 200;
    private ISavedLocationsPresenter presenter;
    boolean introAnimationPending = true;
    private final int COUNT_OF_FUZZY_MATCHES = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initViews();

        if (savedInstanceState != null) {
            introAnimationPending = false;
        }
        presenter = new SavedLocationsPresenter(this);
        AutoCompleter.initialize();
//        pager.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.getSavedLocations();
    }

    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.noSavedLocationsMessage)
    TextView noSavedLocationsMessage;
    @BindView(R.id.savedLocationsRecyclerView)
    RecyclerView savedLocationsRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    EditText searchTextView;

    private void initViews() {

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        savedLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingSearchView.setOnBindSuggestionCallback(bindSuggestionCallback);
        floatingSearchView.setOnMenuItemClickListener(this);
        floatingSearchView.setOnSearchListener(this);
        // Get access to the edit text in floating search view so we can RX that bitch with debounce.
        searchTextView = (EditText) floatingSearchView.findViewById(R.id.search_bar_text);

        RxTextView.textChangeEvents(searchTextView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(textViewTextChangeEvent -> textViewTextChangeEvent.text().length() > 0)
                .subscribe(textViewTextChangeEvent -> {

                    List<HeraldLocation> newSuggestions = new ArrayList<>(COUNT_OF_FUZZY_MATCHES);
                    floatingSearchView.showProgress();

                    Subscription subscribe = AutoCompleter
                            .getFuzzyMatches(textViewTextChangeEvent.text().toString(), COUNT_OF_FUZZY_MATCHES)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(location -> newSuggestions.add(location))
                            .doOnCompleted(() -> {
                                floatingSearchView.swapSuggestions(newSuggestions);
                                floatingSearchView.hideProgress();
                            })
                            .subscribe();
                });
    }

    private SearchSuggestionsAdapter.OnBindSuggestionCallback bindSuggestionCallback =
            (suggestionView, leftIcon, textView, item, itemPosition) -> {

                HeraldLocation rhl = (HeraldLocation) item;


                String formattedHTML = getFormattedHTMLForSuggestion(rhl);
                textView.setText(Html.fromHtml(formattedHTML));

                if (rhl.transportType.equalsIgnoreCase("BUS")) {
                    leftIcon.setImageDrawable(this.getDrawable(R.drawable.ic_blackbus));
                } else {
                    leftIcon.setImageDrawable(this.getDrawable(R.drawable.ic_bluetrain));
                }

            };

    @NonNull
    private String getFormattedHTMLForSuggestion(HeraldLocation rhl) {

        // StringBuilder size is the length of the suggestion and at most every character in pattern
        // is replaced with 7 characters for the HTML bold  and the direction String.
        // This is a rough approximation.
        StringBuilder sb = new StringBuilder(
                rhl.locationName.length() +
                        rhl.getDirection().length() +
                        (floatingSearchView.getQuery().length() * 7)
        );
        String locationName = rhl.getBody();
        String query = floatingSearchView.getQuery();
        int queryMarker = 0;
        int locationNameMarker = 0;
        for (locationNameMarker = 0;
             locationNameMarker < locationName.length() && queryMarker < query.length();
             locationNameMarker++) {

            String locationNameChar = String.valueOf(locationName.charAt(locationNameMarker));
            String queryChar = String.valueOf(query.charAt(queryMarker));

            if (locationNameChar.equalsIgnoreCase(queryChar)) {
                sb.append("<b>").append(locationNameChar).append("</b>");
                queryMarker++;
            } else {
                sb.append(locationNameChar);
            }

        }

        // Query done. If more characters in location name left, tack them on.
        if (locationNameMarker != locationName.length()) {
            sb.append(locationName.substring(locationNameMarker));
        }
        sb.append("<br><small>").append(rhl.getDirection()).append("</small>");
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        searchView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_in));
//        ComponentName cn = new ComponentName(this, MainActivity.class);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        startIntroAnimation();

        return true;
    }

    private void startIntroAnimation() {

        int actionbarSize = Utils.dpToPx(56);
        toolbar.setTranslationY(-actionbarSize);
        floatingSearchView.setTranslationY(-actionbarSize);

        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        floatingSearchView.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("onResume");
        presenter.getSavedLocations();
    }

    @Override
    protected void onStop() {

        super.onStop();

    }

    @Override
    public void locationClicked(String locationID) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("locid", locationID);
        startActivity(i);
    }

    @Override
    public void locationSaved() {

    }

    @Override
    public void displaySavedLocations(List<HeraldLocation> savedLocations) {
        noSavedLocationsMessage.setVisibility(View.GONE);
        savedLocationsRecyclerView.setAdapter(new LocationListRecyclerViewAdapter(savedLocations, this, this));
    }

    @Override
    public void locationRemoved() {

    }

    @Override
    public void noSavedLocations() {
        noSavedLocationsMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        Intent i = new Intent(this, MainActivity.class);
        HeraldLocation hl = (HeraldLocation) searchSuggestion;
        i.putExtra("locid", hl.locationID.trim());
        startActivity(i);
    }

    @Override
    public void onSearchAction(String currentQuery) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("locid", currentQuery.trim());
        startActivity(i);
    }

    @Override
    public void onActionMenuItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filterByStopID: {

                if (item.isChecked()) {
                    Toast.makeText(this, "Name mode", Toast.LENGTH_SHORT).show();
                    floatingSearchView.setMenuItemIconColor(R.color.colorAccent);
                    item.setChecked(false);
                } else {
                    Toast.makeText(this, "Stop mode", Toast.LENGTH_SHORT).show();
                    floatingSearchView.setMenuItemIconColor(R.color.light_gray_inactive_icon);
                    item.setChecked(true);
                }
            }
        }
    }
}
