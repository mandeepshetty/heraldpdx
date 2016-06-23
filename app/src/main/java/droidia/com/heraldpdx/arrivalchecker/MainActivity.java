package droidia.com.heraldpdx.arrivalchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import droidia.com.heraldpdx.trimetapis.arrivals.Location;

public class MainActivity extends AppCompatActivity implements ArrivalListingView, View.OnKeyListener {

    private ArrivalPresenter presenter;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new ArrivalPresenterImpl(this);

        initViews();
    }

    @Override
    public void displayArrivals(ArrivalResults arrivals) {

        displayLocationCard(arrivals.resultSet.location);
        adapter = new RecyclerViewAdapter(arrivals);
        arrivalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrivalsRecyclerView.setAdapter(adapter);
    }

    private void displayLocationCard(List<Location> location) {

        if (location.isEmpty())
            return;

        locationCardLocID.setText(String.valueOf(location.get(0).id));
        locationCardLocDesc.setText(location.get(0).desc);
        locationCard.setVisibility(View.VISIBLE);
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
        if (!TextUtils.isDigitsOnly(locationID)){
            this.locationID.setError("Stop ID should be digits only.");
            return;
        }
        presenter.getArrivalsAtLocation(locationID);
    }

    @BindView(R.id.locationID) EditText locationID;
    @BindView(R.id.arrivalsRecyclerView) RecyclerView arrivalsRecyclerView;
    @BindView(R.id.locationCard) CardView locationCard;
    @BindView(R.id.locationCardLocationDescription) TextView locationCardLocDesc;
    @BindView(R.id.locationCardLocationID) TextView locationCardLocID;
    @BindView(R.id.favoriteButton) MaterialFavoriteButton favoriteButton;
    private void initViews() {
        ButterKnife.bind(this);
        locationID.setOnKeyListener(this);
        favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> favoriteButtonClicked(favorite));
    }

    private void favoriteButtonClicked(boolean favorite) {

        if (favorite) Toast.makeText(this, "favourited!", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "unfavourited!", Toast.LENGTH_SHORT).show();
    }
}
