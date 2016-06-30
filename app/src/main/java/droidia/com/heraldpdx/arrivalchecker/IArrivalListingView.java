package droidia.com.heraldpdx.arrivalchecker;

import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;


/**
 * Created by mandeep on 18/6/16.
 */

interface IArrivalListingView {

    void displayArrivals(ArrivalResults arrivals);

    void fetchingArrivalsFailed(String message);
}
