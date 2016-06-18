package droidia.com.heraldpdx.arrivalchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;

public class MainActivity extends AppCompatActivity implements ArrivalListingView {

    private ArrivalPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new ArrivalPresenterImpl(this);

        test();
    }

    private void test() {
        presenter.getArrivalsAtLocation("9832", 2);
    }

    @Override
    public void displayArrivals(List<ArrivalResults> arrivals) {
        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fetchingArrivalsFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
