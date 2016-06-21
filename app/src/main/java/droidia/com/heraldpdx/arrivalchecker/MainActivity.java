package droidia.com.heraldpdx.arrivalchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;

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

    @BindView(R.id.locationID) EditText locationID;
    @BindView(R.id.arrivalsRecyclerView) RecyclerView arrivalsRecyclerView;
    private void initViews() {
        ButterKnife.bind(this);
        locationID.setOnKeyListener(this);
    }

    @Override
    public void displayArrivals(ArrivalResults arrivals) {
        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
        adapter = new RecyclerViewAdapter(arrivals);
        arrivalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrivalsRecyclerView.setAdapter(adapter);
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

    private void processSearch(String locationID) {
        if (!TextUtils.isDigitsOnly(locationID)){
            this.locationID.setError("Stop ID should be digits only.");
            return;
        }
        presenter.getArrivalsAtLocation(locationID, 1);
    }
}
