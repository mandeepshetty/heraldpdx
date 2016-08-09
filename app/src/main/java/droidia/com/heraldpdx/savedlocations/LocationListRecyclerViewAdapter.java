package droidia.com.heraldpdx.savedlocations;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.Utils;
import droidia.com.heraldpdx.arrivalchecker.ArrivalInteractor;
import droidia.com.heraldpdx.arrivalchecker.IArrivalListingView;
import droidia.com.heraldpdx.trimetapis.arrivals.Arrival;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by mandeep on 26/6/16.
 */

public class LocationListRecyclerViewAdapter extends
        RecyclerView.Adapter<LocationListRecyclerViewAdapter.LocationViewHolder>
        implements ILocationListClickListener, IArrivalListingView {

    private final ILocationListClickListener listener;
    private final ArrivalInteractor arrivalsInteractor;
    private final Context context;
    private List<HeraldLocation> locationList;
    private Map<HeraldLocation, ArrivalResults> savedLocations;
    private static final Observable<Long> refresher = Observable.defer(() ->
            Observable.interval(1, 1, TimeUnit.MINUTES));
    private static Subscription refresherSubscription = null;

    public LocationListRecyclerViewAdapter(List<HeraldLocation> locationList, ILocationListClickListener
            listener, Context context) {

        this.locationList = locationList;
        this.savedLocations = new LinkedHashMap<>(locationList.size());
        arrivalsInteractor = new ArrivalInteractor();

        this.listener = listener;
        this.context = context;

        refreshArrivalsForSavedLocations();

        Observable.timer(1, 1, TimeUnit.MINUTES)
                .subscribe(aLong -> refreshArrivalsForSavedLocations());

        if (refresherSubscription == null){
            refresherSubscription = refresher.subscribe();
        } else {
            if (refresherSubscription.isUnsubscribed())
                refresherSubscription = refresher.subscribe();
        }

    }

    public void unsubscribeRefresher (){

        if (refresherSubscription != null ) {
            if (!refresherSubscription.isUnsubscribed()) {
                refresherSubscription.unsubscribe();
            }
            refresherSubscription = null;
        }
    }
    private void refreshArrivalsForSavedLocations() {

        Timber.e("Refreshing...");
        Observable.range(0, this.locationList.size())
                .map(i -> {
                    HeraldLocation l = this.locationList.get(i);
                    ArrivalResults results = arrivalsInteractor.getArrivalsAtLocation(l.locationID).toBlocking().single();
                    return new Pair<>(i, results);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> {
                    Timber.e("Refreshed !");
                    notifyDataSetChanged();
                })
                .subscribe(enumResults -> savedLocations.put(this.locationList.get(enumResults.first), enumResults.second));
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_location_item, parent, false);
        return new LocationViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {

        HeraldLocation l = locationList.get(position);
        holder.locationID.setText(l.locationID);
        holder.locationName.setText(l.locationName);

        if (savedLocations.get(l) != null) {

            ArrivalResults arrivals = savedLocations.get(l);

            for (int i = 0; i < arrivals.resultSet.arrival.size() && i != 2; i++) {
                Arrival arrival = arrivals.resultSet.arrival.get(i);
                String fullsign = Utils.getArrivalDescription(arrival);
                String estimatedTime = Utils.getEstimatedTimeString(arrival);

                if (i == 0) {
                    holder.arrival_1_Desc.setText(fullsign);
                    holder.arrival_1_estimate.setText(estimatedTime);
                }
                else if (i == 1) {
                    holder.arrival_2_Desc.setText(fullsign);
                    holder.arrival_2_estimate.setText(estimatedTime);
                }
                else break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    @Override
    public void locationClicked(String locationID) {
        listener.locationClicked(locationID);
    }

    @Override
    public void displayArrivals(ArrivalResults arrivals) {

    }

    @Override
    public void fetchingArrivalsFailed(String message) {

    }

    static class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ILocationListClickListener clickListener;

        @BindView(R.id.savedLocationStopID) TextView locationID;
        @BindView(R.id.savedLocationStopName) TextView locationName;
        @BindView(R.id.sl_arrival_1_desc) TextView arrival_1_Desc;
        @BindView(R.id.sl_arrival_2_desc) TextView arrival_2_Desc;
        @BindView(R.id.sl_arrival_1_estimate) TextView arrival_1_estimate;
        @BindView(R.id.sl_arrival_2_estimate) TextView arrival_2_estimate;
        @BindView(R.id.sl_arrival_1_icon) ImageView arrival_1_icon;
        @BindView(R.id.sl_arrival_2_icon) ImageView arrival_2_icon;
        LocationViewHolder(View itemView, ILocationListClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.locationClicked(locationID.getText().toString().trim());
        }
    }
}

