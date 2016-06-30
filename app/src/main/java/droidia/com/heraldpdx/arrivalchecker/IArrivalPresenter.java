package droidia.com.heraldpdx.arrivalchecker;

import rx.Subscription;

/**
 * Created by mandeep on 18/6/16.
 */

interface IArrivalPresenter {

    Subscription getArrivalsAtLocation(String locationID);
}
