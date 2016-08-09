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
public class ArrivalPresenter implements IArrivalPresenter {

    private IArrivalInteractor interactor;
    private IArrivalListingView listingView;


    public ArrivalPresenter(IArrivalListingView listingView) {
        this.interactor = new ArrivalInteractor();
        this.listingView = listingView;
    }

    @Override
    public Subscription getArrivalsAtLocation(String locationID) {

        return interactor
                .getArrivalsAtLocation(locationID)
                .doOnSubscribe(() -> Timber.i("Fetching arrivals for %s", locationID))
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
                        processArrivalResults(arrivalResults);
                    }
                });
    }

    private void processArrivalResults(ArrivalResults arrivalResults) {

        if (arrivalResults.containsError())
            listingView.fetchingArrivalsFailed(arrivalResults.getErrorMessage());
        else
            listingView.displayArrivals(arrivalResults);

    }
}
