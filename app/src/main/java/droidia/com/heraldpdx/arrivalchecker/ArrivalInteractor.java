package droidia.com.heraldpdx.arrivalchecker;

import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import rx.Observable;

/**
 * Created by mandeep on 18/6/16.
 */

interface ArrivalInteractor {

    Observable<ArrivalResults> getArrivalsAtLocation(String locationID, int noOfArrivals);
}
