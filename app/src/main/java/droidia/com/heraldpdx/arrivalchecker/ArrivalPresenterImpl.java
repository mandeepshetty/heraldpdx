package droidia.com.heraldpdx.arrivalchecker;

import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by mandeep on 18/6/16.
 */
public class ArrivalPresenterImpl implements ArrivalPresenter {

    private ArrivalInteractor interactor;
    private ArrivalListingView listingView;


    public ArrivalPresenterImpl(ArrivalListingView listingView) {
        this.interactor = new ArrivalInteractorImpl();
        this.listingView = listingView;
    }

    @Override
    public Subscription getArrivalsAtLocation(String locationID, int noOfArrivals) {

        return interactor
                .getArrivalsAtLocation(locationID, noOfArrivals)
                .doOnSubscribe(() -> Timber.i("Fetching %d arrivals for %s", noOfArrivals, locationID))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrivalResults>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        listingView.fetchingArrivalsFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrivalResults arrivalResults) {

                    }
                });
    }
}